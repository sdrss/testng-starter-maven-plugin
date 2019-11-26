package com.github.sdrss.testngstarter.mvnplugin;

import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.sdrss.testngstarter.mvnplugin.helper.TestParameters;

public class AbstractTestNGStarterMojo extends AbstractMojo {
	
	@Parameter(property = "threadPoolSize", defaultValue = "1")
	private Integer threadPoolSize;
	
	@Parameter(property = "suiteThreadPoolSize", defaultValue = "1")
	private Integer suiteThreadPoolSize;
	
	@Parameter(property = "dataProviderThreadCount", defaultValue = "1")
	private Integer dataProviderThreadCount;
	
	@Parameter(property = "outputDirectory", defaultValue = "outputDirectory")
	private String outputDirectory;
	
	@Parameter(property = "preserveOrder", defaultValue = "true")
	private Boolean preserveOrder;
	
	@Parameter(property = "configFailurePolicy", defaultValue = "skip")
	private String configFailurePolicy;
	
	@Parameter(property = "htmlReport", defaultValue = "false")
	private Boolean htmlReport;
	
	@Parameter(property = "xmlReport", defaultValue = "true")
	private Boolean xmlReport;
	
	@Parameter(property = "junitReport", defaultValue = "false")
	private Boolean junitReport;
	
	@Parameter(property = "excludedGroups", defaultValue = "")
	private String excludedGroups;
	
	@Parameter(property = "groups", defaultValue = "")
	private String groups;
	
	@Parameter(property = "suiteXmlFiles", required = true)
	private String suiteXmlFiles;
	
	@Parameter(property = "listeners", defaultValue = "")
	private String listeners;
	
	@Parameter(property = "isJUnit", defaultValue = "false")
	private Boolean isJUnit;
	
	@Parameter(property = "parallel", defaultValue = "false")
	private String parallel;
	
	@Parameter(property = "randomizeSuites", defaultValue = "false")
	private Boolean randomizeSuites;
	
	@Parameter(property = "reportNGListener", defaultValue = "true")
	private Boolean reportNGListener;
	
	@Parameter(property = "retryFailures", defaultValue = "false")
	private Boolean retryFailures;
	
	@Parameter(property = "failFast", defaultValue = "false")
	private Boolean failFast;
	
	@Parameter(property = "failOnErrors", defaultValue = "false")
	private Boolean failOnErrors;
	
	@Parameter(property = "showPassedConfigurations", defaultValue = "true")
	private Boolean showPassedConfigurations;
	
	@Parameter(property = "knownDefectsMode", defaultValue = "true")
	private Boolean knownDefectsMode;
	
	@Parameter(property = "logOutputReport", defaultValue = "false")
	private Boolean logOutputReport;
	
	@Parameter(property = "title", defaultValue = "ReportNG")
	private String title;
	
	@Parameter(property = "testTimeout", defaultValue = "0")
	private Long testTimeout;
	
	@Parameter(property = "testRetry", defaultValue = "0")
	private Integer testRetry;
	
	@Parameter(property = "systemProperties")
	private List<String> systemProperties;
	
	public void initProperties(Properties properties) {
		properties.put(TestParameters.threadPoolSize.name(), threadPoolSize);
		properties.put(TestParameters.suiteThreadPoolSize.name(), suiteThreadPoolSize);
		properties.put(TestParameters.dataProviderThreadCount.name(), dataProviderThreadCount);
		properties.put(TestParameters.outputDirectory.name(), outputDirectory);
		properties.put(TestParameters.preserveOrder.name(), preserveOrder);
		properties.put(TestParameters.configFailurePolicy.name(), configFailurePolicy);
		properties.put(TestParameters.htmlReport.name(), htmlReport);
		properties.put(TestParameters.xmlReport.name(), xmlReport);
		properties.put(TestParameters.junitReport.name(), junitReport);
		properties.put(TestParameters.reportNGListener.name(), reportNGListener);
		properties.put(TestParameters.logOutputReport.name(), logOutputReport);
		if (listeners != null) {
			properties.put(TestParameters.listeners.name(), listeners);
		} else {
			properties.put(TestParameters.listeners.name(), "");
		}
		if (excludedGroups != null) {
			properties.put(TestParameters.excludedGroups.name(), excludedGroups);
		} else {
			properties.put(TestParameters.excludedGroups.name(), "");
		}
		if (groups != null) {
			properties.put(TestParameters.groups.name(), groups);
		} else {
			properties.put(TestParameters.groups.name(), "");
		}
		properties.put(TestParameters.isJUnit.name(), isJUnit);
		properties.put(TestParameters.parallel.name(), parallel);
		properties.put(TestParameters.randomizeSuites.name(), randomizeSuites);
		if (suiteXmlFiles != null) {
			properties.put(TestParameters.testSuites.name(), suiteXmlFiles);
		} else {
			properties.put(TestParameters.testSuites.name(), "");
		}
		properties.put(TestParameters.failFast.name(), failFast);
		properties.put(TestParameters.failOnErrors.name(), failOnErrors);
		properties.put(TestParameters.retryFailures.name(), retryFailures);
		properties.put(TestParameters.showPassedConfigurations.name(), showPassedConfigurations);
		properties.put(TestParameters.knownDefectsMode.name(), knownDefectsMode);
		properties.put(TestParameters.title.name(), title);
		properties.put(TestParameters.testTimeout.name(), testTimeout);
		properties.put(TestParameters.testRetry.name(), testRetry);
		properties.put(TestParameters.systemProperties.name(), systemProperties);
		
		if (getLog().isDebugEnabled()) {
			getLog().debug(properties.toString());
		}
	}
	
	public void execute() throws MojoExecutionException {
	}
	
}
