FROM livingatlases/java-11-base:latest
# Args and Envs
ARG APP_ARTIFACT=cas-management
ARG USER=cas-management
ENV APP_VERSION=6.5.5-2
ENV JAVA_OPTS="-Djava.awt.headless=true -Xmx1g -Xms256m -XX:+UseConcMarkSweepGC -Dcas.standalone.configurationDirectory=/data/cas-management/config/ -Dlog4j2.formatMsgNoLookups=true"
ENV LOGGING_CONFIG=/data/cas-management/config/log4j2.xml
ENV LOG_DIR=/var/log/atlas/cas-management
# Directories and perms
RUN mkdir -p /data/$APP_ARTIFACT $LOG_DIR && \
    groupadd -r $USER -g 1000 && useradd -r -g $USER -u 1000 -m $USER && \
    chown -R $USER:$USER /data/$APP_ARTIFACT $LOG_DIR
WORKDIR /opt/atlas/$APP_ARTIFACT
# war
ADD https://nexus.ala.org.au/service/local/repositories/releases/content/au/org/ala/${APP_ARTIFACT}/${APP_VERSION}/${APP_ARTIFACT}-${APP_VERSION}-exec.war app.war
RUN chown -R $USER:$USER /opt/atlas/$APP_ARTIFACT
USER $USER
EXPOSE 8070
# cas-management don't use the JAVA_OPTS
# Lint with:
# docker run --rm -i hadolint/hadolint < Dockerfile
# hadolint ignore=DL3025
ENTRYPOINT dockerize -wait tcp://mysql:3306 -wait tcp://cas:9000 -wait tcp://mongo:27017 -timeout 120s sh -c "java -jar app.war"
