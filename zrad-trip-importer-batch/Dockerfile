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

# Make sure /opt/app/workdir exists BEFORE setting the workdir
RUN mkdir -p $WORKDIR_ENV/workdir

# Set workdir
WORKDIR $WORKDIR_ENV

# Copy jar file
COPY ./batch/target/$SERVICE_NAME-$SERVICE_VERSION.jar zrad01-importer.jar

# Expose the debug port
EXPOSE 5005