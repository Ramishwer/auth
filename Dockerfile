FROM gradle:8.6.0-jdk17
ARG env=local
WORKDIR /application
RUN mkdir target
COPY build.gradle /application
COPY settings.gradle /application
COPY src /application/src
COPY gradlew /application
ENV ENV ${env}
USER root
ENV MAVEN_OPTS="-Dmaven.repo.local=/root/.m2/repository"
RUN echo "$ENV environment is specified"
RUN gradle clean --no-daemon
CMD gradle clean assemble --no-daemon && cp build/libs/*.jar /application/target/