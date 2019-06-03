CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` bigint(20) NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_name` varchar(255) NOT NULL,
  `account_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc3b4xfbq6rbkkrddsdum8t5f0` (`account_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci