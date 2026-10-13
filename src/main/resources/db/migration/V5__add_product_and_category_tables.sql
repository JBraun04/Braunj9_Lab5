create table categories
(
    id   bigint auto_increment
        primary key,
    name nvarchar(20) null
);

create table products
(
    id          bigint auto_increment
        primary key,
    name        nvarchar(20) null,
    price       decimal      null,
    category_id bigint       not null,
    constraint `FOREIGN`
        foreign key (category_id) references categories (id)
);

