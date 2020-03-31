package com.github.sdrss.testngstarter.mvnplugin;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.transfer.dependencies.resolve.DependencyResolver;

import com.github.sdrss.testngstarter.mvnplugin.helper.LoaderFinder;

@Mojo(defaultPhase = LifecyclePhase.TEST, name = "test", requiresProject = true, threadSafe = true, requiresDependencyResolution = ResolutionScope.TEST)
public class TestNGStarterMojo extends AbstractTestNGStarterMojo {
	
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;
	
	@Component
	private DependencyResolver dependencyResolver;
	
	@Component
	private PluginDescriptor pluginDescriptor;
	
	@Parameter(property = "skipTests", defaultValue = "false")
	protected boolean skipTests;
	
	private final String testNGStarterMainClass = "com.github.sdrss.testngstarter.mvnplugin.helper.TestNGStarterMainClass";
	private final String methodName = "execute";
	
	public void execute() throws MojoExecutionException {
		Log logger = getLog();
		if (verifyParameters(logger)) {
			final Properties properties = new Properties();
			initProperties(properties);
			IsolatedThreadGroup threadGroup = new IsolatedThreadGroup(testNGStarterMainClass);
			Thread bootstrapThread = new Thread(threadGroup, new Runnable() {
				public void run() {
					try {
						Class<?> bootClass = Thread.currentThread().getContextClassLoader().loadClass(testNGStarterMainClass);
						MethodHandles.Lookup lookup = MethodHandles.lookup();
						MethodHandle mainHandle = lookup.findStatic(bootClass, methodName, MethodType.methodType(void.class, Properties.class));
						mainHandle.invoke(properties);
					} catch (IllegalAccessException e) { // just pass it on
						Thread.currentThread().getThreadGroup().uncaughtException(Thread.currentThread(), new Exception("The specified mainClass doesn't contain a main method with appropriate signature.", e));
					} catch (InvocationTargetException e) { // use the cause if available to improve the plugin execution output
						Throwable exceptionToReport = e.getCause() != null ? e.getCause() : e;
						Thread.currentThread().getThreadGroup().uncaughtException(Thread.currentThread(), exceptionToReport);
					} catch (Throwable e) { // just pass it on
						Thread.currentThread().getThreadGroup().uncaughtException(Thread.currentThread(), e);
					}
				}
			}, testNGStarterMainClass + ".execute(properties)");
			// Load ClassLoader
			ClassLoader classLoader = getClassLoader();
			bootstrapThread.setContextClassLoader(classLoader);
			bootstrapThread.start();
			joinNonDaemonThreads(threadGroup);
			if (true) {
				terminateThreads(threadGroup);
				try {
					threadGroup.destroy();
				} catch (IllegalThreadStateException e) {
					getLog().warn("Couldn't destroy threadgroup " + threadGroup, e);
				}
			}
			synchronized (threadGroup) {
				if (threadGroup.uncaughtException != null) {
					throw new MojoExecutionException("An exception occured while executing the Java class. "
							+ threadGroup.uncaughtException.getMessage(), threadGroup.uncaughtException);
				}
			}
		}
	}
	
	protected boolean verifyParameters(Log logger) {
		if (isSkip()) {
			logger.info("Tests are skipped.");
			return false;
		}
		return true;
	}
	
	private boolean isSkip() {
		return skipTests;
	}
	
	class IsolatedThreadGroup extends ThreadGroup {
		private Throwable uncaughtException;
		
		public IsolatedThreadGroup(String name) {
			super(name);
		}
		
		public void uncaughtException(Thread thread, Throwable throwable) {
			if (throwable instanceof ThreadDeath) {
				return;
			}
			synchronized (this) {
				if (uncaughtException == null) {
					uncaughtException = throwable;
				}
			}
			getLog().warn(throwable);
		}
	}
	
