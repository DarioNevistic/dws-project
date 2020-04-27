create table user (
    id bigint not null auto_increment,
    email varchar(50) not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    password varchar(1000) not null,
    active boolean not null,
    email_verified boolean not null,
    user_type varchar(10) not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    version bigint not null,
    primary key (id)
) engine = InnoDB;

create table patient (
    id bigint not null auto_increment,
    user_id bigint not null,
    blood_type varchar(20),
    date_of_birth datetime(6),
    country_of_birth varchar(50),
    place_of_birth varchar(50),
    gender varchar(10),
    phone_number varchar(30),
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    version bigint not null,
    primary key (id)
) engine = InnoDB;

create table employee (
    id bigint not null auto_increment,
    user_id bigint not null,
    type varchar(10) not null,
    specialization varchar(100),
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    version bigint not null,
    primary key (id)
) engine = InnoDB;

create table role (
    id bigint not null auto_increment,
    role_name varchar(10) not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    version bigint not null,
    primary key (id)
) engine = InnoDB;

create table user_role (
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
) engine = InnoDB;

create table invitation (
    id bigint not null auto_increment,
    receiver VARCHAR(254) NOT NULL,
    status VARCHAR(20) NOT NULL,
    expiration DATETIME(6),
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    version bigint not null,
    primary key (id)
) engine = InnoDB;

create table invitation_token (
    id bigint not null auto_increment,
    invitation_id BIGINT not null,
    hash VARCHAR(40) not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    version bigint not null,
    primary key (id)
) engine = InnoDB;

create table active_ws_user (
    session_id varchar(100) not null,
    username varchar(50) not null,
    connection_time datetime(6) not null,
    primary key (session_id)
) engine = InnoDB;

create table conversation (
    id bigint not null auto_increment,
    title varchar(100) not null,
    creator_user_id bigint not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    deleted_at datetime(6),
    deleted boolean not null,
    version bigint not null,
    primary key (id)
) engine = InnoDB;

create table instant_message (
    id bigint not null auto_increment,
    sender_user_id bigint not null,
    payload text not null,
    message_type varchar(10) not null,
    conversation_id bigint not null,
    created_at datetime(6) not null,
    version bigint not null,
    primary key (id)
) engine = InnoDB;

create table participant (
    id bigint not null auto_increment,
    user_id bigint not null,
    conversation_id bigint not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    version bigint not null,
    primary key (id)
) engine = InnoDB;

alter table `employee`
ADD CONSTRAINT FK6lk0xml9r7okjdq0onka4ytju
FOREIGN KEY (`user_id`)
REFERENCES `user` (`id`)
    ON delete CASCADE;

alter table `patient`
ADD CONSTRAINT FKp6ttmfrxo2ejiunew4ov805uc
FOREIGN KEY (`user_id`)
REFERENCES `user` (`id`)
    ON delete CASCADE;

