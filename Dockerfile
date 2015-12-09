FROM niaquinto/gradle
MAINTAINER Reto Gmür <me@farewellutopia.com>

#Prepare
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Build
COPY ./ /usr/src/app

RUN gradle build export

ENTRYPOINT ["java"]
CMD ["-jar", "entry/generated/distributions/executable/launch.jar"]
