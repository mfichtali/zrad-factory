#################################################################################################
#                                               Run                                             #
#################################################################################################
FROM alpine/java:21-jre

# Argument from compose
ARG service_name
ARG service_version
ARG workdir_env

# Copy arg to env
ENV SERVICE_NAME=$service_name
ENV SERVICE_VERSION=$service_version
ENV WORKDIR_ENV=$workdir_env

# Set workdir
WORKDIR $WORKDIR_ENV

# Copy jar file
COPY ./core-infrastructure/target/$SERVICE_NAME-$SERVICE_VERSION.jar ref-core.jar

# RUN jar file
#ENTRYPOINT java -jar ref-core.jar
#ENTRYPOINT ["java", "-jar", "ref-core.jar"]