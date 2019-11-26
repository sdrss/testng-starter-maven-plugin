package com.github.sdrss.testngstarter.mvnplugin.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;
import org.uncommons.reportng.HTMLReporter;

public class Execution {
	
	private static final int ABORT = -1;
	private static final int NORMAL = 0;
	private static final Logger LOGGER = LoggerFactory.getLogger(Execution.class);
	
	private Execution() {
		
	}
	
	public static void abort() {
		systemExit(ABORT);
	}
	
	public static void skip() throws SkipException {
		System.setProperty(HTMLReporter.SKIP_EXECUTION, "true");
		LOGGER.error("Skip Tests Mode is Enabled. <Skip>");
		throw new SkipException("Skipped because property [" + HTMLReporter.SKIP_EXECUTION + "=" + true + "]");
	}
	
	public static void normal() {
		systemExit(NORMAL);
	}
	
	private static void systemExit(int status) {
		System.exit(status);
	}
}
