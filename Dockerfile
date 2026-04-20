FROM ubuntu:latest
LABEL authors="USER"
WORKDIR /app
COPY /target/BookSocialNetwork-*.jar .

EXPOSE 8080
ENV URL = jdbc:mysql:localhost://

CMD java -jar BookSocialNetwork-1.0.0.jar