[![](https://jitpack.io/v/o7-Fire/Atomic-Library.svg)](https://jitpack.io/#o7-Fire/Atomic-Library)
---
A frankenstein library that nobody ask for

note replace -SNAPSHOT with commit hash

gradle:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

```groovy
dependencies {
    implementation "com.github.o7-Fire.Atomic-Library:Atomic:-SNAPSHOT"
    implementation "com.github.o7-Fire.Atomic-Library:Desktop:-SNAPSHOT"
}
```

maven:
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

```xml
<dependencies>
	<dependency>
	    <groupId>com.github.o7-Fire.Atomic-Library</groupId>
	    <artifactId>Atomic</artifactId>
	    <version>-SNAPSHOT</version>
	</dependency>
	<dependency>
	    <groupId>com.github.o7-Fire.Atomic-Library</groupId>
	    <artifactId>Desktop</artifactId>
	    <version>-SNAPSHOT</version>
	</dependency>
</dependencies>
