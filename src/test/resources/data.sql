INSERT INTO users (id, email, password, authority)
VALUES (1, 'lyah.artem10@mail.ru', '$2y$10$ZPgg5k.SQaJIxjGF7AU15.GNVF2U7MVJJWgMxkyuXjW550XIEEK52', 'ACTIVE');

INSERT INTO users (id, email, password, authority)
VALUES (2, 'lyah.artem10@gmail.com', '$2y$10$hkUwTHYaxqemq.NMf8l66OD.4M1RSCwCT9dl461J4gKAMdaNU1MkO', 'ACTIVE');

INSERT INTO users (id, email, password, authority, reset_token)
VALUES (3, 'd10@gmail.com', '$2y$10$x.jaNOvtBnsMqyhehZ5ituZzUAGnrHiSXzme1/i0EzrcWgRHMl0Ve', 'RESET', '1d6c4215-ceb3-4968-ac35-3456de6b4aa9');


INSERT INTO notes (id, title, text, user_id, creation_time)
VALUES (1, 'Спортзал', 'Сходить в спортзал в четверг в 11:20', 1, '2022-03-13 03:14:07');

INSERT INTO notes (id, title, text, user_id, creation_time)
VALUES (2, 'Лабораторная работа', 'Сделать лабораторную работу до пятницы', 1, '2022-03-14 13:14:07');

INSERT INTO notes (id, title, text, user_id, creation_time)
VALUES (3, 'Магазин', 'Пойти вечером в магазин за продуктами в пятнуцу', 1, '2022-03-15 10:14:07');

INSERT INTO notes (id, title, text, user_id, creation_time)
VALUES (4, 'Прогулка в парке', 'Пойти погулять вечером в субботу', 1, '2022-03-16 8:14:07');
