FROM openjdk:17-alpine
EXPOSE 8080
ADD target/devanmejia.todolist.jar devanmejia.todolist.jar
ENTRYPOINT ["java", "-jar", "/devanmejia.todolist.jar"]
