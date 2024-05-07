create table if not exists artist
(
    id              serial primary key,
    name            varchar(200) not null,
    genre           varchar(100) not null,
    origin          varchar(500) not null
);
