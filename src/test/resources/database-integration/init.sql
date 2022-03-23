create table users
(
    id          bigserial,
    email       varchar(254) not null,
    password    varchar(254) not null,
    authority   varchar(6)   not null,
    reset_token varchar(36)
);

alter table users
    owner to postgres;

create unique index users_email_uindex
    on users (email);

create unique index users_id_uindex
    on users (id);

create table notes
(
    id            bigserial
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

alter table notes
    owner to postgres;

create unique index notes_id_uindex
    on notes (id);


INSERT INTO users (email, password, authority)
VALUES ('lyah.artem10@mail.ru', '$2y$10$ZPgg5k.SQaJIxjGF7AU15.GNVF2U7MVJJWgMxkyuXjW550XIEEK52', 'ACTIVE');

INSERT INTO users (email, password, authority)
VALUES ('lyah.artem10@gmail.com', '$2y$10$hkUwTHYaxqemq.NMf8l66OD.4M1RSCwCT9dl461J4gKAMdaNU1MkO', 'ACTIVE');

INSERT INTO users (email, password, authority, reset_token)
VALUES ('d10@gmail.com', '$2y$10$x.jaNOvtBnsMqyhehZ5ituZzUAGnrHiSXzme1/i0EzrcWgRHMl0Ve', 'RESET', '1d6c4215-ceb3-4968-ac35-3456de6b4aa9');


INSERT INTO notes (title, text, user_id, creation_time)
VALUES ('Gym', 'Go to gym on Thursday', 1, '2022-03-13 03:14:07');

INSERT INTO notes (title, text, user_id, creation_time)
VALUES ('Homework', 'Do homework on Friday evening', 1, '2022-03-14 13:14:07');

INSERT INTO notes (title, text, user_id, creation_time)
VALUES ('Shopping', 'Go shopping to buy products on Friday', 1, '2022-03-15 10:14:07');

INSERT INTO notes (title, text, user_id, creation_time)
VALUES ('Park', 'Go to park on Saturday', 1, '2022-03-16 8:14:07');
