FROM openjdk:11-jre-slim
ARG APP_ARTIFACT=cas-management
ARG APP_VERSION=6.5.5-2
ENV JAVA_OPTS="-Djava.awt.headless=true -Xmx1g -Xms256m -XX:+UseConcMarkSweepGC -Dcas.standalone.configurationDirectory=/data/cas-management/config/ -Dlog4j2.formatMsgNoLookups=true"
ENV LOGGING_CONFIG=/data/cas-management/config/log4j2.xml
ENV LOG_DIR=/var/log/atlas/cas-management
WORKDIR /opt
ADD https://nexus.ala.org.au/service/local/repositories/releases/content/au/org/ala/${APP_ARTIFACT}/${APP_VERSION}/${APP_ARTIFACT}-${APP_VERSION}-exec.war app.war
RUN mkdir /data
RUN mkdir -p /var/log/atlas/cas-management
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
