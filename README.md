# maven-testng-starter-plugin

A mvn plugin to start testNG suites with all testNG/reportNG related attributes

Command line Usage : mvn clean install testng-starter:test

Params

| Attribute               | Description                                               | Mandatory | Default Value |
|-------------------------|-----------------------------------------------------------|-----------|---------------|
| threadPoolSize          | Define the number of threads in the thread pool           | false     |               |
| suiteThreadPoolSize     | Define the number of threads in suite thread pool         | false     |               |
| dataProviderThreadCount | Define the number of threads in data provider thread pool | false     |               |
| outputDirectory         | Sets the output directory where the testNG reports will be created |false| "test-output"|
| preserveOrder           |                                                           |           |               |
| configFailurePolicy     | Sets the policy for whether or not to ever invoke a configuration method again after it has failed once. Possible values are SKIP, CONTINUE| false | |
| htmlReport              | Generate the default testNG html report                   | false | |
| xmlReport               | Generate the default JUnit XML report                     |       | |
| junitReport             | Specify if this run should be made in JUnit mode          |       | |
| excludedGroups          | Define which groups will be excluded from this run. Comman separated list.| | |
| groups                  | Define which groups will be included from this run. Comman separated list.| | |
| suiteXmlFiles           |                                                                           | | |
| suiteXmlFilesPostBuild           |                                                                           | | |
| listeners           |                                                                           | | |
| isJUnit           |                                                                           | | |
| parallel           |                                                                           | | |
| randomizeSuites           |                                                                           | | |
| reportNGListener           |                                                                           | | |
| reportNGOutputDirectory           |                                                                           | | |
| retryFailures           |                                                                           | | |
| failFast           |                                                                           | | |
| failOnErrors           |                                                                           | | |
| showPassedConfigurations           |                                                                           | | |
| knownDefectsMode           |                                                                           | | |
| logOutputReport           |                                                                           | | |
| title           |                                                                           | | |
| testTimeout           |                                                                           | | |
| testRetry           |                                                                           | | |
| systemProperties           |                                                                           | | |
