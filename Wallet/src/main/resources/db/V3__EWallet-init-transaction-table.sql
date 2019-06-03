CREATE TABLE `transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `transaction_date` date DEFAULT NULL,
  `transaction_reference` varchar(255) NOT NULL,
  `transaction_type` int(11) NOT NULL,
  `account_id` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6g20fcr3bhr6bihgy24rq1r1b` (`account_id`),
  KEY `FKsg7jp0aj6qipr50856wf6vbw1` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci