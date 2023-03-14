-- salamdb.dealer definition

CREATE TABLE `dealer` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `totp` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`)
) ;


-- salamdb.jwt_refresh definition

CREATE TABLE `jwt_refresh` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` timestamp NOT NULL,
  `user_id` bigint NOT NULL,
  `username` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ;



-- salamdb.plan definition

CREATE TABLE `plan` (
  `id` bigint unsigned NOT NULL,
  `open_access_id` varchar(200) NOT NULL,
  `meta` json NOT NULL,
  PRIMARY KEY (`id`)
) ;



-- salamdb.dealer_plan definition

CREATE TABLE `dealer_plan` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `dealer_id` bigint unsigned NOT NULL,
  `plan_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `dealer_id` (`dealer_id`),
  KEY `plan_id` (`plan_id`),
  CONSTRAINT `dealer_plan_ibfk_1` FOREIGN KEY (`dealer_id`) REFERENCES `dealer` (`id`),
  CONSTRAINT `dealer_plan_ibfk_2` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`)
);


-- salamdb.request definition

CREATE TABLE `request` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `state` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `order_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dealer_id` bigint unsigned DEFAULT NULL,
  `meta` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `finished_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `request_FK` (`dealer_id`),
  CONSTRAINT `request_FK` FOREIGN KEY (`dealer_id`) REFERENCES `dealer` (`id`)
);


-- salamdb.transition definition

CREATE TABLE `transition` (
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

INSERT INTO dealer (id,name,phone,created_at,updated_at,deleted_at,totp) VALUES
	 (1,'Dealer one','123456789','2023-02-01 06:33:27','2023-02-01 06:33:27',NULL,NULL),
	 (2,'Dealer two','987654329','2023-02-01 06:33:27','2023-02-01 06:33:27',NULL,NULL);

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

INSERT INTO dealer_plan (dealer_id,plan_id) VALUES
	 (1,1903),
	 (1,2902);