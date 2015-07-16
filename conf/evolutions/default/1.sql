# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table users (
  id                        bigint not null,
  email                     varchar(255),
  name                      varchar(255),
  active                    boolean,
  email_validated           boolean,
  right                     integer not null,
  constraint ck_users_right check (right in (0,1)),
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id))
;

create table linked_account (
  id                        bigint not null,
  base_user_id              bigint,
  provider_user_id          varchar(255),
  provider_key              varchar(255),
  constraint pk_linked_account primary key (id))
;

create sequence users_seq;

create sequence linked_account_seq;

alter table linked_account add constraint fk_linked_account_baseUser_1 foreign key (base_user_id) references users (id) on delete restrict on update restrict;
create index ix_linked_account_baseUser_1 on linked_account (base_user_id);


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists users;

drop table if exists linked_account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists users_seq;

drop sequence if exists linked_account_seq;

