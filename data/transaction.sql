create table transaction
(
    id          serial
        primary key,
    user_id     integer                 not null,
    book_id     integer                 not null,
    return_date timestamp,
    status      varchar(50)             not null,
    created_at  timestamp default now() not null
);

alter table transaction
    owner to postgres;

