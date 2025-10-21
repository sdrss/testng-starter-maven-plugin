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

import com.github.sdrss.testngstarter.mvnplugin.helper.exceptions.TestNGSuiteNotFoundException;
import com.google.common.base.Strings;

public final class TestNGStarterMainClass {
	
	public static final Logger logger = LoggerFactory.getLogger(TestNGStarterMainClass.class);
	public static final String STRIPE = "===============================================";
	public static final String TESTNG_RETRY_SUITE_NAME = "testng-failed.xml";
	public static final String TESTNG_RETRY_PATH = "_RetryFailures";
	public static final String TESTNG_POST_PATH = "_Post";
	static boolean isJunit = false;
	static boolean retryFailures = false;
	static boolean useReportNG = false;
	static boolean failOnError = false;
	static String testOutputDirectory = "";
	static String reportNGOutputDirectory = "";
	
	public static void execute(Properties properties) throws TestNGSuiteNotFoundException {
		// Keep statuses
		int tngStatusMain = -1;
		int tngStatusExecuteFailures = -1;
		
		// Initialize and Run
		logger.info(STRIPE);
		logger.info("Start TestNG");
		logger.info(STRIPE);
		TestNG testng = new TestNG();
		initTestNG(testng, properties);
		if (useReportNG) {
			initReportNG(testng, properties);
		}
		setSystemProperties(properties);
		testng.run();
		// Get Status
		tngStatusMain = getTestNGStatus(testng);
		// Print Summary
		printSummary();
		
		// Execute testng-failed.xml
		if (retryFailures) {
			logger.info(STRIPE);
			logger.info("TestNG status is '" + testng.getStatus() + "' and '" + TestParameters.executeTestngFailedxml.name() +
					"' attribute is '" + Boolean.toString(retryFailures) + "'");
			logger.info("Start TestNG and execute 'testng-failed.xml' suite");
			logger.info(STRIPE);
			File f = new File(testOutputDirectory + "/" + TESTNG_RETRY_SUITE_NAME);
			if (f.exists() && !f.isDirectory()) {
				// Re Initialize and Run
				testng = new TestNG();
				Properties retryProperties = new Properties();
				retryProperties.putAll(properties);
				retryProperties.setProperty(TestParameters.suiteXmlFiles.name(), testOutputDirectory + "/" + TESTNG_RETRY_SUITE_NAME);
				retryProperties.setProperty(TestParameters.reportNGOutputDirectory.name(), reportNGOutputDirectory + TESTNG_RETRY_PATH);
				initTestNG(testng, retryProperties);
				if (useReportNG) {
					initReportNG(testng, retryProperties);
				}
				setSystemProperties(retryProperties);
				testng.run();
				tngStatusExecuteFailures = getTestNGStatus(testng);
				// Print Summary
				printSummary();
			} else {
				logger.info("Not found " + testOutputDirectory + "/" + TESTNG_RETRY_SUITE_NAME);
			}
			
		}
		// Fail on error
		if (checkFailOnError(failOnError, tngStatusMain, tngStatusExecuteFailures)) {
			logger.info("TestNG status is '" + testng.getStatus() + "' and '" + TestParameters.failOnErrors.name() +
					"' attribute is '" + Boolean.toString(failOnError) + "'");
			logger.error("Abort execution");
			Execution.abort();
		}
		Execution.normal();
	}
	
	private static boolean checkFailOnError(boolean failOnError, int tngStatusMain, int tngStatusExecuteFailures) {
		boolean abort = false;
		if (failOnError) {
			if (TestNGStatus.PASS.get() != tngStatusMain) {
				abort = true;
			}
			if (tngStatusExecuteFailures > -1 && TestNGStatus.PASS.get() != tngStatusExecuteFailures) {
				abort = false;
			}
		}
		return abort;
	}
	
