FROM livingatlases/java-11-base:1.0.0

# Lint with:
# docker run --rm -i hadolint/hadolint < Dockerfile

# Args
ARG APP_ARTIFACT=cas-management
ARG USER=cas-management
ARG WAR_FILE=./build/libs/cas-management*.war
ARG DOCKERIZE_ARGS="-wait tcp://mysql:3306 -wait tcp://cas:9000 -wait tcp://mongo:27017 -timeout 120s"

# Env variables
ENV SERVER_PORT="8070"
ENV WAR_FILE_ENV=${WAR_FILE}
ENV JAVA_OPTS="-Djava.awt.headless=true -Xmx1g -Xms256m -XX:+UseConcMarkSweepGC -Dcas.standalone.configurationDirectory=/data/cas-management/config/ -Dlog4j2.formatMsgNoLookups=true"
ENV LOGGING_CONFIG=/data/cas-management/config/log4j2.xml
ENV LOG_DIR=/var/log/atlas/cas-management
ENV DOCKERIZE_OPTS=${DOCKERIZE_ARGS}

# Directories and perms
RUN mkdir -p /data/$APP_ARTIFACT $LOG_DIR && \
    groupadd -r $USER -g 1000 && useradd -r -g $USER -u 1000 -m $USER && \
    chown -R $USER:$USER /data/$APP_ARTIFACT $LOG_DIR
WORKDIR /opt/atlas/$APP_ARTIFACT

# war
COPY $WAR_FILE_ENV app.war
RUN chown -R $USER:$USER /opt/atlas/$APP_ARTIFACT

USER $USER
EXPOSE $SERVER_PORT
# hadolint ignore=DL3025
# cas-management don't use the JAVA_OPTS
ENTRYPOINT dockerize $DOCKERIZE_OPTS sh -c "java -jar app.war"
