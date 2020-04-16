package com.github.sdrss.testngstarter.mvnplugin.helper.exceptions;

public class TestNGSuiteNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public TestNGSuiteNotFoundException() {
		
	}

    public TestNGSuiteNotFoundException(String message){
       super(message);
    }
    
    public TestNGSuiteNotFoundException(String message,Exception ex){
        super(message,ex);
     }
    
    public TestNGSuiteNotFoundException(Exception ex){
        super(ex);
     }
}