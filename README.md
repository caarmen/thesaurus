Thesaurus
=========

This library provides a thesaurus based on:

* The the Roget's Thesaurus of English Words and Phrases, by Roget, 1911: http://www.gutenberg.org/ebooks/10681
* The WordNet 2.0 thesaurus used in LibreOffice: (https://github.com/LibreOffice/dictionaries/tree/master/en)

To test out the library on the command-line:

```
    ./gradlew clean cliJar
    java -jar example/build/libs/example-all-1.0-SNAPSHOT.jar <word to look up>
```

This brand new library is not yet available on a remote maven repository.

If you want to build the library to include it in your project:

To build the library:

```
./gradlew clean build
```


This will create the file `./library/build/libs/thesaurus-<version>.jar` which you can include in your project.

To deploy the jar to your local maven repository:

```
    ./gradlew clean install
```

Then include this maven dependency:

```
    <dependency>
      <groupId>ca.rmen</groupId>
      <artifactId>thesaurus</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
```

Or gradle dependency:

```
    compile 'ca.rmen:thesaurus:1.0-SNAPSHOT'
```