	private ClassLoader getClassLoader() throws MojoExecutionException {
		List<Path> classpathURLs = new ArrayList<>();
		addRelevantProjectDependenciesToClasspath(classpathURLs);
		try {
			return LoaderFinder.find(classpathURLs, testNGStarterMainClass);
		} catch (NullPointerException | IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	private void addRelevantProjectDependenciesToClasspath(List<Path> path) throws MojoExecutionException {
		getLog().debug("Project Dependencies will be included.");
		List<Artifact> artifacts = new ArrayList<>();
		List<Path> theClasspathFiles = new ArrayList<>();
		collectProjectArtifactsAndClasspath(artifacts, theClasspathFiles);
		for (Path classpathFile : theClasspathFiles) {
			getLog().debug("Adding to classpath : " + classpathFile);
			path.add(classpathFile);
		}
		for (Artifact classPathElement : artifacts) {
			if (classPathElement.getFile() != null) {
				getLog().debug("Adding project dependency artifact: " + classPathElement.getArtifactId() + " to classpath");
				path.add(classPathElement.getFile().toPath());
			} else {
				getLog().debug("! Adding project dependency artifact: " + classPathElement.getArtifactId() + " to classpath");
			}
		}
	}
	
	protected void collectProjectArtifactsAndClasspath(List<Artifact> artifacts, List<Path> theClasspathFiles) {
		artifacts.add(project.getArtifact());
		artifacts.addAll(project.getArtifacts());
		artifacts.addAll(project.getPluginArtifacts());
		artifacts.addAll(pluginDescriptor.getArtifacts());
		theClasspathFiles.add(Paths.get(project.getBuild().getOutputDirectory()));
		theClasspathFiles.add(Paths.get(project.getBuild().getTestOutputDirectory()));
	}
	
	private void joinNonDaemonThreads(ThreadGroup threadGroup) {
		boolean foundNonDaemon;
		do {
			foundNonDaemon = false;
			Collection<Thread> threads = getActiveThreads(threadGroup);
			for (Thread thread : threads) {
				if (thread.isDaemon()) {
					continue;
				}
				foundNonDaemon = true;
				joinThread(thread, 0);
			}
		} while (foundNonDaemon);
	}
	
	private Collection<Thread> getActiveThreads(ThreadGroup threadGroup) {
		Thread[] threads = new Thread[threadGroup.activeCount()];
		int numThreads = threadGroup.enumerate(threads);
		Collection<Thread> result = new ArrayList<Thread>(numThreads);
		for (int i = 0; i < threads.length && threads[i] != null; i++) {
			result.add(threads[i]);
		}
		return result;
	}
	
	private void joinThread(Thread thread, long timeoutMsecs) {
		try {
			getLog().debug("joining on thread " + thread);
			thread.join(timeoutMsecs);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // good practice if don't throw
			getLog().warn("interrupted while joining against thread " + thread, e); // not expected!
		}
		if (thread.isAlive()) {
			getLog().warn("thread " + thread + " was interrupted but is still alive after waiting at least " + timeoutMsecs + "msecs");
		}
	}
	
	private void terminateThreads(ThreadGroup threadGroup) {
		long startTime = System.currentTimeMillis();
		Set<Thread> uncooperativeThreads = new HashSet<Thread>(); // these were not responsive to interruption
		for (Collection<Thread> threads = getActiveThreads(threadGroup); !threads.isEmpty(); threads = getActiveThreads(threadGroup), threads.removeAll(uncooperativeThreads)) {
			// Interrupt all threads we know about as of this instant (harmless if spuriously went dead (! isAlive())
			// or if something else interrupted it ( isInterrupted() ).
			for (Thread thread : threads) {
				getLog().debug("interrupting thread " + thread);
				thread.interrupt();
			}
			// Now join with a timeout and call stop() (assuming flags are set right)
			for (Thread thread : threads) {
				if (!thread.isAlive()) {
					continue; // and, presumably it won't show up in getActiveThreads() next iteration
				}
				int daemonThreadJoinTimeout = 15000;
				if (daemonThreadJoinTimeout <= 0) {
					joinThread(thread, 0); // waits until not alive; no timeout
					continue;
				}
				long timeout = daemonThreadJoinTimeout - (System.currentTimeMillis() - startTime);
				if (timeout > 0) {
					joinThread(thread, timeout);
				}
				if (!thread.isAlive()) {
					continue;
				}
				uncooperativeThreads.add(thread); // ensure we don't process again
			}
		}
		if (!uncooperativeThreads.isEmpty()) {
			getLog().warn("NOTE: " + uncooperativeThreads.size() + " thread(s) did not finish despite being asked to "
					+ " via interruption. This is not a problem with mvn plugin, it is a problem with the running code."
					+ " Although not serious, it should be remedied.");
		} else {
			int activeCount = threadGroup.activeCount();
			if (activeCount != 0) {
				Thread[] threadsArray = new Thread[1];
				threadGroup.enumerate(threadsArray);
				getLog().debug("strange; " + activeCount + " thread(s) still active in the group " + threadGroup + " such as " + threadsArray[0]);
			}
		}
	}
	
}
