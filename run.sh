#!/bin/sh
#./gradlew clean assemble
java -Xmx1G -Xms1g -Dcas.standalone.configurationDirectory=/data/cas-management/config -jar build/libs/cas-management-6.6.1-1-SNAPSHOT-exec.war
