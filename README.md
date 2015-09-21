# P3 Entry Resource

This project delivers the entry resource for a P3 platform instance.

## Cloning

Use the `--recursive` option when cloning this repo to make sure submodules are cloned too.

## Usage

The projects come with eclipse configuration files so that they can be used with bndtools. Just import both project into an empty eclipse workspace.

You can also build with gradle:

    gradle build
    
And create an executable jar:

    gradle export

You'll find the exectuable jar at ./entry/generated/distributions/executable/launch.jar.

Execute with:

    java -jar ./entry/generated/distributions/executable/launch.jar


