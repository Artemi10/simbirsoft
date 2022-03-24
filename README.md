# Devanmejia ToDoList
>Simple CRUD Spring Boot project

![Logo](src/main/resources/static/images/logo.png "Project Logo")

## Table of Contents
* [General Info](#general-info)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Deployment](#deployment)
* [Contact](#contact)

## General Info
- Application allows authorized users to create, store, update and delete private apps.
- Users can easily manage their own apps.

## Technologies Used
- Java 17
- Spring Boot:
    * Spring Data JPA
    * Spring Security
- Lombok
- Tests:
    * JUnit
    * Mockito
- Postgresql
- Thymeleaf
- Materialize js

## Features
- Create account by filling out sign up form.
- Log in to user account with/without remember me token.
- Password recovery by sending resetting letter to user email.
- Log out from application.
- CRUD operations with apps.

## Deployment
- Download git project
> git clone git://github.com/Artemi10/simbirsoft.git
- Enter to project directory
> cd ./simbirsoft
- Start docker engine
- Run docker-compose command
> docker-compose up
- Open project in browser
> http://localhost:8080/

![Deployment commands](screenshots/commands.png "Deployment commands")

![Deployment app](screenshots/app.png "Deployment app")

## Contact
Email: lyah.artem10@mail.ru
