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
	
	@Parameter(property = "suiteXmlFilesPostBuild", required = false)
	private String suiteXmlFilesPostBuild;
	
	@Parameter(property = "listeners", defaultValue = "")
	private String listeners;
	
	@Parameter(property = "isJUnit", defaultValue = "false")
	private Boolean isJUnit;
	
	@Parameter(property = "parallel", defaultValue = "false")
	private String parallel;
	
	@Parameter(property = "randomizeSuites", defaultValue = "false")
	private Boolean randomizeSuites;
	
	@Parameter(property = "generateReportNGhtmlReport", defaultValue = "true")
	private Boolean generateReportNGhtmlReport;
	
	@Parameter(property = "reportNGOutputDirectory", defaultValue = "html")
	private String reportNGOutputDirectory;
	
	@Parameter(property = "retryTestFailures", defaultValue = "false")
	private Boolean retryTestFailures;
	
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
	
	@Parameter(property = "reportNGhtmlReportTitle", defaultValue = "ReportNG")
	private String reportNGhtmlReportTitle;
	
	@Parameter(property = "globalTestTimeOut", defaultValue = "0")
	private Long globalTestTimeOut;
	
	@Parameter(property = "retryFailures", defaultValue = "0")
	private Integer retryFailures;
	
	@Parameter(property = "systemProperties")
	private List<String> systemProperties;
	
	public void initProperties(Properties properties) {
		properties.put(TestParameters.threadPoolSize.name(), threadPoolSize);
		properties.put(TestParameters.suiteThreadPoolSize.name(), suiteThreadPoolSize);
		properties.put(TestParameters.dataProviderThreadCount.name(), dataProviderThreadCount);
		properties.put(TestParameters.outputDirectory.name(), outputDirectory);
		properties.put(TestParameters.preserveOrder.name(), preserveOrder);
		properties.put(TestParameters.configFailurePolicy.name(), configFailurePolicy);
		properties.put(TestParameters.generateHtmlReport.name(), generateHtmlReport);
		properties.put(TestParameters.generateXMLReport.name(), generateXMLReport);
		properties.put(TestParameters.generateJunitReport.name(), generateJunitReport);
		properties.put(TestParameters.generateReportNGhtmlReport.name(), generateReportNGhtmlReport);
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
			properties.put(TestParameters.suiteXmlFiles.name(), suiteXmlFiles);
		} else {
			properties.put(TestParameters.suiteXmlFiles.name(), "");
		}
		if (suiteXmlFilesPostBuild != null) {
			properties.put(TestParameters.suiteXmlFilesPostBuild.name(), suiteXmlFilesPostBuild);
		} else {
			properties.put(TestParameters.suiteXmlFilesPostBuild.name(), "");
		}
		properties.put(TestParameters.reportNGOutputDirectory.name(), reportNGOutputDirectory);
		properties.put(TestParameters.failFast.name(), failFast);
		properties.put(TestParameters.failOnErrors.name(), failOnErrors);
		properties.put(TestParameters.retryTestFailures.name(), retryTestFailures);
		properties.put(TestParameters.showPassedConfigurations.name(), showPassedConfigurations);
		properties.put(TestParameters.handleKnownDefectsAsFailures.name(), handleKnownDefectsAsFailures);
		properties.put(TestParameters.reportNGhtmlReportTitle.name(), reportNGhtmlReportTitle);
		properties.put(TestParameters.globalTestTimeOut.name(), globalTestTimeOut);
		properties.put(TestParameters.retryFailures.name(), retryFailures);
		properties.put(TestParameters.systemProperties.name(), systemProperties);
		
		if (getLog().isDebugEnabled()) {
			getLog().debug(properties.toString());
		}
	}
	
	public void execute() throws MojoExecutionException {
	}
	
}
