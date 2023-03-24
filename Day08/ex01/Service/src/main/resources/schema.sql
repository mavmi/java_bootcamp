drop table if exists "user";
create table if not exists "user"(
     id bigint primary key,
     email varchar not null
);
