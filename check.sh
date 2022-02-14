#!/bin/sh
./mvnw clean package
unzip -l target/cas-management-exec.war
