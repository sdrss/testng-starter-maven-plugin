# testng-starter-maven-plugin

A maven plugin to start testNG suites with all [testNG](https://testng.org/doc/)/[reportNG](https://github.com/sdrss/reportNG) configuration attributes.

![GitHub Release Date](https://img.shields.io/github/release-date/sdrss/testng-starter-maven-plugin) ![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/sdrss/testng-starter-maven-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.sdrss/testng-starter-maven-plugin?style=blue)](https://img.shields.io/maven-central/v/com.github.sdrss/testng-starter-maven-plugin) 
[![Build](https://github.com/sdrss/maven-testng-starter-plugin/workflows/Java_CI/badge.svg)](https://github.com/sdrss/maven-testng-starter-plugin/workflows/Java_CI/badge.svg)

Check Maven Repository releases [here](https://mvnrepository.com/artifact/com.github.sdrss/testng-starter-maven-plugin)

** **
 **Pom.xml** plugin dependency example :
```
<pluginManagement>
	<plugins>
		<plugin>
			<groupId>com.github.sdrss</groupId>
			<artifactId>testng-starter-maven-plugin</artifactId>
			<version>1.4.0</version>
			<executions>
				<execution>
				<goals>
					<goal>test</goal>
				</goals>
				</execution>
				<configuration>
				        <!-- TestNG Starter mvn plugin Configuration-->
				        <suiteXmlFiles>src/test/resources/Regression.xml</suiteXmlFiles>
				</configuration>
			</executions>
         	</plugin>
        	...
  	</plugins>
<pluginManagement> 
 ```
** ** 
 
**Command line usage samples** : 
 * mvn testng-starter:test
 * mvn testng-starter:test -DsystemProperties=param1:value1 -DsuiteXmlFiles=src/test/resources/MySuite1.xml,src/test/resources/MySuite2.xml -DfailFast=true -DreportNGOutputDirectory=html -DmaxTestRetryFailures=2 -DexecuteTestngFailedxml=true -DhandleKnownDefectsAsFailures=false -Dlisteners=com.mypackage.TestListener,com.mypackage.SuiteListener
 * mvn testng-starter:test -DsystemProperties=param1:value1 -DsuiteXmlFiles=MySuite1.xml,MySuite1.xml -DsuitesSearchDirectory=src/test/resources/ -DfailFast=true -DreportNGOutputDirectory=html -DmaxTestRetryFailures=2 -DexecuteTestngFailedxml=true -DhandleKnownDefectsAsFailures=false -Dlisteners=com.mypackage.TestListener,com.mypackage.SuiteListener

** ** 
| Attribute | Default Value | Description |
|-----------|-------------|---------------|
|configFailurePolicy           | NONE        |Sets the policy for whether or not to ever invoke a configuration method again after it has failed once. Possible values are SKIP,CONTINUE. See for more [testNG](https://testng.org/doc)|
|dataProviderThreadCount       |             |See for more [testNG](https://testng.org/doc)|
|excludedGroups                |             |Define which groups will be excluded from this run. Separated by a comma. See for more [testNG](https://testng.org/doc)|
|failFast                      | false       |Fail execution upon first test failure. The rest of the execution will be skipped. Possible values true,false. See for more [reportNG](https://github.com/sdrss/reportNG)|
|failOnErrors                  | false       |System.exit -1 in case of test failures. Possible values true,false. See for more [reportNG](https://github.com/sdrss/reportNG)|
|generateHtmlReport            | false       |Generate default testNG html report. Possible values true,false. See for more [testNG](https://testng.org/doc)|
|generateJunitReport           | false       |Generate default Junit report. Possible values true,false. See for more [testNG](https://testng.org/doc)|
|generateReportNGhtmlReport    | true        |Generate [reportNG](https://github.com/sdrss/reportNG) html report. Possible values true,false.|
|generateXMLReport             | true        |Generate default testNG xml report. Possible values true,false.  See for more [testNG](https://testng.org/doc)|
|globalTestTimeOut             | 0           |Test time out in milliseconds. In case of 0 then test timeout listener is not invoked. See for more [reportNG](https://github.com/sdrss/reportNG)|
|groups                        |             |Define which groups will be included from this run. Separated by a comma. See for more [testNG](https://testng.org/doc)|
|handleKnownDefectsAsFailures  | false       |@Tests marked with @KnownDefect will fail or not accordingly. Possible values true,false. See for more [reportNG](https://github.com/sdrss/reportNG)|
|isJUnit                       | false       |Specify if this run should be made in JUnit mode. Possible values true,false. See for more [testNG](https://testng.org/doc)|
|listeners                     |             |Custom Listeners invoked into testNG run.Separated by a comma. See for more [testNG](https://testng.org/doc)|
|logOutputReport               | false       |Generate a log output report. Possible values true,false. See for more [reportNG](https://github.com/sdrss/reportNG)|
|outputDirectory               | test-output |Sets the output directory where the reports will be created.See for more [reportNG](https://github.com/sdrss/reportNG)|
|parallel                      | NONE        |Parallel execution. Possible values are CLASSES,INSTANCES,METHODS,TESTS. See for more [testNG](https://testng.org/doc)|
|preserveOrder                 | true        |Possible values true,false. See for more [testNG](https://testng.org/doc)|
|randomizeSuites               | false       |Possible values true,false. See for more [testNG](https://testng.org/doc)|
|reportNGhtmlReportTitle       |             |ReportNG html report title. See for more [reportNG](https://github.com/sdrss/reportNG)|
|reportNGOutputDirectory       | html        |ReportNG html report output path. Default {testNG_default_output_directory}/reportNG|
|maxTestRetryFailures               | 0           |Define the max number of retries for a test. In case of 0 then retry Listener is not invoked. See for more [reportNG](https://github.com/sdrss/reportNG)|
|executeTestngFailedxml             | false       |Generate and run testng-failed.xml. Possible values true,false. See for more [testNG](https://testng.org/doc)|
|showPassedConfigurations           | true        |Show passed configuration into [reportNG](https://github.com/sdrss/reportNG) html report. Possible values true,false.|
|threadPoolSize                     |             |Define the number of threads in the thread pool. See for more [testNG](https://testng.org/doc)|
|toggleFailureIfAllTestsWereSkipped |             | Whether TestNG should enable/disable failing when all the tests were skipped and nothing was run (Mostly when a test is powered by a data provider and when the data provider itself fails causing all tests to skip). See for more [testNG](https://testng.org/doc)|
|suiteThreadPoolSize                |             |See for more [testNG](https://testng.org/doc)               |
|suiteXmlFiles                      |             |Set the suites file names to be run ,separated by a comma. See for more [testNG](https://testng.org/doc)|
|suitesSearchDirectory              |             |Set a directory to search for testNG suite xml files.|
|suiteXmlFilesPostBuild             |             |Set the suites file names to be run after running suiteXmlFiles. This can be used for cleanup or even code coverage actions.|
|systemProperties                   |             |Set System properties as key:value Separated by a comma. For example : DsystemProperties=serverXML:myserver.xml |
