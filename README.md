# maven-testng-starter-plugin

A mvn plugin to start testNG suites with all testNG/reportNG related attributes

Command line Usage : mvn clean install testng-starter:test

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
|reportNGhtmlReportTitle       | ReportNG    |ReportNG html report title. See for more [reportNG](https://github.com/sdrss/reportNG)|
|reportNGOutputDirectory       | html        |ReportNG html report output path.|
|retryFailures                 | 0           |Define the max number of retries for a test. In case of 0 then retry Listener is not invoked.|
|showPassedConfigurations      | true        |Show passed configuration into [reportNG](https://github.com/sdrss/reportNG) html report. Possible values true,false.|
|threadPoolSize                | 1           |Define the number of threads in the thread pool.|
|suiteThreadPoolSize           | 1           |               |
|suiteXmlFiles                 |             |Set the suites file names to be run ,separated by a comma.|
|suiteXmlFilesPostBuild        |             |Set the suites file names to be run after running suiteXmlFiles. This can be used for cleanup or even code coverage actions.|
|systemProperties              |             |Set System properties as key:value Separated by a comma. For example : DsystemProperties=serverXML:myserver.xml |
