# P3 Entry Resource [![Build Status](https://travis-ci.org/fusepoolP3/p3-entry-resource.svg?branch=master)](https://travis-ci.org/fusepoolP3/p3-entry-resource)


This project delivers the entry resource for a P3 platform instance.

What is a *P3 platform instance*? A P3 platform is a set of web and linked data
applications that allows the user to transform and store data using the Linked 
Data Platform (LDP) standard as well as the 
[Transforming Container API](https://github.com/fusepoolP3/overall-architecture/blob/master/transforming-container-api.md). 

What is the the *entry resource*? That's the Linked Data resource that links to 
the web and linked applications that constitute the platform instance.

So what is this project about? This project provides the web application that 
returns an RDF description of a platform instance. It also provides and HTML
representation of the platform which uses [LD2h](https://github.com/rdf2h/ld2h) to render the platform using 
[RDF2h](https://github.com/rdf2h/rdf2h) templates.

Without cloning the repository or compiling the project you can run the application
with docker:

    docker run --rm -ti -p 80:8080 fusepoolp3/entry-resource

The first access to the service using a JavaScript capable client will configure 
the platform instance. For that it uses the hostname of the request, so it is
important to use the correct hostname on the first request for the platform resource.

Starting P3 Entry Resource with the above command will use a default configuration
script that configure a new platform instance using services of the [Platform 
Reference Implementation](https://github.com/fusepoolP3/p3-platform-reference-implementation) 
instance on `sandbox.fusepool.info`.

Normally you want to provide your own configuration script. With docker you can 
provide a  backend-config.js configuration script as follows. Run the docker 
image from the directory with your `01-backend-config.js` file:

    docker run --rm -v $(pwd):/etc/fusepool-p3/boot-scripts/ -p 80:8080 fusepoolp3/entry-resource 

When launched `p3-entry-resource` will check for configuration scripts in `~/.fusepool-p3/boot-script`, in `/etc/fusepool-p3/boot-scripts/` as well as its internal resource folder [src/eu/fusepool/p3/entry/default-config/](entry/src/eu/fusepool/p3/entry/default-config/). In the above example we override the built-in `01-backend-config.js` in full, this script has to implemement a function`P3BackendConfigurator.initialize` taht gets an [PlatformEntryConfigurator](entry/src/META-INF/resources/js/PlatformEntryConfigurator.js) instance as argument and returns a promise that is fulfilled when the platform is configured. To configure the platform the configuration script should invoke the methods provided by the `PlatformEntryConfigurator` instance. 

Rather than fully replacing the configuration script we can also instead add additional scripts that selectively modify the default configuration class, this typically makes our script much shorter by avoiding code duplication.

Typically one would override one or several of the following:

* P3BackendConfigurator.prototype.serviceHost: a string with the hostname
* P3BackendConfigurator.prototype.getLdpRoot: a function returning a promise for the LDP Root
* P3BackendConfigurator.prototype.registerRegistries: a function retuning a promise that is fullfilled when the various registries are registered
* P3BackendConfigurator.prototype.registerBackendfeatures: a function returning a promise that is fulfilled when the LDP-Root and the SPARQL Endpoint are registered

After the first configuration an instance stores its configuration persistently 
in `.fusepool-p3\PlatformConfig.ser`, so delete this file if you want to create 
a new configuration.


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

    docker run --rm -ti -p 80:8080 fusepoolp3/entry-resource
    
### Maven

You can also build with maven:

    mvn install

This creates an executable jar `launcher/target`.

Execute with:

    java -jar launcher/target/launcher-*.jar"


