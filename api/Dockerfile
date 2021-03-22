FROM gradle:6.8.3-jdk8 AS build
COPY --chown=gradle:gradle . /code
WORKDIR /code
RUN gradle build --no-daemon

EXPOSE 4567

CMD gradle run
