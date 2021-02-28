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

StopGo version 1 was written in Python.
This version of Stopgo has been re-written in Java.
If you're learning to code, you can help us improve StopGo.

To contribute code, download the source by cloning this Git repository.
The Git repository is an [Eclipse](http://eclipse.org) workspace plus configuration for [Netbeans](http://netbeans.apache.org/) for development.
You don't have to use Eclipse or Netbeans for development, but it's strongly encouraged (like StopGo itself, they're both free and open source and run on any platform).

Run the ``env.java`` script to configure the classpath, or configure it manually in your IDE:

```
java ./env.java stopgo-java/dot-classpath
```

Read through the project code carefully to understand how StopGo works.
Once you understand which class does what, and what each method provides, start making whatever improvements you feel are needed.
When you've finished, submit a patch or merge request through Git or by email.

### Gradle

If you use Gradle and have it installed, generate the Gradle files:

```
$ gradle wrapper
```

## Bugs

If you find bugs in this software, file issues on either [Github](https://github.com/MakerBox-NZ/stopgo2) or [Gitlab](https://gitlab.com/makerbox/stopgo2).

