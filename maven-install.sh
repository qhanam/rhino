#!/bin/sh

mvn install:install-file \
  -Dfile=build/rhino1_7R5pre/js.jar \
  -DpomFile=pom.xml
