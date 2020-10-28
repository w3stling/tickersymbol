Ticker Symbol Search
====================

[![Build Status](https://travis-ci.com/w3stling/tickersymbol.svg?branch=master)](https://travis-ci.com/w3stling/tickersymbol)
[![Download](https://api.bintray.com/packages/apptastic/maven-repo/tickersymbol/images/download.svg)](https://bintray.com/apptastic/maven-repo/tickersymbol/_latestVersion)
[![Javadoc](https://img.shields.io/badge/javadoc-2.0.11-blue.svg)](https://w3stling.github.io/tickersymbol/javadoc/2.0.11)
[![License](https://img.shields.io/:license-MIT-blue.svg?style=flat-round)](https://apptastic-software.mit-license.org)   
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.apptastic%3Atickersymbol&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.apptastic%3Atickersymbol)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.apptastic%3Atickersymbol&metric=coverage)](https://sonarcloud.io/component_measures?id=com.apptastic%3Atickersymbol&metric=Coverage)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=com.apptastic%3Atickersymbol&metric=bugs)](https://sonarcloud.io/component_measures?id=com.apptastic%3Atickersymbol&metric=bugs)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=com.apptastic%3Atickersymbol&metric=vulnerabilities)](https://sonarcloud.io/component_measures?id=com.apptastic%3Artickersymbol&metric=vulnerabilities)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.apptastic%3Atickersymbol&metric=code_smells)](https://sonarcloud.io/component_measures?id=com.apptastic%3Atickersymbol&metric=code_smells)


A ticker symbol is an arrangement of characters (usually letters) representing a particular security listed on an
exchange or otherwise traded publicly. When a company issues securities to the public marketplace, it selects an
available ticker symbol for its securities that investors use to place trade orders.

This Java library makes it easier to automate ticker symbols lookup via Java stream API.
Requires at minimum Java 11.

Examples
--------
### Search ticker symbol by name
Search ticker symbols for Ericsson B that is listed on Nasdaq OMX Nordic (MIC equals to XSTO) and is traded in currency SEK. 
```java
TickerSymbolSearch tickerSymbol = new TickerSymbolSearch();
List<TickerSymbol> symbols = tickerSymbol.searchByName("Ericsson B")
                        .filter(s -> "XSTO".equals(s.getMic()))
                        .filter(s -> "SEK".equals(s.getCurrency()))
                        .collect(Collectors.toList());
```

### Search ticker symbol by ISIN code
Same as example above but searching for Ericsson B with ISIN number.
```java
TickerSymbolSearch tickerSymbol = new TickerSymbolSearch();
List<TickerSymbol> symbols = tickerSymbol.searchByIsin("SE0000108656")
                        .filter(s -> "XSTO".equals(s.getMic()))
                        .filter(s -> "SEK".equals(s.getCurrency()))
                        .collect(Collectors.toList());
```

Download
--------

Download [the latest JAR][1] or grab via [Maven][2] or [Gradle][3].

### Maven setup
Add JCenter repository for resolving artifact:
```xml
<project>
    ...
    <repositories>
        <repository>
            <id>jcenter</id>
            <url>https://jcenter.bintray.com</url>
        </repository>
    </repositories>
    ...
</project>
```

Add dependency declaration:
```xml
<project>
    ...
    <dependencies>
        <dependency>
            <groupId>com.apptastic</groupId>
            <artifactId>tickersymbol</artifactId>
            <version>2.0.11</version>
        </dependency>
    </dependencies>
    ...
</project>
```

### Gradle setup
Add JCenter repository for resolving artifact:
```groovy
repositories {
    jcenter()
}
```

Add dependency declaration:
```groovy
dependencies {
    implementation 'com.apptastic:tickersymbol:2.0.11'
}
```

License
-------

    MIT License
    
    Copyright (c) 2020, Apptastic Software
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.


[1]: https://bintray.com/apptastic/maven-repo/tickersymbol/_latestVersion
[2]: https://maven.apache.org
[3]: https://gradle.org