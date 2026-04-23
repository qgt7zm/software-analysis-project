# Software Analysis Project Experimental Setup

Note: Versions are latest as of 3 Apr 2026

## Development Environment

### Languages

- JDK: 21 (preferred) or 17 (minimum)

### Static Analyzers

- IntelliJ IDEA: 2026.1
- JSpecify: 1.0.0
- NullAway: 0.13.1
- Error Prone: 2.48.0 (JDK 21) or 2.42.0 (JDK 17)

Use this IntelliJ code inspection [configuration file](SWA_Project.xml).

## Build System Configuration

### Gradle

Compile Command: `./gradlew clean <subproject>:compileJava`

See this partial [build.gradle.kts](gradle.kts) example.

### Maven

Compile Command: `./mvnw <—pl subproject> <—am> clean compile`
(You may need ``--am`` to build submodule dependencies first)

See this partial [pom.xml](maven.xml) example and [.mvn/jvm.config](maven.config) file.

## Benchmark Libraries

Tips

- Lines of code can be counted with `wc -l *.java` (non-recursive)
  or `find <dir> -name "*.java" | xargs wc -l` (recursive)
- Annotations can be counted with `grep -oi <pattern> *.java | wc -l`
  (special regex characters should be escaped)

### com.google.testing.compile/compile-testing

- Category: Compilation
- Version: 0.23.0
- Packages: All
- Size: 5,092 LoC
- Annotations: 94

### com.google.guava/guava

- Category: Utils
- Version: 33.5.0
- Packages: com.google.common.base
- Size: 13,806 LoC
- Annotations: 363
- Suppressions: 5

### org.jsoup/jsoup

- Category: HTML
- Version: 1.22.1
- Packages: org.jsoup.parser
- Size: 9,221 LoC
- Annotations: 54

### org.apache.logging.log4j/log4j-core

- Category: Logging
- Version: 3.0.0-beta3
- Packages: org.apache.logging.log4j.core, org.apache.logging.log4j.core.impl
- Size: 3,539 + 4,287 = 7,826 LoC
- Annotations: 52

### com.graphql-java/graphql-java (TODO)

- Category: Database
- Version: 25.0
- Packages: graphql.language
- Size: 12,370 LoC
- Annotations: TBA

### org.junit.jupiter/junit-jupiter-engine (TODO)

- Category: Testing
- Version: 6.0.3
- Packages: TBD
- Size: TBA
- Annotations: TBA