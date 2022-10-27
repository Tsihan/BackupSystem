/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022/10/5 23:39:24                           */
/*==============================================================*/


drop table if exists downloads;

drop table if exists s3s;

drop table if exists transfers;

drop table if exists uploads;

drop table if exists users;

/*==============================================================*/
/* Table: downloads                                             */
/*==============================================================*/
create table downloads
(
   download_userid      varchar(6) not null,
   s3_name              varchar(18) not null,
   download_time        datetime not null,
   primary key (download_userid, download_time)
);

/*==============================================================*/
/* Table: s3s                                                   */
/*==============================================================*/
create table s3s
(
   s3_name              varchar(18) not null,
   s3_lastmodifiedtime  datetime,
   s3_ownerid           varchar(6),
   primary key (s3_name)
);

/*==============================================================*/
/* Table: transfers                                             */
/*==============================================================*/
create table transfers
(
   cloud_srcuserid      varchar(6) not null,
   cloud_desuserid      varchar(6) not null,
   s3_name              varchar(18) not null,
   cloud_time           datetime not null,
   primary key (s3_name, cloud_srcuserid, cloud_desuserid, cloud_time)
);

/*==============================================================*/
/* Table: uploads                                               */
/*==============================================================*/
create table uploads
(
   upload_userid        varchar(6) not null,
   s3_name              varchar(18) not null,
   upload_time          datetime not null,
   primary key (upload_userid, upload_time)
);

/*==============================================================*/
/* Table: users                                                 */
/*==============================================================*/
create table users
(
   user_id              varchar(6) not null,
   user_name            varchar(32) not null,
   user_encryptedpassword varchar(32) not null,
   user_email           varchar(128) not null,
   primary key (user_id)
);

alter table downloads add constraint FK_s3_senddata foreign key (s3_name)
      references s3s (s3_name) on delete restrict on update restrict;

alter table downloads add constraint FK_user_askdownload foreign key (download_userid)
      references users (user_id) on delete restrict on update restrict;

alter table transfers add constraint FK_transfer_receive foreign key (s3_name)
      references s3s (s3_name) on delete restrict on update restrict;

alter table transfers add constraint FK_transfer_send foreign key (cloud_srcuserid)
      references users (user_id) on delete restrict on update restrict;

alter table uploads add constraint FK_s3_receivedata foreign key (s3_name)
      references s3s (s3_name) on delete restrict on update restrict;

alter table uploads add constraint FK_user_askupload foreign key (upload_userid)
      references users (user_id) on delete restrict on update restrict;

