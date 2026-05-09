FROM maven:3.9-eclipse-temurin-17-alpine

WORKDIR /docker/project

COPY . .

RUN apk add --no-cache curl \
  && curl -o allure.zip -L "https://github.com/allure-framework/allure2/releases/download/2.34.0/allure-2.34.0.zip" \
  && unzip allure.zip -d /opt/allure \
  && rm allure.zip \
  && ln -s /opt/allure/allure-2.34.0/bin/allure /usr/bin/allure \
  && mvn package -DskipTests