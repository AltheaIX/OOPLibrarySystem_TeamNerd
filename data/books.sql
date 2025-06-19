create table books
(
    id         serial
        primary key,
    isbn       text,
    title      text                    not null,
    author     text                    not null,
    quantity   integer   default 0     not null,
    created_at timestamp default now() not null
);

alter table books
    owner to postgres;

