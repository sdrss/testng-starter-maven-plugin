package com.github.sdrss.testngstarter.mvnplugin.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlSuite.FailurePolicy;
import org.testng.xml.XmlSuite.ParallelMode;
import org.uncommons.reportng.HTMLReporter;
import org.uncommons.reportng.dto.TestNGSemantics;

import com.google.common.base.Strings;

public final class TestNGStarterMainClass {
	
	public static final Logger logger = LoggerFactory.getLogger(TestNGStarterMainClass.class);
	public static final String STRIPE = "===============================================";
	public static final String testNG_Retry_Suite_Name = "testng-failed.xml";
	public static final String testNG_Retry_Path = "_RetryFailures";
	public static final String testNG_Post_Path = "_Post";
	static Boolean isJunit = null;
	static Boolean retryFailures = false;
	static Boolean postBuildSuites = false;
	static Boolean useReportNG = false;
	static Boolean failOnError = false;
	static String testOutputDirectory = "";
	static String reportNGOutputDirectory = "";
	
	public static void execute(Properties properties) {
		// Initialize
		logger.info("Start TestNG");
		TestNG tng = new TestNG();
		initTestNG(tng, properties);
		if (useReportNG) {
			initReportNG(tng, properties);
		}
		setSystemProperties(properties);
		tng.run();
		// Get Status
		int status = tng.getStatus();
		logger.info("TestNG status is : [ " + TestNGStatus.TestNGStatusGet(status) + " ] ");
		// Print Summary
		printSummary();
		// Execute testng-failed.xml
		if (retryFailures) {
			tng = new TestNG();
			Properties retryProperties = new Properties();
			retryProperties.putAll(properties);
			retryProperties.setProperty(TestParameters.suiteXmlFiles.name(), testOutputDirectory + "/" + testNG_Retry_Suite_Name);
			retryProperties.setProperty(TestParameters.reportNGOutputDirectory.name(), reportNGOutputDirectory + testNG_Retry_Path);
			initTestNG(tng, retryProperties);
			if (useReportNG) {
				initReportNG(tng, retryProperties);
			}
			setSystemProperties(retryProperties);
			tng.run();
		}
		// Execute Post Suites
		if (postBuildSuites) {
			tng = new TestNG();
			Properties postProperties = new Properties();
			postProperties.putAll(properties);
			postProperties.setProperty(TestParameters.suiteXmlFiles.name(), properties.getProperty(TestParameters.suiteXmlFilesPostBuild.name()));
			postProperties.setProperty(TestParameters.suiteXmlFilesPostBuild.name(), "");
			reportNGOutputDirectory = reportNGOutputDirectory.replace(testNG_Retry_Path, "");
			postProperties.setProperty(TestParameters.reportNGOutputDirectory.name(), reportNGOutputDirectory + testNG_Post_Path);
			postProperties.setProperty(TestParameters.executeTestngFailedxml.name(), "false");
			initTestNG(tng, postProperties);
			if (useReportNG) {
				initReportNG(tng, postProperties);
			}
			setSystemProperties(postProperties);
			tng.run();
		}
		if (failOnError) {
			Execution.abort();
		}
		Execution.normal();
	}
	
	private static void setSystemProperties(Properties properties) {
		if (properties.get(TestParameters.systemProperties.name()) != null) {
			@SuppressWarnings("unchecked")
			List<String> systemPropertiesList = (List<String>) properties.get(TestParameters.systemProperties.name());
			if (systemPropertiesList != null) {
				for (String temp : systemPropertiesList) {
					String[] splitter = temp.split(":");
					if (splitter.length == 2) {
						System.setProperty(splitter[0], splitter[1]);
					} else if (splitter.length == 1) {
						// Or throw Exception ? for null SystemProperty ?
						System.setProperty(splitter[0], "");
					}
				}
			}
		}
	}
	
