FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.6_10

LABEL Descripción="wstipocambio" \
      Autor="jronal@gmail.com" \
      Versión="0.0.1"

ENV APP_NAME=wstipocambio APP_VERSION=0.0.1-SNAPSHOT
ARG APP_WORKDIR=/app


RUN export TZ='America/Lima' && \
    apk add --no-cache tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone && \
    mkdir -p $APP_WORKDIR && chmod 777 $APP_WORKDIR

COPY target/$APP_NAME-$APP_VERSION.jar $APP_WORKDIR
WORKDIR $APP_WORKDIR
EXPOSE 8089
CMD java -jar -Xmx700M $APP_NAME-$APP_VERSION.jar -web -webAllowOthers -tcp -tcpAllowOthers -browser


