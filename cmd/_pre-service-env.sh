#!/bin/sh

JZCOMMONX_VERSION=LATEST
ZICACHEX_VERSION=LATEST

#Install dependencies into local repository
LOCAL_REPO=$PROJECT_HOME/repo/

#mvn install:install-file -Durl=file://$LOCAL_REPO -Dfile=mylib-1.0.jar -DgroupId=com.example -DartifactId=mylib -Dpackaging=jar -Dversion=1.0
