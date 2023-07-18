CREATE TABLE IF NOT EXISTS user (
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

CREATE TABLE IF NOT EXISTS role (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  role varchar(50) NOT NULL,
  PRIMARY KEY role_pk_id(id),
  UNIQUE KEY role_uk_role(role)
);

CREATE TABLE IF NOT EXISTS user_role (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  user_id bigint unsigned NOT NULL,
  role_id bigint unsigned NOT NULL,
  PRIMARY KEY user_role_pk_id(id),
  FOREIGN KEY user_role_fk_user_id(user_id) REFERENCES user(id),
  FOREIGN KEY user_role_fk_role_id(role_id) REFERENCES role(id),
  UNIQUE KEY user_role_uk_user_id_role_id(user_id, role_id)
);

insert into user(id,email,name,password,totp,phone,active,created_at,updated_at,deleted_at) values
(1,'admin1@yopmail.com','Admin User1','$2a$10$ta4dq/lPKWZq56rMJep81.DPYm/EHOeSVePP5ZOA8tfbNckCQM4u2',NULL,NULL,true,'2023-02-01 06:33:27','2023-02-01 06:33:27',NULL),
(2,'admin2@yopmail.com','Admin User2','$2a$10$ta4dq/lPKWZq56rMJep81.DPYm/EHOeSVePP5ZOA8tfbNckCQM4u2',NULL,NULL,true,'2023-02-01 06:33:27','2023-02-01 06:33:27',NULL),
(3,'customer1@yopmail.com','Customer User2','$2a$10$ta4dq/lPKWZq56rMJep81.DPYm/EHOeSVePP5ZOA8tfbNckCQM4u2',NULL,'+9660501235679',true,'2023-02-01 06:33:27','2023-02-01 06:33:27',NULL);

insert into role(id,role) values
(1,'ADMIN'),
(2,'CUSTOMER');

insert into user_role(user_id,role_id) values
(1,1),
(2,1),
(3,2);

CREATE TABLE IF NOT EXISTS `jwt_refresh` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` timestamp NOT NULL,
  `user_id` bigint NOT NULL,
  `username` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ;



-- salamdb.plan definition

CREATE TABLE IF NOT EXISTS `plan` (
  `id` bigint unsigned NOT NULL,
  `open_access_id` varchar(200) NOT NULL,
  `meta` json NOT NULL,
  PRIMARY KEY (`id`)
) ;


-- salamdb.request definition

CREATE TABLE IF NOT EXISTS `request` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `state` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `order_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint unsigned DEFAULT NULL,
  `meta` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `finished_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `request_FK` (`user_id`),
  CONSTRAINT `request_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);


-- salamdb.transition definition

CREATE TABLE IF NOT EXISTS `transition` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `from` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `to` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_id` bigint unsigned NOT NULL,
  `payload` json NOT NULL,
  `created_at` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `transitions_request_id_foreign` (`request_id`),
  CONSTRAINT `transitions_request_id_foreign` FOREIGN KEY (`request_id`) REFERENCES `request` (`id`)
) ;


-- seed data

INSERT INTO plan (id,open_access_id,meta) VALUES
	 (1903,'9902','{"name": "Fiber Prepaid,240", "price": "1 SAR", "category": "ftth", "planType": "prepaid", "description": "Unlimited, 12 Months + 3 Months free", "uploadSpeed": "60", "downloadSpeed": "240"}'),
	 (2902,'2902','{"name": "Fiber Prepaid,100", "price": "1 SAR", "category": "ftth", "planType": "prepaid", "description": "Unlimited, 12 Months", "uploadSpeed": "25", "downloadSpeed": "100"}'),
	 (3103,'10102','{"name": "Fiber Prepaid,240", "price": "1 SAR/Month", "addons": [{"desc": "Orbit Family Pack Free Upon Order", "type": "orbit", "price": "Free", "title": "Orbit Family Plan"}], "category": "ftth", "planType": "postpaid", "description": "Unlimited", "uploadSpeed": "60", "downloadSpeed": "240"}'),
	 (3104,'10802','{"name": "Fiber Postpaid,100", "price": "1 SAR/Month", "category": "ftth", "planType": "postpaid", "description": "Unlimited", "uploadSpeed": "25", "downloadSpeed": "100"}'),
	 (3302,'3302','{"name": "Fiber Prepaid,500", "price": "1 SAR", "category": "ftth", "planType": "prepaid", "description": "Unlimited, 12 Months + 3 Months free", "uploadSpeed": "125", "downloadSpeed": "500"}'),
	 (3802,'8508','{"name": "Fiber Postpaid,500", "price": "1 SAR/Month", "addons": [{"desc": "Orbit Family Pack Free Upon Order", "type": "orbit", "price": "Free", "title": "Orbit Family Plan"}], "category": "ftth", "planType": "postpaid", "description": "Unlimited", "uploadSpeed": "125", "downloadSpeed": "500"}'),
	 (4403,'2902','{"name": "Fiber Prepaid,100", "price": "1 SAR", "category": "ftth", "planType": "prepaid", "description": "Unlimited, 12 Months", "uploadSpeed": "25", "downloadSpeed": "100"}'),
	 (4703,'3302','{"name": "Fiber Prepaid,500", "price": "1 SAR", "category": "ftth", "planType": "prepaid", "description": "Unlimited, 12 Months + 3 Months free", "uploadSpeed": "125", "downloadSpeed": "500"}'),
	 (4903,'3302','{"name": "Fiber Prepaid,1000", "price": "1 SAR", "category": "ftth", "planType": "prepaid", "description": "Unlimited, 12 Months + 3 Months free", "uploadSpeed": "250", "downloadSpeed": "930"}'),
	 (5003,'8508','{"name": "Fiber Postpaid,1000", "price": "1 SAR/Month", "addons": [{"desc": "Orbit Family Pack Free Upon Order", "type": "orbit", "price": "Free", "title": "Orbit Family Plan"}], "category": "ftth", "planType": "postpaid", "description": "Unlimited", "uploadSpeed": "250", "downloadSpeed": "930"}');
INSERT INTO plan (id,open_access_id,meta) VALUES
	 (8508,'8508','{"name": "Fiber Postpaid,500", "price": "1 SAR/Month", "category": "ftth", "planType": "postpaid", "description": "Unlimited", "uploadSpeed": "125", "downloadSpeed": "500"}'),
	 (9902,'9902','{"name": "Fiber Prepaid,240", "price": "1 SAR", "category": "ftth", "planType": "prepaid", "description": "Unlimited, 12 Months + 3 Months free", "uploadSpeed": "60", "downloadSpeed": "240"}'),
	 (10102,'10102','{"name": "Fiber Prepaid,240", "price": "1 SAR/Month", "category": "ftth", "planType": "postpaid", "description": "Unlimited", "uploadSpeed": "60", "downloadSpeed": "240"}'),
	 (10802,'10802','{"name": "Fiber Prepaid,100", "price": "1 SAR", "category": "ftth", "planType": "prepaid", "description": "Unlimited, 12 Months", "uploadSpeed": "25", "downloadSpeed": "100"}');
