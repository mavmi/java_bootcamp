drop table if exists message;
drop table if exists "user";

create table if not exists "user"(
     id bigint generated by default as identity primary key,
     email varchar not null,
     password varchar not null,
     log boolean not null
);

create table if not exists message(
    id serial primary key,
    author bigint not null,
    text varchar not null,
    datetime timestamp not null,
    constraint fk_message_author foreign key (author) references "user"(id)
);

