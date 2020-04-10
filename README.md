# testng-starter-maven-plugin

A maven plugin to start testNG suites with all [testNG](https://testng.org/doc/)/[reportNG](https://github.com/sdrss/reportNG) related attributes.

[![Build](https://github.com/sdrss/maven-testng-starter-plugin/workflows/Java_CI/badge.svg)](https://github.com/sdrss/maven-testng-starter-plugin/workflows/Java_CI/badge.svg)

<!--
![GitHub Release Date](https://img.shields.io/github/release-date/sdrss/maven-testng-starter-plugin) ![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/sdrss/maven-testng-starter-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.sdrss/maven-testng-starter-plugin?style=blue)](https://img.shields.io/maven-central/v/com.github.sdrss/maven-testng-starter-plugin)
-->

 **Pom.xml** plugin dependency example :
```
<pluginManagement>
	<plugins>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>testng-starter-maven-plugin</artifactId>
			<version>0.0.1-SNAPSHOT</version>
			<executions>
				<execution>
				<goals>
					<goal>test</goal>
				</goals>
				</execution>
				<configuration>
				<!-- TestNG Starter mvn plugin Configuration-->
				<suiteXmlFiles>src/test/resources/Suites/Sample.xml</suiteXmlFiles>
				</configuration>
			</executions>
         	</plugin>
        	...
  	</plugins>
<pluginManagement> 
 ```
 
 
**Command line usage** : mvn clean install testng-starter:test


** ** 
| Attribute | Default Value | Description |
|-----------|-------------|---------------|
|configFailurePolicy           | NONE        |Sets the policy for whether or not to ever invoke a configuration method again after it has failed once. Possible values are SKIP,CONTINUE|
|dataProviderThreadCount       |  1          |               |
|excludedGroups                |             |Define which groups will be excluded from this run. Separated by a comma.|
|failFast                      | false       |Fail execution upon first test failure. The rest of the execution will be skipped. Possible values true,false. See for more [reportNG](https://github.com/sdrss/reportNG)|
|failOnErrors                  | false       |System.exit -1 in case of test failures. Possible values true,false.|
|generateHtmlReport            | false       |Generate default testNG html report. Possible values true,false.|
|generateJunitReport           | false       |Generate default Junit report. Possible values true,false.|
|generateReportNGhtmlReport    | true        |Generate [reportNG](https://github.com/sdrss/reportNG) html report. Possible values true,false.|
|generateXMLReport             | true        |Generate default testNG xml report. Possible values true,false.|
|globalTestTimeOut             | 0           |Test time out in milliseconds. In case of 0 then test timeout listener is not invoked.|
|groups                        |             |Define which groups will be included from this run. Separated by a comma.|
|handleKnownDefectsAsFailures  | false       |@Tests marked with @KnownDefect will fail or not accordingly. Possible values true,false. See for more [reportNG](https://github.com/sdrss/reportNG)|
|isJUnit                       | false       |Specify if this run should be made in JUnit mode. Possible values true,false.|
|listeners                     |             |Custom Listeners invoked into testNG run.Separated by a comma.|
|logOutputReport               | false       |Generate a log output report. Possible values true,false.|
|outputDirectory               | test-output |Sets the output directory where the reports will be created.|
|parallel                      | NONE        |Parallel execution. Possible values are CLASSES,INSTANCES,METHODS,TEST.|
|preserveOrder                 | true        |Possible values true,false.|
|randomizeSuites               | false       |Possible values true,false.|
|reportNGhtmlReportTitle       |             |ReportNG html report title. See for more [reportNG](https://github.com/sdrss/reportNG)|
|reportNGOutputDirectory       | html        |ReportNG html report output path. Default {testNG_default_output_directory}/reportNG|
|maxTestRetryFailures          | 0           |Define the max number of retries for a test. In case of 0 then retry Listener is not invoked.|
|executeTestngFailedxml        | false       |Generate and run testng-failed.xml. Possible values true,false.|
|showPassedConfigurations      | true        |Show passed configuration into [reportNG](https://github.com/sdrss/reportNG) html report. Possible values true,false.|
|threadPoolSize                | 1           |Define the number of threads in the thread pool.|
|suiteThreadPoolSize           | 1           |               |
|suiteXmlFiles                 |             |Set the suites file names to be run ,separated by a comma.|
|suiteXmlFilesPostBuild        |             |Set the suites file names to be run after running suiteXmlFiles. This can be used for cleanup or even code coverage actions.|
|systemProperties              |             |Set System properties as key:value Separated by a comma. For example : DsystemProperties=serverXML:myserver.xml |
