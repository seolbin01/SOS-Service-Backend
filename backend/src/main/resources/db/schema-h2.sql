drop table if exists user;

create table user (
    id bigint not null auto_increment,
    name varchar(255),
    email varchar(255) not null,
    password varchar(255),
    birth date,
    phone_number varchar(14),
    profile_image varchar(255),
    role varchar(255) not null,
    created_at datetime(6) not null,
    modified_at datetime(6) not null,
    child_id BIGINT NULL,
    primary key (id)
);

ALTER TABLE user
    ADD CONSTRAINT FK_USER_ON_CHILD FOREIGN KEY (child_id) REFERENCES user (id);

create table persistent_logins (
    username varchar(64) not null,
    series varchar(64) not null,
    token varchar(64) not null,
    last_used timestamp not null,
    primary key (series)
);