Ticker Symbol Search
====================

[![Build](https://github.com/w3stling/tickersymbol/actions/workflows/build.yml/badge.svg)](https://github.com/w3stling/tickersymbol/actions/workflows/build.yml)
[![Download](https://img.shields.io/badge/download-3.1.1-brightgreen.svg)](https://search.maven.org/artifact/com.apptasticsoftware/tickersymbol/3.1.1/jar)
[![Javadoc](https://img.shields.io/badge/javadoc-3.1.1-blue.svg)](https://w3stling.github.io/tickersymbol/javadoc/3.1.1)
[![License](http://img.shields.io/:license-MIT-blue.svg?style=flat-round)](http://apptastic-software.mit-license.org)   
[![CodeQL](https://github.com/w3stling/tickersymbol/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/w3stling/tickersymbol/actions/workflows/codeql-analysis.yml)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=w3stling_tickersymbol&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=w3stling_tickersymbol)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=w3stling_tickersymbol&metric=coverage)](https://sonarcloud.io/summary/new_code?id=w3stling_tickersymbol)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=w3stling_tickersymbol&metric=bugs)](https://sonarcloud.io/summary/new_code?id=w3stling_tickersymbol)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=w3stling_tickersymbol&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=w3stling_tickersymbol)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=w3stling_tickersymbol&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=w3stling_tickersymbol)

> **Note** - from version 3.0.0:
> * New Java package name
> * New group ID in Maven / Gradle dependency declaration
> * Moved repository from `JCenter` to `Maven Central Repository`

A ticker symbol is an arrangement of characters (usually letters) representing a particular security listed on an
exchange or otherwise traded publicly. When a company issues securities to the public marketplace, it selects an
available ticker symbol for its securities that investors use to place trade orders.

This Java library makes it easier to automate ticker symbol lookups.
Requires at minimum Java 11.

Examples
--------
### Search ticker symbol by security identifier ISIN, CUSIP, or SEDOL.
Same as example above but searching for Ericsson B with ISIN number.
```java
TickerSymbolSearch tickerSymbol = new TickerSymbolSearch();
List<TickerSymbol> symbols = tickerSymbol.searchByIdentifier("SE0000108656");
```

Download
--------

Download [the latest JAR][1] or grab via [Maven][2] or [Gradle][3].

### Maven setup
Add dependency declaration:
```xml
<project>
    ...
    <dependencies>
        <dependency>
            <groupId>com.apptasticsoftware</groupId>
            <artifactId>tickersymbol</artifactId>
            <version>3.1.1</version>
        </dependency>
    </dependencies>
    ...
</project>
```

### Gradle setup
Add dependency declaration:
```groovy
dependencies {
    implementation 'com.apptasticsoftware:tickersymbol:3.1.1'
}
```

License
-------

    MIT License
    
    Copyright (c) 2022, Apptastic Software
    
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


[1]: https://search.maven.org/artifact/com.apptasticsoftware/tickersymbol/3.1.1/jar
[2]: https://maven.apache.org
[3]: https://gradle.org