#!/bin/sh
#./gradlew clean assemble
java -Xmx1G -Xms1g -Dcas.standalone.configurationDirectory=/data/cas-management/config -jar build/libs/cas-management-6.5.4-1-SNAPSHOT-exec.war
