# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cuisine (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_cuisine primary key (id))
;

create table vendor (
  vendor_id                 bigint auto_increment not null,
  name                      varchar(255),
  category                  varchar(255),
  district                  varchar(255),
  city                      varchar(255),
  contact                   bigint,
  speciality                varchar(255),
  cuisine                   varchar(255),
  cost                      integer,
  serves                    varchar(255),
  mode                      varchar(255),
  rating                    float,
  picture                   varbinary(255),
  constraint pk_vendor primary key (vendor_id))
;


create table vendor_cuisine (
  vendor_vendor_id               bigint not null,
  cuisine_id                     bigint not null,
  constraint pk_vendor_cuisine primary key (vendor_vendor_id, cuisine_id))
;



alter table vendor_cuisine add constraint fk_vendor_cuisine_vendor_01 foreign key (vendor_vendor_id) references vendor (vendor_id) on delete restrict on update restrict;

alter table vendor_cuisine add constraint fk_vendor_cuisine_cuisine_02 foreign key (cuisine_id) references cuisine (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table cuisine;

drop table vendor_cuisine;

drop table vendor;

SET FOREIGN_KEY_CHECKS=1;