	private static int getTestNGStatus(TestNG testng) {
		int status = testng.getStatus();
		logger.info("TestNG status is : '" + TestNGStatus.TestNGStatusGet(status) + "'");
		return status;
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
	
	private static void initReportNG(TestNG testNG, Properties properties) {
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
				if ((boolean) properties.get(TestParameters.showPassedConfigurations.name())) {
					System.setProperty(HTMLReporter.SHOW_PASSED_CONFIGURATIONS, Boolean.TRUE.toString());
				} else {
					System.setProperty(HTMLReporter.SHOW_PASSED_CONFIGURATIONS, Boolean.FALSE.toString());
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.showPassedConfigurations.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.handleKnownDefectsAsFailures.name()) != null) {
			try {
				if (!(boolean) properties.get(TestParameters.handleKnownDefectsAsFailures.name())) {
					System.setProperty(HTMLReporter.KWOWNDEFECTSMODE, Boolean.TRUE.toString());
				} else {
					System.setProperty(HTMLReporter.KWOWNDEFECTSMODE, Boolean.FALSE.toString());
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.handleKnownDefectsAsFailures.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.escapeOutput.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.escapeOutput.name())) {
					System.setProperty(HTMLReporter.ESCAPE_OUTPUT, Boolean.TRUE.toString());
				} else {
					System.setProperty(HTMLReporter.ESCAPE_OUTPUT, Boolean.FALSE.toString());
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.escapeOutput.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.logOutputReport.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.logOutputReport.name())) {
					System.setProperty(HTMLReporter.LOG_OUTPUT_REPORT, Boolean.TRUE.toString());
				} else {
					System.setProperty(HTMLReporter.LOG_OUTPUT_REPORT, Boolean.FALSE.toString());
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
		
		if (properties.get(TestParameters.reportNGhtmlReportTitle.name()) != null) {
			try {
				String title = (String) properties.get(TestParameters.reportNGhtmlReportTitle.name());
				if (!Strings.isNullOrEmpty(title)) {
					System.setProperty(HTMLReporter.REPORTNG_TITLE, title);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.failFast.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.failOnErrors.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.failOnErrors.name())) {
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
	private static void initTestNG(TestNG testNG, Properties properties) throws TestNGSuiteNotFoundException {
		List<Class<? extends ITestNGListener>> listenerClasses = new ArrayList<Class<? extends ITestNGListener>>();
		
		if (properties.get(TestParameters.threadPoolSize.name()) != null) {
			try {
				testNG.setThreadCount((Integer) (properties.get(TestParameters.threadPoolSize.name())));
			} catch (Exception ex) {
				logger.debug(TestParameters.threadPoolSize.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.suiteThreadPoolSize.name()) != null) {
			try {
				testNG.setSuiteThreadPoolSize((Integer) (properties.get(TestParameters.suiteThreadPoolSize.name())));
			} catch (Exception ex) {
				logger.debug(TestParameters.suiteThreadPoolSize.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.dataProviderThreadCount.name()) != null) {
			try {
				testNG.setDataProviderThreadCount((Integer) (properties.get(TestParameters.dataProviderThreadCount.name())));
			} catch (Exception ex) {
				logger.debug(TestParameters.dataProviderThreadCount.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.outputDirectory.name()) != null) {
			try {
				testNG.setOutputDirectory((String) properties.get(TestParameters.outputDirectory.name()));
			} catch (Exception ex) {
				logger.debug(TestParameters.outputDirectory.name(), ex);
				logger.debug("Set Default " + TestNG.DEFAULT_OUTPUTDIR);
				testNG.setOutputDirectory(TestNG.DEFAULT_OUTPUTDIR);
			}
		} else {
			testNG.setOutputDirectory(TestNG.DEFAULT_OUTPUTDIR);
		}
		testOutputDirectory = testNG.getOutputDirectory();
		
		if (properties.get(TestParameters.preserveOrder.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.preserveOrder.name())) {
					testNG.setPreserveOrder(true);
				} else {
					testNG.setPreserveOrder(false);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.preserveOrder.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.isJUnit.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.isJUnit.name())) {
					testNG.setJUnit(true);
					isJunit = true;
				} else {
					testNG.setJUnit(false);
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
						testNG.setConfigFailurePolicy(FailurePolicy.SKIP);
					} else if (configFailurePolicy.equalsIgnoreCase(FailurePolicy.CONTINUE.name())) {
						testNG.setConfigFailurePolicy(FailurePolicy.CONTINUE);
					} else {
						logger.debug("Do nothing for " + TestParameters.configFailurePolicy.name());
					}
				}
				
			} catch (Exception ex) {
				logger.debug(TestParameters.configFailurePolicy.name(), ex);
			}
		}
		// Disable All Listeners and enable one by one according to params
		testNG.setUseDefaultListeners(false);
		// HTML Listener
		if (properties.get(TestParameters.generateHtmlReport.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.generateHtmlReport.name())) {
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
				if ((boolean) properties.get(TestParameters.generateXMLReport.name())) {
					if (isJunit) {
						listenerClasses.add(org.testng.reporters.JUnitXMLReporter.class);
					} else {
						listenerClasses.add(org.testng.reporters.XMLReporter.class);
					}
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.generateXMLReport.name(), ex);
			}
		}
		// Junit Listener
		if (properties.get(TestParameters.generateJunitReport.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.generateJunitReport.name())) {
					listenerClasses.add(org.testng.reporters.JUnitReportReporter.class);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.generateJunitReport.name(), ex);
			}
		}
		// ReportNG main Listener
		if (properties.get(TestParameters.generateReportNGhtmlReport.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.generateReportNGhtmlReport.name())) {
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
				if ((boolean) properties.get(TestParameters.failFast.name())) {
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
				if ((boolean) properties.get(TestParameters.executeTestngFailedxml.name())) {
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
					testNG.setExcludedGroups(excludedGroupsCommaSeparated);
				} else {
					testNG.setExcludedGroups(null);
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.excludedGroups.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.groups.name()) != null) {
			try {
				String groupsCommaSeparated = (String) properties.get(TestParameters.groups.name());
				if (!groupsCommaSeparated.isEmpty()) {
					testNG.setGroups(groupsCommaSeparated);
				} else {
					testNG.setGroups(null);
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
						testNG.setParallel(ParallelMode.CLASSES);
					} else if (parallel.equalsIgnoreCase(ParallelMode.INSTANCES.name())) {
						testNG.setParallel(ParallelMode.INSTANCES);
					} else if (parallel.equalsIgnoreCase(ParallelMode.METHODS.name())) {
						testNG.setParallel(ParallelMode.METHODS);
					} else if (parallel.equalsIgnoreCase(ParallelMode.NONE.name())) {
						testNG.setParallel(ParallelMode.NONE);
					} else if (parallel.equalsIgnoreCase(ParallelMode.TESTS.name())) {
						testNG.setParallel(ParallelMode.TESTS);
					}
				}
			} catch (Exception ex) {
				logger.debug(TestParameters.parallel.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.randomizeSuites.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.randomizeSuites.name())) {
					testNG.setRandomizeSuites(true);
				} else {
					testNG.setRandomizeSuites(false);
				}
				
			} catch (Exception ex) {
				logger.debug(TestParameters.randomizeSuites.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.toggleFailureIfAllTestsWereSkipped.name()) != null) {
			try {
				if ((boolean) properties.get(TestParameters.toggleFailureIfAllTestsWereSkipped.name())) {
					testNG.toggleFailureIfAllTestsWereSkipped(true);
				} else {
					testNG.toggleFailureIfAllTestsWereSkipped(false);
				}
				
			} catch (Exception ex) {
				logger.debug(TestParameters.toggleFailureIfAllTestsWereSkipped.name(), ex);
			}
		}
		
		if (properties.get(TestParameters.suiteXmlFiles.name()) == null) {
			throw new IllegalStateException("No suite files were specified");
		} else {
			List<String> testSuites = new ArrayList<>();
			try {
				String testSuitesCommaSeparated = (String) properties.get(TestParameters.suiteXmlFiles.name());
				testSuites = Arrays.asList(testSuitesCommaSeparated.split(","));
			} catch (Exception ex) {
				logger.debug(TestParameters.suiteXmlFiles.name(), ex);
			}
			if (!testSuites.isEmpty()) {
				List<String> testSuitesTrimmed = new ArrayList<>(testSuites.size());
				// Default Search path is current directory
				String path = "./";
				boolean useCustomDirectory = false;
				if (properties.get(TestParameters.suitesSearchDirectory.name()) != null && !"".equals(properties.get(TestParameters.suitesSearchDirectory.name()))) {
					path = properties.get(TestParameters.suitesSearchDirectory.name()).toString();
					// If a custom suite search path 'normalize' it
					if (!path.endsWith("/")) {
						path = path.concat("/");
					}
					if (!path.startsWith("/") && !path.startsWith(".")) {
						path = "/".concat(path);
					}
					useCustomDirectory = true;
				}
				for (String tempSuite : testSuites) {
					File suitePath;
					String suitePathName;
					tempSuite = tempSuite.trim();
					if (tempSuite.startsWith("/")) {
						tempSuite = tempSuite.replaceFirst("//", "");
					}
					if (useCustomDirectory) {
						suitePath = FindSuites.find(tempSuite, path);
						if (suitePath == null) {
							throw new TestNGSuiteNotFoundException("Suite file " + tempSuite + " not found");
						}
						tempSuite = suitePath.getAbsolutePath();
					} else {
						suitePathName = path.concat(tempSuite);
						suitePath = new java.io.File(suitePathName);
					}
					if (!suitePath.isFile()) {
						throw new TestNGSuiteNotFoundException("Suite file " + tempSuite + " is not a valid file");
					}
					testSuitesTrimmed.add(tempSuite.trim());
				}
				testNG.setTestSuites(testSuitesTrimmed);
			} else {
				throw new TestNGSuiteNotFoundException("No suite files were specified");
			}
		}
		
		// Set Listeners
		if (!listenerClasses.isEmpty()) {
			logger.info("Add Listeners");
			// Set and Print out
			testNG.setListenerClasses(listenerClasses);
			for (Class<? extends ITestNGListener> temp : listenerClasses) {
				logger.info("Listener : " + temp.getCanonicalName());
			}
			logger.info(STRIPE);
		}
	}
	
	private static void printSummary() {
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
		String fixed = System.getProperty(TestNGSemantics.FIXED);
		if (fixed == null || "null".equalsIgnoreCase(fixed)) {
			fixed = "0";
		}
		// Print Title
		logger.info("Title : " + System.getProperty(HTMLReporter.REPORTNG_TITLE));
		if (useReportNG) {
			logger.info("Total Passed: " + passed + " Failures: " + failed + " Skips: " + skipped + " KnownDefects: " + knownDefect + " Fixed: " + fixed);
		} else {
			logger.info("Total Passed: " + passed + " Failures: " + failed + " Skips: " + skipped);
		}
		logger.info(STRIPE);
	}
	
}
