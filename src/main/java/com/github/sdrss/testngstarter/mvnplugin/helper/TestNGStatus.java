package com.github.sdrss.testngstarter.mvnplugin.helper;

import java.text.MessageFormat;

public enum TestNGStatus {
	PASS ("0"),
	FAIL ("1"),
	SKIP ("2"),
	SKIP_FAIL ("3"),
	FAIL_WITHIN_SUCCESS ("4"),
	FAIL_WITHIN_SUCCESS_FAILED ("5"),
	FAIL_WITHIN_SUCCESS_SKIPPED ("6"),
	FAIL_WITHIN_SUCCESS_SKIPPED_FAILED ("7");
	
	private String myLocator;
	
	TestNGStatus(String locator) {
		myLocator = locator;
	}
	
	public static String TestNGStatusGet(int locator) {
		switch (locator) {
			case 0:
				return TestNGStatus.PASS.name();
			case 1:
				return TestNGStatus.FAIL.name();
			case 2:
				return TestNGStatus.SKIP.name();
			case 3:
				return TestNGStatus.SKIP_FAIL.name();
			case 4:
				return TestNGStatus.FAIL_WITHIN_SUCCESS.name();
			case 5:
				return TestNGStatus.FAIL_WITHIN_SUCCESS_FAILED.name();
			case 6:
				return TestNGStatus.FAIL_WITHIN_SUCCESS_SKIPPED.name();
			case 7:
				return TestNGStatus.FAIL_WITHIN_SUCCESS_SKIPPED_FAILED.name();
			default:
				return "";
		}
	}
	
	public int get() {
		return Integer.parseInt(myLocator);
	}
	
	public String getWithParams(Object... params) {
		return MessageFormat.format(myLocator, params);
	}
}
