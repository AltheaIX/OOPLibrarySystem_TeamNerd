create table users
(
    id         bigint       default nextval('users_id_seq'::regclass) not null
        primary key,
    full_name  varchar(255),
    major      varchar(255),
    email      varchar(255),
    password   varchar(255),
    role       varchar(255) default 'user'::user_role                 not null,
    created_at timestamp    default now()
);

alter table users
    owner to postgres;