	private static void initReportNG(TestNG tng, Properties properties) {
		if (properties.get(TestParameters.reportNGOutputDirectory.name()) != null) {
			try {
				System.setProperty(HTMLReporter.LOG_OUTPUT_REPORT_PATH, (String) properties.get(TestParameters.reportNGOutputDirectory.name()));
			} catch (Exception ex) {
				logger.debug(TestParameters.reportNGOutputDirectory.name(), ex);
			}
		} else {
			System.setProperty(HTMLReporter.LOG_OUTPUT_REPORT_PATH, (String) properties.get(TestParameters.reportNGOutputDirectory.name()));
		}
		reportNGOutputDirectory = System.getProperty(HTMLReporter.LOG_OUTPUT_REPORT_PATH);
		
		if (properties.get(TestParameters.showPassedConfigurations.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.showPassedConfigurations.name())) {
					System.setProperty(HTMLReporter.SHOW_PASSED_CONFIGURATIONS, "true");
				} else {
					System.setProperty(HTMLReporter.SHOW_PASSED_CONFIGURATIONS, "false");
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.showPassedConfigurations.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.handleKnownDefectsAsFailures.name()) != null) {
			try {
				if (!(Boolean) properties.get(TestParameters.handleKnownDefectsAsFailures.name())) {
					System.setProperty(HTMLReporter.KWOWNDEFECTSMODE, "true");
				} else {
					System.setProperty(HTMLReporter.KWOWNDEFECTSMODE, "false");
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.handleKnownDefectsAsFailures.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.escapeOutput.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.escapeOutput.name())) {
					System.setProperty(HTMLReporter.ESCAPE_OUTPUT, "true");
				} else {
					System.setProperty(HTMLReporter.ESCAPE_OUTPUT, "false");
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.escapeOutput.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.logOutputReport.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.logOutputReport.name())) {
					System.setProperty(HTMLReporter.LOG_OUTPUT_REPORT, "true");
				} else {
					System.setProperty(HTMLReporter.LOG_OUTPUT_REPORT, "false");
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.logOutputReport.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.externalLinks.name()) != null) {
			try {
				// TODO HTMLReporter.EXTERNAL_LINKS
			} catch (Exception ex) {
				logger.debug(TestParameters.externalLinks.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.argumentsTitle.name()) != null) {
			try {
				// TODO HTMLReporter.ARGUMENTS_TITLE
			} catch (Exception ex) {
				logger.debug(TestParameters.argumentsTitle.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.reportNGhtmlReportTitle.name()) != null) {
			try {
				String title = (String) properties.get(TestParameters.reportNGhtmlReportTitle.name());
				System.setProperty(HTMLReporter.REPORTNG_TITLE, title);
			} catch (Exception ex) {
				logger.debug(TestParameters.failFast.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.failOnErrors.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.failOnErrors.name())) {
					failOnError = true;
				} else {
					failOnError = false;
				}
				
			} catch (Exception ex) {
				logger.debug(TestParameters.failOnErrors.name(), ex);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void initTestNG(TestNG tng, Properties properties) {
		List<Class<? extends ITestNGListener>> listenerClasses = new ArrayList<Class<? extends ITestNGListener>>();
		
		if (properties.get(TestParameters.threadPoolSize.name()) != null) {
			try {
				tng.setThreadCount((Integer) (properties.get(TestParameters.threadPoolSize.name())));
			} catch (Exception ex) {
				logger.debug(TestParameters.threadPoolSize.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.suiteThreadPoolSize.name()) != null) {
			try {
				tng.setSuiteThreadPoolSize((Integer) (properties.get(TestParameters.suiteThreadPoolSize.name())));
			} catch (Exception ex) {
				logger.debug(TestParameters.suiteThreadPoolSize.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.dataProviderThreadCount.name()) != null) {
			try {
				tng.setDataProviderThreadCount((Integer) (properties.get(TestParameters.dataProviderThreadCount.name())));
			} catch (Exception ex) {
				logger.debug(TestParameters.dataProviderThreadCount.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.outputDirectory.name()) != null) {
			try {
				tng.setOutputDirectory((String) properties.get(TestParameters.outputDirectory.name()));
			} catch (Exception ex) {
				logger.debug(TestParameters.outputDirectory.name(), ex);
				logger.debug("Set Default " + TestNG.DEFAULT_OUTPUTDIR);
				tng.setOutputDirectory(TestNG.DEFAULT_OUTPUTDIR);
			}
		} else {
			tng.setOutputDirectory(TestNG.DEFAULT_OUTPUTDIR);
		}
		testOutputDirectory = tng.getOutputDirectory();
		
		if (properties.get(TestParameters.preserveOrder.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.preserveOrder.name())) {
					tng.setPreserveOrder(true);
				} else {
					tng.setPreserveOrder(false);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.preserveOrder.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.isJUnit.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.isJUnit.name())) {
					tng.setJUnit(true);
					isJunit = true;
				} else {
					tng.setJUnit(false);
					isJunit = false;
				}
				
			} catch (Exception ex) {
				logger.debug(TestParameters.isJUnit.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.configFailurePolicy.name()) != null) {
			try {
				String configFailurePolicy = (String) properties.get(TestParameters.configFailurePolicy.name());
				if (!Strings.isNullOrEmpty(configFailurePolicy)) {
					if (configFailurePolicy.equalsIgnoreCase(FailurePolicy.SKIP.name())) {
						tng.setConfigFailurePolicy(FailurePolicy.SKIP);
					} else if (configFailurePolicy.equalsIgnoreCase(FailurePolicy.CONTINUE.name())) {
						tng.setConfigFailurePolicy(FailurePolicy.CONTINUE);
					} else {
						logger.debug("Do nothing for " + TestParameters.configFailurePolicy.name());
					}
				}
				
			} catch (Exception ex) {
				logger.debug(TestParameters.configFailurePolicy.name(), ex);
			}
		}
		// Disable All Listeners and enable one by one according to params
		tng.setUseDefaultListeners(false);
		// HTML Listener
		if (properties.get(TestParameters.generateHtmlReport.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.generateHtmlReport.name())) {
					listenerClasses.add(org.testng.reporters.SuiteHTMLReporter.class);
					listenerClasses.add(org.testng.reporters.EmailableReporter.class);
					listenerClasses.add(org.testng.reporters.EmailableReporter2.class);
					listenerClasses.add(org.testng.reporters.TestHTMLReporter.class);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.generateHtmlReport.name(), ex);
			}
		}
		// XML Listener
		if (properties.get(TestParameters.generateXMLReport.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.generateXMLReport.name())) {
					if (isJunit != null) {
						if (isJunit) {
							listenerClasses.add(org.testng.reporters.JUnitXMLReporter.class);
						} else {
							listenerClasses.add(org.testng.reporters.XMLReporter.class);
						}
					}
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.generateXMLReport.name(), ex);
			}
		}
		// Junit Listener
		if (properties.get(TestParameters.generateJunitReport.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.generateJunitReport.name())) {
					listenerClasses.add(org.testng.reporters.JUnitReportReporter.class);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.generateJunitReport.name(), ex);
			}
		}
		// ReportNG main Listener
		if (properties.get(TestParameters.generateReportNGhtmlReport.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.generateReportNGhtmlReport.name())) {
					listenerClasses.add(org.uncommons.reportng.HTMLReporter.class);
					useReportNG = true;
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.generateReportNGhtmlReport.name(), ex);
			}
		}
		// Time Out Listener
		if (properties.get(TestParameters.globalTestTimeOut.name()) != null) {
			try {
				Long testTimeout = (Long) properties.get(TestParameters.globalTestTimeOut.name());
				if (testTimeout > 0) {
					if (!listenerClasses.contains(org.uncommons.reportng.listeners.IAnnotationTransformerListener.class)) {
						listenerClasses.add(org.uncommons.reportng.listeners.IAnnotationTransformerListener.class);
					}
					System.setProperty(HTMLReporter.TEST_TIMEOUT, testTimeout.toString());
				} else {
					System.setProperty(HTMLReporter.TEST_TIMEOUT, "0");
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.globalTestTimeOut.name(), ex);
			}
		}
		// Retry Listener
		if (properties.get(TestParameters.maxTestRetryFailures.name()) != null) {
			try {
				Integer testRetry = (Integer) properties.get(TestParameters.maxTestRetryFailures.name());
				if (testRetry > 0) {
					if (!listenerClasses.contains(org.uncommons.reportng.listeners.IAnnotationTransformerListener.class)) {
						listenerClasses.add(org.uncommons.reportng.listeners.IAnnotationTransformerListener.class);
					}
					System.setProperty(HTMLReporter.TEST_MAX_RETRY_COUNT, testRetry.toString());
				} else {
					System.setProperty(HTMLReporter.TEST_MAX_RETRY_COUNT, "0");
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.maxTestRetryFailures.name(), ex);
			}
		}
		// Fail Fast Listener
		if (properties.get(TestParameters.failFast.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.failFast.name())) {
					listenerClasses.add(org.uncommons.reportng.listeners.FailFastListener.class);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.failFast.name(), ex);
			}
		}
		// Custom Listeners
		if (properties.get(TestParameters.listeners.name()) != null) {
			try {
				String listenersCommaSeparated = (String) properties.get(TestParameters.listeners.name());
				List<String> customListeners = Arrays.asList(listenersCommaSeparated.split(","));
				if (!customListeners.isEmpty()) {
					for (String tempListener : customListeners) {
						listenerClasses.add((Class<? extends ITestNGListener>) Class.forName(tempListener));
					}
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.listeners.name(), ex);
			}
		}
		// Retry Failures Listener to create testng-failed.xml file
		if (properties.get(TestParameters.executeTestngFailedxml.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.executeTestngFailedxml.name())) {
					listenerClasses.add(org.testng.reporters.FailedReporter.class);
					retryFailures = true;
				} else {
					logger.debug("Do nothing");
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.executeTestngFailedxml.name(), ex);
			}
		}
		if (properties.get(TestParameters.excludedGroups.name()) != null) {
			try {
				String excludedGroupsCommaSeparated = (String) properties.get(TestParameters.excludedGroups.name());
				if (!excludedGroupsCommaSeparated.isEmpty()) {
					tng.setExcludedGroups(excludedGroupsCommaSeparated);
				} else {
					tng.setExcludedGroups(null);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.excludedGroups.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.groups.name()) != null) {
			try {
				String groupsCommaSeparated = (String) properties.get(TestParameters.groups.name());
				if (!groupsCommaSeparated.isEmpty()) {
					tng.setGroups(groupsCommaSeparated);
				} else {
					tng.setGroups(null);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.groups.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.parallel.name()) != null) {
			try {
				String parallel = (String) properties.get(TestParameters.parallel.name());
				if (!Strings.isNullOrEmpty(parallel)) {
					if (parallel.equalsIgnoreCase(ParallelMode.CLASSES.name())) {
						tng.setParallel(ParallelMode.CLASSES);
					} else if (parallel.equalsIgnoreCase(ParallelMode.INSTANCES.name())) {
						tng.setParallel(ParallelMode.INSTANCES);
					} else if (parallel.equalsIgnoreCase(ParallelMode.METHODS.name())) {
						tng.setParallel(ParallelMode.METHODS);
					} else if (parallel.equalsIgnoreCase(ParallelMode.NONE.name())) {
						tng.setParallel(ParallelMode.NONE);
					} else if (parallel.equalsIgnoreCase(ParallelMode.TESTS.name())) {
						tng.setParallel(ParallelMode.TESTS);
					} else {
						tng.setParallel(ParallelMode.NONE);
					}
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.parallel.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.randomizeSuites.name()) != null) {
			try {
				if ((Boolean) properties.get(TestParameters.randomizeSuites.name())) {
					tng.setRandomizeSuites(true);
				} else {
					tng.setRandomizeSuites(false);
				}
				
			} catch (Exception ex) {
				logger.debug(TestParameters.randomizeSuites.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.suiteXmlFiles.name()) == null) {
			throw new IllegalStateException("No suite files were specified");
		} else {
			List<String> testSuites = new ArrayList<>();
			if (properties.get(TestParameters.suiteXmlFiles.name()) != null) {
				try {
					String testSuitesCommaSeparated = (String) properties.get(TestParameters.suiteXmlFiles.name());
					testSuites = Arrays.asList(testSuitesCommaSeparated.split(","));
				} catch (Exception ex) {
					logger.debug(TestParameters.suiteXmlFiles.name(), ex);
				}
			}
			if (!testSuites.isEmpty()) {
				List<File> testSuitesPaths = new ArrayList<>(testSuites.size());
				for (String temp : testSuites) {
					String path = "./";
					File suitePath = new java.io.File(path.concat("/").concat(temp.trim()));
					if (!suitePath.isFile()) {
						throw new IllegalStateException("Suite file " + temp + " is not a valid file");
					}
					testSuitesPaths.add(suitePath);
				}
				tng.setTestSuites(testSuites);
			} else {
				throw new IllegalStateException("No suite files were specified");
			}
		}
		
		if (properties.get(TestParameters.suiteXmlFilesPostBuild.name()) == null) {
			postBuildSuites = false;
		} else {
			try {
				String testSuitesCommaSeparated = (String) properties.get(TestParameters.suiteXmlFilesPostBuild.name());
				if (testSuitesCommaSeparated.isEmpty()) {
					postBuildSuites = false;
				} else {
					postBuildSuites = true;
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.suiteXmlFilesPostBuild.name(), ex);
			}
		}
		
		// Set Listeners
		tng.setListenerClasses(listenerClasses);
		// Print
		for (Class<? extends ITestNGListener> temp : listenerClasses) {
			logger.info("Listener : " + temp.getCanonicalName());
		}
	}
	
	private static void printSummary() {
		// For Jenkins Integration Print parsed Console output
		// ${BUILD_LOG_REGEX, regex="<br>", linesBefore=0, linesAfter=0,
		// maxMatches=0, showTruncatedLines=false, substText=" ",
		// escapeHtml=true, matchedLineHtmlStyle="null", addNewline=true}
		systemOut(STRIPE);
		String passed = System.getProperty(TestNGSemantics.PASS);
		if (passed == null || "null".equalsIgnoreCase(passed)) {
			passed = "0";
		}
		String failed = System.getProperty(TestNGSemantics.FAILED);
		if (failed == null || "null".equalsIgnoreCase(failed)) {
			failed = "0";
		}
		String skipped = System.getProperty(TestNGSemantics.SKIP);
		if (skipped == null || "null".equalsIgnoreCase(skipped)) {
			skipped = "0";
		}
		String knownDefect = System.getProperty(TestNGSemantics.KNOWN_DEFECT);
		if (knownDefect == null || "null".equalsIgnoreCase(knownDefect)) {
			knownDefect = "0";
		}
		// Print Title
		systemOut("Title : " + System.getProperty(HTMLReporter.REPORTNG_TITLE));
		systemOut("Total Passed: " + passed + " Failures: " + failed + " Skips: " + skipped + " KnownDefects: " + knownDefect + "");
		systemOut(STRIPE);
	}
	
	public static void systemOut(String text) {
		System.out.println(text);
	}
	
	public static void systemErr(String text) {
		System.err.println(text);
	}
}