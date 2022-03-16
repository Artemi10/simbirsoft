create table users
(
    id          bigint,
    email       varchar(254) not null,
    password    varchar(254) not null,
    authority   varchar(6)   not null,
    reset_token varchar(36)
);

create unique index users_email_uindex
    on users (email);

create unique index users_id_uindex
    on users (id);

create table notes
(
    id            bigint
        constraint notes_pk
            primary key,
    title         varchar(40)  not null,
    text          varchar(254) not null,
    user_id       bigint       not null
        constraint notes_users_id_fk
            references users (id)
            on update cascade on delete cascade,
    creation_time timestamp    not null
);

create unique index notes_id_uindex
    on notes (id);
