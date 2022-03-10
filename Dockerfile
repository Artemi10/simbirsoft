FROM openjdk:17-alpine
EXPOSE 8080
ADD target/devanmejia.todolist.jar devanmejia.todolist.jar
ENTRYPOINT ["java", "-Duser.language=ru", "-Duser.country=RU", "-Duser.timezone=Europe/Moscow", "-jar", "/devanmejia.todolist.jar"]
