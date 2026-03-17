# testng-starter-maven-plugin

A Maven plugin to execute [TestNG](https://testng.org/doc/) test suites with full support for all TestNG and [ReportNG](https://github.com/sdrss/reportNG) configuration attributes.

![GitHub Release Date](https://img.shields.io/github/release-date/sdrss/testng-starter-maven-plugin) ![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/sdrss/testng-starter-maven-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.sdrss/testng-starter-maven-plugin?style=blue)](https://img.shields.io/maven-central/v/com.github.sdrss/testng-starter-maven-plugin)
[![Build](https://github.com/sdrss/maven-testng-starter-plugin/workflows/Java_CI/badge.svg)](https://github.com/sdrss/maven-testng-starter-plugin/workflows/Java_CI/badge.svg)

Check Maven Repository releases [here](https://mvnrepository.com/artifact/com.github.sdrss/testng-starter-maven-plugin)

---

## Overview

The **testng-starter-maven-plugin** bridges Maven's build lifecycle and the TestNG test framework by providing a single `test` goal that:

- Runs one or more TestNG suite XML files (configured or discovered automatically from a directory)
- Registers and configures [ReportNG](https://github.com/sdrss/reportNG) for rich HTML test reports
- Supports test retries, global timeouts, and fail-fast mode
- Allows parallel test execution (methods, classes, instances, tests)
- Accepts custom TestNG listeners and arbitrary system properties
- Optionally re-runs `testng-failed.xml` for automatic retry of failed tests
- Executes tests in an isolated ClassLoader built from the project's resolved dependency graph, so plugin internals never pollute the test classpath

**Relationship to ReportNG:** This plugin pairs with [ReportNG](https://github.com/sdrss/reportNG) (`com.github.sdrss:reportng`) to produce detailed HTML test reports. ReportNG is bundled as a plugin dependency — no extra configuration is required in the test project.

---

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- TestNG suite XML file(s) (e.g. `src/test/resources/Regression.xml`)

---

## Quick Start

### 1. Add the plugin to `pom.xml`

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.sdrss</groupId>
            <artifactId>testng-starter-maven-plugin</artifactId>
            <version>2.0.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>test</goal>
                    </goals>
                    <configuration>
                        <suiteXmlFiles>src/test/resources/Regression.xml</suiteXmlFiles>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 2. Run

```bash
mvn testng-starter:test
```

The plugin binds to the `test` phase by default, so it also runs as part of `mvn test` or `mvn verify`.

---

## Suite File Discovery

There are two ways to specify which TestNG suite files to run:

**Option A – Explicit paths (full or relative to project root):**
```bash
mvn testng-starter:test -DsuiteXmlFiles=src/test/resources/Smoke.xml,src/test/resources/Regression.xml
```

**Option B – Directory scan + file name(s):**

Set `suitesSearchDirectory` to a base directory and provide only the file name(s) in `suiteXmlFiles`. The plugin will recursively search the directory for matching files.
```bash
mvn testng-starter:test -DsuitesSearchDirectory=src/test/resources/ -DsuiteXmlFiles=Smoke.xml,Regression.xml
```

**Post-build suites:** Use `suiteXmlFilesPostBuild` to run additional suite(s) *after* the main suites complete — useful for teardown or code coverage collection.

---

## Command Line Examples

```bash
# Minimal — run a single suite
mvn testng-starter:test -DsuiteXmlFiles=src/test/resources/MySuite.xml

# Parallel execution with retry
mvn testng-starter:test \
  -DsuiteXmlFiles=src/test/resources/MySuite.xml \
  -Dparallel=METHODS \
  -DthreadPoolSize=4 \
  -DmaxTestRetryFailures=2 \
  -DexecuteTestngFailedxml=true

# Pass system properties and run only specific groups
mvn testng-starter:test \
  -DsuiteXmlFiles=src/test/resources/MySuite.xml \
  -DsystemProperties=env:staging,timeout:30 \
  -Dgroups=smoke

# Fail fast with custom listeners and ReportNG output
mvn testng-starter:test \
  -DsuiteXmlFiles=src/test/resources/MySuite1.xml,src/test/resources/MySuite2.xml \
  -DfailFast=true \
  -DreportNGOutputDirectory=html \
  -Dlisteners=com.mypackage.TestListener,com.mypackage.SuiteListener

# Search directory for suites, known defects mode on
mvn testng-starter:test \
  -DsuitesSearchDirectory=src/test/resources/ \
  -DsuiteXmlFiles=MySuite.xml \
  -DhandleKnownDefectsAsFailures=false \
  -DmaxTestRetryFailures=2 \
  -DexecuteTestngFailedxml=true
```

---

## Configuration Reference

All parameters can be set via `<configuration>` in `pom.xml` or as `-D` command-line properties.


### Suite Selection

| Parameter | Default | Description |
|-----------|---------|-------------|
| `suiteXmlFiles` | *(required)* | Comma-separated list of TestNG suite XML files to run. Provide full paths or file names when used with `suitesSearchDirectory`. |
| `suitesSearchDirectory` | | Base directory to recursively search for the suite files listed in `suiteXmlFiles`. |
| `suiteXmlFilesPostBuild` | | Comma-separated suite files to execute after `suiteXmlFiles` complete. Useful for teardown or coverage tasks. |
| `skipTests` | `false` | Skip test execution entirely. |

### TestNG Core

| Parameter | Default | Description |
|-----------|---------|-------------|
| `configFailurePolicy` | `SKIP` | How to handle a configuration method (`@BeforeMethod` etc.) that has failed once. Values: `SKIP`, `CONTINUE`. See [TestNG docs](https://testng.org/doc). |
| `preserveOrder` | `true` | Run tests in the order they are declared in the suite XML. See [TestNG docs](https://testng.org/doc). |
| `parallel` | `NONE` | Enable parallel execution. Values: `NONE`, `METHODS`, `CLASSES`, `INSTANCES`, `TESTS`. See [TestNG docs](https://testng.org/doc). |
| `threadPoolSize` | | Number of threads to use when `parallel` is set. See [TestNG docs](https://testng.org/doc). |
| `suiteThreadPoolSize` | | Thread pool size for running multiple suites in parallel. See [TestNG docs](https://testng.org/doc). |
| `dataProviderThreadCount` | | Number of threads to use for data providers. See [TestNG docs](https://testng.org/doc). |
| `randomizeSuites` | `false` | Randomize the order in which suites are executed. See [TestNG docs](https://testng.org/doc). |
| `isJUnit` | `false` | Run in JUnit compatibility mode. See [TestNG docs](https://testng.org/doc). |
| `groups` | | Comma-separated list of groups to include in the run. See [TestNG docs](https://testng.org/doc). |
| `excludedGroups` | | Comma-separated list of groups to exclude from the run. See [TestNG docs](https://testng.org/doc). |
| `listeners` | | Comma-separated list of fully qualified custom listener class names to register with TestNG (e.g. `com.mypackage.MyListener`). See [TestNG docs](https://testng.org/doc). |
| `outputDirectory` | `test-output` | Directory where TestNG writes its XML results and default reports. |
| `toggleFailureIfAllTestsWereSkipped` | | When `true`, fail the build if all tests were skipped (e.g. due to a data provider failure). See [TestNG docs](https://testng.org/doc). |
| `systemProperties` | | Comma-separated key:value pairs set as Java system properties before test execution. Example: `env:staging,baseUrl:https://myapp`. |

### Retry and Failure Control

| Parameter | Default | Description |
|-----------|---------|-------------|
| `maxTestRetryFailures` | `0` | Maximum number of times a failed test is retried. Set to `0` to disable retries. Requires the ReportNG retry listener (enabled automatically). See [ReportNG](https://github.com/sdrss/reportNG). |
| `executeTestngFailedxml` | `false` | After the main run, generate and execute `testng-failed.xml` to re-run only failed tests. Useful in conjunction with `maxTestRetryFailures`. See [TestNG docs](https://testng.org/doc). |
| `globalTestTimeOut` | `0` | Maximum duration (milliseconds) for each test method. Set to `0` to disable the timeout listener. See [ReportNG](https://github.com/sdrss/reportNG). |
| `failFast` | `false` | Abort the entire test run on the first test failure. Remaining tests are marked as skipped. Requires ReportNG listener (registered automatically). See [ReportNG](https://github.com/sdrss/reportNG). |
| `failOnErrors` | `false` | Exit with code `-1` (build failure) when any test failures are detected, even if Maven would otherwise consider the build successful. See [ReportNG](https://github.com/sdrss/reportNG). |

### ReportNG HTML Report

| Parameter | Default | Description |
|-----------|---------|-------------|
| `generateReportNGhtmlReport` | `true` | Generate the ReportNG HTML report. See [ReportNG](https://github.com/sdrss/reportNG). |
| `reportNGOutputDirectory` | `html` | Sub-directory (under `outputDirectory`) where the ReportNG HTML report is written. |
| `reportNGhtmlReportTitle` | | Custom title displayed at the top of the ReportNG HTML report. See [ReportNG](https://github.com/sdrss/reportNG). |
| `showPassedConfigurations` | `true` | Include passed `@Before*` / `@After*` configuration methods in the HTML report. See [ReportNG](https://github.com/sdrss/reportNG). |
| `logOutputReport` | `false` | Append captured log output to the HTML report. See [ReportNG](https://github.com/sdrss/reportNG). |
| `handleKnownDefectsAsFailures` | `false` | When `true`, tests annotated with `@KnownDefect` are treated as failures rather than a distinct status. See [ReportNG](https://github.com/sdrss/reportNG). |

### Additional Report Formats

| Parameter | Default | Description |
|-----------|---------|-------------|
| `generateXMLReport` | `true` | Generate the default TestNG XML result report. See [TestNG docs](https://testng.org/doc). |
| `generateHtmlReport` | `false` | Generate the default TestNG HTML report (separate from ReportNG). See [TestNG docs](https://testng.org/doc). |
| `generateJunitReport` | `false` | Generate a JUnit-compatible XML report (useful for CI integrations). See [TestNG docs](https://testng.org/doc). |

---

## How It Works

1. **Classpath construction** — The plugin collects all project artifacts (test scope included) and builds a `URLClassLoader` from them. This ensures that test code and its dependencies are available at runtime without contaminating the plugin's own classloader.
2. **Isolated thread execution** — `TestNGStarterMainClass` runs inside a custom `IsolatedThreadGroup`. Any uncaught exceptions bubble back to Maven as build failures. Unresponsive threads are terminated after a 15-second grace period.
3. **TestNG configuration** — All plugin parameters are translated into `TestNG` API calls and ReportNG system properties before `TestNG.run()` is invoked.
4. **Listener registration** — Depending on configuration, the following listeners are registered automatically:
   - `HTMLReporter` (ReportNG) — when `generateReportNGhtmlReport=true`
   - `IAnnotationTransformerListener` — when `globalTestTimeOut > 0` or `maxTestRetryFailures > 0`
   - `FailFastListener` — when `failFast=true`
   - `XMLReporter`, `JUnitXMLReporter` — controlled by `generateXMLReport` / `generateJunitReport`
5. **Retry run** — If `executeTestngFailedxml=true` and failures were recorded, the plugin runs `testng-failed.xml` automatically after the main run.

---

## License

[Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
