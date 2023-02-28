
CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `totp` varchar(20) DEFAULT NULL,
  `phone_no` varchar(20) NOT NULL,
  `active` boolean
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ;

CREATE TABLE `role` (
  `id` bigint unsigned NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `user_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `role_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`),
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (role_id) REFERENCES role(id)
);

insert into user(id,email,name,password,totp,phone_no,active,created_at,updated_at,deleted_at)values
(1,'dummy@gmail.com','user1','us@1234',NULL,'+9660501235678',true,'2023-02-01 06:33:27','2023-02-01 06:33:27',NULL),
(2,'user@gmail.com','user2','user@1234',NULL,'+9660544561234',true,'2023-02-01 06:33:27','2023-02-01 06:33:27',NULL);

insert into role(id,role)values
(1,'ADMIN'),
(2,'DEALER');

insert into user_role(user_id,role_id)values
(1,1),
(2,2);
