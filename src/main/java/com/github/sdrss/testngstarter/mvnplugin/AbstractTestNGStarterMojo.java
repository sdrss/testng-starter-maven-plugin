package com.github.sdrss.testngstarter.mvnplugin;

import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.sdrss.testngstarter.mvnplugin.helper.TestParameters;

public class AbstractTestNGStarterMojo extends AbstractMojo {
	
	@Parameter(property = "threadPoolSize")
	private Integer threadPoolSize;
	
	@Parameter(property = "suiteThreadPoolSize")
	private Integer suiteThreadPoolSize;
	
	@Parameter(property = "dataProviderThreadCount")
	private Integer dataProviderThreadCount;
	
	@Parameter(property = "outputDirectory", defaultValue = "test-output")
	private String outputDirectory;
	
	@Parameter(property = "preserveOrder", defaultValue = "true")
	private Boolean preserveOrder;
	
	@Parameter(property = "configFailurePolicy", defaultValue = "skip")
	private String configFailurePolicy;
	
	@Parameter(property = "generateHtmlReport", defaultValue = "false")
	private Boolean generateHtmlReport;
	
	@Parameter(property = "generateXMLReport", defaultValue = "true")
	private Boolean generateXMLReport;
	
	@Parameter(property = "generateJunitReport", defaultValue = "false")
	private Boolean generateJunitReport;
	
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
	
	@Parameter(property = "parallel")
	private String parallel;
	
	@Parameter(property = "randomizeSuites", defaultValue = "false")
	private Boolean randomizeSuites;
	
	@Parameter(property = "toggleFailureIfAllTestsWereSkipped")
	private Boolean toggleFailureIfAllTestsWereSkipped;
	
	@Parameter(property = "generateReportNGhtmlReport", defaultValue = "true")
	private Boolean generateReportNGhtmlReport;
	
	@Parameter(property = "reportNGOutputDirectory", defaultValue = "html")
	private String reportNGOutputDirectory;
	
	@Parameter(property = "executeTestngFailedxml", defaultValue = "false")
	private Boolean executeTestngFailedxml;
	
	@Parameter(property = "failFast", defaultValue = "false")
	private Boolean failFast;
	
	@Parameter(property = "failOnErrors", defaultValue = "false")
	private Boolean failOnErrors;
	
	@Parameter(property = "showPassedConfigurations", defaultValue = "true")
	private Boolean showPassedConfigurations;
	
	@Parameter(property = "handleKnownDefectsAsFailures", defaultValue = "false")
	private Boolean handleKnownDefectsAsFailures;
	
	@Parameter(property = "logOutputReport", defaultValue = "false")
	private Boolean logOutputReport;
	
	@Parameter(property = "reportNGhtmlReportTitle", defaultValue = "")
	private String reportNGhtmlReportTitle;
	
	@Parameter(property = "globalTestTimeOut", defaultValue = "0")
	private Long globalTestTimeOut;
	
	@Parameter(property = "maxTestRetryFailures", defaultValue = "0")
	private Integer maxTestRetryFailures;
	
	@Parameter(property = "systemProperties")
	private List<String> systemProperties;
	
	@Parameter(property = "suitesSearchDirectory", defaultValue = "")
	private String suitesSearchDirectory;
	
	public Properties initProperties() {
		Properties properties = new Properties();
		if (threadPoolSize != null) {
			properties.put(TestParameters.threadPoolSize.name(), threadPoolSize);
		}
		if (suiteThreadPoolSize != null) {
			properties.put(TestParameters.suiteThreadPoolSize.name(), suiteThreadPoolSize);
		}
		if (dataProviderThreadCount != null) {
			properties.put(TestParameters.dataProviderThreadCount.name(), dataProviderThreadCount);
		}
		if (outputDirectory != null) {
			properties.put(TestParameters.outputDirectory.name(), outputDirectory);
		}
		if (preserveOrder != null) {
			properties.put(TestParameters.preserveOrder.name(), preserveOrder);
		}
		if (configFailurePolicy != null) {
			properties.put(TestParameters.configFailurePolicy.name(), configFailurePolicy);
		}
		if (generateHtmlReport != null) {
			properties.put(TestParameters.generateHtmlReport.name(), generateHtmlReport);
		}
		if (generateXMLReport != null) {
			properties.put(TestParameters.generateXMLReport.name(), generateXMLReport);
		}
		if (generateJunitReport != null) {
			properties.put(TestParameters.generateJunitReport.name(), generateJunitReport);
		}
		if (generateReportNGhtmlReport != null) {
			properties.put(TestParameters.generateReportNGhtmlReport.name(), generateReportNGhtmlReport);
		}
		if (logOutputReport != null) {
			properties.put(TestParameters.logOutputReport.name(), logOutputReport);
		}
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
		if (isJUnit != null) {
			properties.put(TestParameters.isJUnit.name(), isJUnit);
		}
		if (parallel != null) {
			properties.put(TestParameters.parallel.name(), parallel);
		}
		if (randomizeSuites != null) {
			properties.put(TestParameters.randomizeSuites.name(), randomizeSuites);
		}
		if (toggleFailureIfAllTestsWereSkipped != null) {
			properties.put(TestParameters.toggleFailureIfAllTestsWereSkipped.name(), toggleFailureIfAllTestsWereSkipped);
		}
		if (suiteXmlFiles != null) {
			properties.put(TestParameters.suiteXmlFiles.name(), suiteXmlFiles);
		} else {
			properties.put(TestParameters.suiteXmlFiles.name(), "");
		}
		if (reportNGOutputDirectory != null) {
			properties.put(TestParameters.reportNGOutputDirectory.name(), reportNGOutputDirectory);
		}
		if (failFast != null) {
			properties.put(TestParameters.failFast.name(), failFast);
		}
		if (failOnErrors != null) {
			properties.put(TestParameters.failOnErrors.name(), failOnErrors);
		}
		if (executeTestngFailedxml != null) {
			properties.put(TestParameters.executeTestngFailedxml.name(), executeTestngFailedxml);
		}
		if (showPassedConfigurations != null) {
			properties.put(TestParameters.showPassedConfigurations.name(), showPassedConfigurations);
		}
		if (handleKnownDefectsAsFailures != null) {
			properties.put(TestParameters.handleKnownDefectsAsFailures.name(), handleKnownDefectsAsFailures);
		}
		if (reportNGhtmlReportTitle != null) {
			properties.put(TestParameters.reportNGhtmlReportTitle.name(), reportNGhtmlReportTitle);
		} else {
			properties.put(TestParameters.reportNGhtmlReportTitle.name(), "");
		}
		if (globalTestTimeOut != null) {
			properties.put(TestParameters.globalTestTimeOut.name(), globalTestTimeOut);
		}
		if (maxTestRetryFailures != null) {
			properties.put(TestParameters.maxTestRetryFailures.name(), maxTestRetryFailures);
		}
		if (systemProperties != null) {
			properties.put(TestParameters.systemProperties.name(), systemProperties);
		}
		if (suitesSearchDirectory != null) {
			properties.put(TestParameters.suitesSearchDirectory.name(), suitesSearchDirectory);
		} else {
			properties.put(TestParameters.suitesSearchDirectory.name(), "");
		}
		if (getLog().isDebugEnabled()) {
			getLog().debug(properties.toString());
		}
		return properties;
	}
	
	public void execute() throws MojoExecutionException {
	}
	
}
