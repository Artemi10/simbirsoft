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
