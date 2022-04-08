create table users
(
    id          bigserial
        constraint users_pk
            primary key,
    email       varchar(255) not null,
    password    varchar(255) not null,
    authority   varchar(6)   not null,
    reset_token varchar(128)
);

alter table users
    owner to postgres;

create table applications
(
    id            bigserial
        constraint applications_pk
            primary key,
    name          varchar(255) not null,
    creation_time timestamp    not null,
    user_id       bigint       not null
        constraint applications_users_id_fk
            references users
            on delete cascade
);

alter table applications
    owner to postgres;

create unique index applications_id_uindex
    on applications (id);

create unique index users_email_uindex
    on users (email);

create unique index users_id_uindex
    on users (id);

create table events
(
    id                bigserial
        constraint events_pk
            primary key,
    name              varchar(50) not null,
    extra_information varchar(255),
    time              timestamp   not null,
    application_id    bigint      not null
        constraint events_applications_id_fk
            references applications
            on delete cascade
);

alter table events
    owner to postgres;

create unique index events_id_uindex
    on events (id);


INSERT INTO users (email, password, authority)
VALUES ('lyah.artem10@mail.ru', '$2y$10$ZPgg5k.SQaJIxjGF7AU15.GNVF2U7MVJJWgMxkyuXjW550XIEEK52', 'ACTIVE');

INSERT INTO users (email, password, authority)
VALUES ('lyah.artem10@gmail.com', '$2y$10$hkUwTHYaxqemq.NMf8l66OD.4M1RSCwCT9dl461J4gKAMdaNU1MkO', 'ACTIVE');

INSERT INTO users (email, password, authority, reset_token)
VALUES ('d10@gmail.com', '$2y$10$x.jaNOvtBnsMqyhehZ5ituZzUAGnrHiSXzme1/i0EzrcWgRHMl0Ve', 'RESET', '1d6c4215-ceb3-4968-ac35-3456de6b4aa9');


INSERT INTO applications (name, user_id, creation_time)
VALUES ('Simple CRUD App', 1, '2022-03-13 03:14:07');

INSERT INTO applications (name, user_id, creation_time)
VALUES ('Todo list', 1, '2022-03-14 13:14:07');

INSERT INTO applications (name, user_id, creation_time)
VALUES ('Flight Timetable', 1, '2022-03-15 10:14:07');

INSERT INTO applications (name, user_id, creation_time)
VALUES ('User chat', 1, '2022-03-16 8:14:07');


INSERT INTO events (name, extra_information, time, application_id)
VALUES ('User successfully signed up', 'Extra information', '2022-03-14 22:14:07', 2);

INSERT INTO events (name, extra_information, time, application_id)
VALUES ('User successfully logged in', 'Extra information', '2022-03-14 23:15:07', 2);

INSERT INTO events (name, extra_information, time, application_id)
VALUES ('Add new note', 'Extra information', '2022-03-17 23:15:47', 2);

INSERT INTO events (name, extra_information, time, application_id)
VALUES ('Update note', 'Extra information', '2022-04-14 23:39:07', 2);

