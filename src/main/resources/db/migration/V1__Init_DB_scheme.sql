create sequence hibernate_sequence start 100000 increment 1;


    create table dialog (
       id int8 not null,
        last_update timestamp,
        interlocutor_one_id int8 NOT NULL ,
        interlocutor_two_id int8 NOT NULL,
        primary key (id)
    );


    create table message (
       id int8 not null,
        creation_time timestamp NOT NULL,
        text varchar(2048) NOT NULL,
        user_id int8,
        dialog_id int8,
        primary key (id)
    );


    create table user_role (
       user_id int8 not null,
        roles varchar(255)
    );


    create table user_info (
       user_id int8 not null,
        country_code varchar(255),
        communication_level int2,
        gender varchar(255),
        primary key (user_id)
    );


    create table usr (
       id int8 not null,
        email varchar(255),
        password varchar(255) NOT NULL,
        username varchar(255) NOT NULL,
        primary key (id)
    );


    alter table if exists dialog
       add constraint dialog_unique_interlocutors_idx unique (interlocutor_one_id, interlocutor_two_id);


    alter table if exists usr
       add constraint users_unique_email_idx unique (email);


    alter table if exists usr
       add constraint users_unique_username_idx unique (username);


    alter table if exists dialog
       add constraint dialog_usr_ione_fk
       foreign key (interlocutor_one_id)
       references usr;


    alter table if exists dialog
       add constraint dialog_usr_itwo_fk
       foreign key (interlocutor_two_id)
       references usr;


    alter table if exists message
       add constraint message_usr_fk
       foreign key (user_id)
       references usr;


    alter table if exists message
       add constraint message_dialog_fk
       foreign key (dialog_id)
       references dialog;


    alter table if exists user_role
       add constraint user_role_usr_fk
       foreign key (user_id)
       references usr;


    alter table if exists user_info
       add constraint user_info_usr_fk
       foreign key (user_id)
       references usr;