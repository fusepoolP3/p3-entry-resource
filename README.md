# P3 Entry Resource [![Build Status](https://travis-ci.org/fusepoolP3/p3-entry-resource.svg?branch=master)](https://travis-ci.org/fusepoolP3/p3-entry-resource)


This project delivers the entry resource for a P3 platform instance.

## Cloning

Use the `--recursive` option when cloning this repo to make sure submodules are cloned too.

## Usage

This project can be used with docker, gradle or eclipse. Choose the way you prefer.

### Docker

With docker you can either build it locally or just get a pre-built version from dockerhub.

#### Building

    docker build  -t p3-entry-resource .
    
#### Running

To use the built build above and launch it on port 80:

    docker run --rm -ti -p 80:8080 p3-entry-resource
    
To use the version from dockerhub

    docker run --rm -ti -p 8081:8080 fusepoolp3/entry-resource
    
### Eclipse

The projects comes with eclipse configuration files so that they can be used with [bndtools](http://bndtools.org/). Just import both project into an empty eclipse workspace. As it is written in Scala you also need the [Scala IDE Plugin](http://scala-ide.org/).

 * Note that you cannot clone the project out of eclipse, as it is not an eclipse project but an eclipse workspace
 * Install the bndtools befotre opening the workspace

### Gradle

You can also build with gradle:

    gradle build
    
And create an executable jar:

    gradle export

You'll find the exectuable jar at ./entry/generated/distributions/executable/launch.jar.

Execute with:

    java -jar ./entry/generated/distributions/executable/launch.jar


