CREATE TABLE user (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  email varchar(100),
  name varchar(100) NOT NULL,
  password varchar(255),
  totp varchar(50),
  phone varchar(20),
  active boolean default true,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
  deleted_at timestamp,
  PRIMARY KEY user_pk_id(id),
  UNIQUE KEY user_uk_email(email),
  UNIQUE KEY user_uk_phone(phone)
);

CREATE TABLE role (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  role varchar(50) NOT NULL,
  PRIMARY KEY role_pk_id(id),
  UNIQUE KEY role_uk_role(role)
);

CREATE TABLE user_role (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  user_id bigint unsigned NOT NULL,
  role_id bigint unsigned NOT NULL,
  PRIMARY KEY user_role_pk_id(id),
  FOREIGN KEY user_role_fk_user_id(user_id) REFERENCES user(id),
  FOREIGN KEY user_role_fk_role_id(role_id) REFERENCES role(id),
  UNIQUE KEY user_role_uk_user_id_role_id(user_id, role_id)
);

insert into user(id,email,name,password,totp,phone,active,created_at,updated_at,deleted_at) values
(1,null,'Dealer one',NULL,NULL,'+9660501235678',true,'2023-02-01 06:33:27','2023-02-01 06:33:27',NULL),
(2,null,'Dealer two',NULL,NULL,'+9660544561234',true,'2023-02-01 06:33:27','2023-02-01 06:33:27',NULL),
(3,'admin1@yopmail.com','Admin User1','$2a$10$PfepXgY5zns5vCxtdGgnNuOAL7dHzxK9JeAAPyI8uolG2Pg.O99Gi',NULL,NULL,true,'2023-02-01 06:33:27','2023-02-01 06:33:27',NULL),
(4,'admin2@yopmail.com','Admin User2','$2a$10$PfepXgY5zns5vCxtdGgnNuOAL7dHzxK9JeAAPyI8uolG2Pg.O99Gi',NULL,NULL,true,'2023-02-01 06:33:27','2023-02-01 06:33:27',NULL);

insert into role(id,role) values
(1,'ADMIN'),
(2,'DEALER');

insert into user_role(user_id,role_id) values
(1,2),
(2,2),
(3,1),
(4,1);

ALTER table request drop foreign key request_FK;
ALTER TABLE request CHANGE dealer_id user_id bigint unsigned NULL;
alter table request add constraint request_fk_user_id foreign key (user_id) references user(id);

alter table dealer_plan drop foreign key dealer_plan_ibfk_1;
alter table dealer_plan add constraint dealer_plan_fk_dealer_id FOREIGN KEY(dealer_id) references user(id);
drop table dealer;
