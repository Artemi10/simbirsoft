FROM openjdk:17-alpine
EXPOSE 8080
ADD target/devanmejia.applist.jar devanmejia.applist.jar
ENTRYPOINT ["java", "-Duser.language=ru", "-Duser.country=RU", "-Duser.timezone=Europe/Moscow", "-jar", "/devanmejia.applist.jar"]
