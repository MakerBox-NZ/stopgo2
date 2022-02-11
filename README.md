# Stopgo

StopGo v2.0 is a stop-motion animation application.
Its goal is to be a simple and direct way to take a series of photographs, with onion-skinning, to create stop-motion animation.

StopGo is free and open source.

## Download and Install

Git is where we do development. If you want to download and use StopGo to make movies, get it here:

http://makerbox.org.nz/about-2/stopgo/

You are free to use it at home, in the classroom, or anywhere!
You are free to share it with others.

## System Requirements

To use StopGo, you must install [Java (OpenJDK) 11](https://adoptopenjdk.net/).

## Development 

This version of Stopgo has been re-written in Java (v1.0 was written in Python).
If you are a Java coder, or you're learning Java, then you can help us improve StopGo.

To contribute code:

* Download and install [NetBeans](https://netbeans.apache.org/). It's also free and open source and works on all platforms.
* Download and install [Maven](https://maven.apache.org/) so NetBeans can manage dependencies and build a Stopgo package.
* Download the Stopgo source code by cloning this Git repository.

Read through the project code carefully to understand how StopGo works.
Once you understand which class does what, and what each method provides, start making whatever improvements you feel are needed.

When you've finished, submit a patch or merge request through Git or by email.

## Build

To build this project from source code, use Maven:

```bash
$ mvn clean package
```

Builds are saved to an auto-generated `targets` directory.
Run it using this Java command (replacing `X.Y` with the actual version number):

```bash
$ java -jar targets/Stopgo-X.Y.jar
```

### When build targets fail

It's possible to have more than one version of Java installed at one time.
Your operating system may provide, for instance, Java 8, while Stopgo is written for Java 11.
This can cause errors like `invalid target build`, but it's easy to fix.

On Linux, you can set your default Java version with `alternatives`:

```bash
$ alternatives --config java
```

Verify your Java version:

```bash
$ java -version
openjdk version "11.0.14" 2022-01-18 LTS
```

On all platforms, you must also set the `$JAVA_HOME` [environment variable](https://opensource.com/article/19/8/what-are-environment-variables).

First, find out where your Java 11 executable is located.
Sometimes this involves following symlinks, and symlinks to symlinks.
Just keep following the trail until you find the actual executable.

```bash
$ ls -l `which java`
/usr/bin/java -> /etc/alternatives/java
$ ls -l /etc/alternatives/java
/etc/alternatives/java -> /usr/lib/jvm/java-11-openjdk/bin/java
$ ls -l /usr/lib/jvm/java-11-openjdk/bin/java
/usr/lib/jvm/java-11-openjdk/bin/java
```

Verify that you've found the executable, and not a symlink:

```bash
$ file /usr/bin/java
/usr/bin/java: symbolic link to /etc/alternatives/java
$ file /usr/lib/jvm/java-11-openjdk/bin/java
ELF 64-bit LSB shared object, x86-64, version 1 (SYSV), dynamically linked, interpreter /lib64/ld-linux-x86-64.so.2
```

The base directory of the executable is your `JAVA_HOME`. 
In this example, that's `/usr/lib/jvm/java-11-openjdk` but on your system it could be `/usr/lib64/adoptopenjdk/bin/java` or `/usr/local/Cellar/openjdk@11/11.0.10/libexec/openjdk.jdk/Contents/Home` or `C:\Program Files\Java\jdk-11.0.14` or whatever location your system uses for Java.

On Linux or Mac, you can set your `JAVA_HOME` with this command:

```bash
$ echo "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk" >> ~/.bashrc
$ source ~/.bashrc
```

On Windows: 

```
PS> [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-11.0.14")
```

Confirm that Maven uses your new `JAVA_HOME`:

```bash
$ mvn -version
Apache Maven 3.5.4 (Red Hat 3.5.4-5)
Maven home: /usr/share/maven
Java version: 11.0.14, vendor: Red Hat, Inc., runtime: /usr/lib/jvm/java-11-openjdk-11
```

Now you can build the project with the correct target.

## Bugs

Should you find bugs in this software, file issues on either [Github](https://github.com/MakerBox-NZ/stopgo2) or [Gitlab](https://gitlab.com/makerbox/stopgo2).
