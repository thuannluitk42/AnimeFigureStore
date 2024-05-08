create database animefigurestore;
use animefigurestore;

CREATE TABLE `users` (
                         `user_id` int(11) NOT NULL AUTO_INCREMENT,
                         `username` varchar(50) NOT NULL,
                         `password` varchar(255) NOT NULL,
                         `fullname` nvarchar(255) NULL DEFAULT NULL ,
                         `address` nvarchar(255) null DEFAULT NULL ,
                         `phonenumber` varchar(12),
                         `email` varchar(255),
                         `dob` varchar(8),
                         `avatar` text,
                         `is_deleted` boolean,
                         `created_date` timestamp DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`user_id`)
);

CREATE TABLE `roles` (
                         `role_id` int(11) NOT NULL AUTO_INCREMENT,
                         `role_name` varchar(50) NOT NULL,
                         `roles_descripion` nvarchar(255)  NULL DEFAULT NULL ,
                         `is_deleted` boolean default false,
                         `created_date` timestamp DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`role_id`)
);

CREATE TABLE `users_roles` (
                         `user_id` int(11),
                         `role_id` int(11),
                         PRIMARY KEY (`user_id`,`role_id`)
);

CREATE TABLE `products` (
                         `product_id` int(11) NOT NULL AUTO_INCREMENT,
						 `product_name` nvarchar(255) NOT NULL,
						 `product_price` decimal NOT NULL,
                         `product_images` text NULL comment 'danh sach anh',
                         `product_quantity` int,
                         `product_description` text,
						 `discount` text comment 'gia tri giam gia 50%, 50000',
                         `category_id` int(11) NOT NULL,
                         `is_deleted` boolean default false,
                         `created_date` timestamp DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`product_id`)
);

CREATE TABLE `categories` (
                         `category_id` int(11) NOT NULL AUTO_INCREMENT,
						 `category_name` nvarchar(255)  NOT NULL,
                         `is_deleted` boolean default false,
                         `created_date` timestamp DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`category_id`)
);

CREATE TABLE `orders` (
                         `order_id` int(11) NOT NULL AUTO_INCREMENT,
						 `user_id` int(11),
                         `total` decimal,
						 `delivery_address` nvarchar(255) null DEFAULT NULL,
                         `payment_option` int comment '0: pay offline, 1: pay online',
                         `payment_status` int comment '0: pending, 1: success, 2: failed',
                         `vnpay_transaction_id` int comment 'vnp_TxnRef: ma giao dich ben vnpay',
                         `created_date` timestamp DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`order_id`)
);

CREATE TABLE `orders_detail` (
                         `order_id` int(11),
						 `product_id` int(11),
						 `unit_price;` decimal comment 'gia luc tinh tien',
                         `amount` int ,
                         `sub_total` timestamp DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`order_id`,`product_id`)
);




INSERT INTO `roles` (`role_name`,`roles_descripion`,`is_deleted`) VALUES ('admin','manage everything','0');
INSERT INTO `roles` (`role_name`,`roles_descripion`,`is_deleted`) VALUES ('salesperson','manage product price, customer, shipping, order and sales report','0');
INSERT INTO `roles` (`role_name`,`roles_descripion`,`is_deleted`) VALUES ('editor', 'manage categories, brands, product, artical, and menus','0');
INSERT INTO `roles` (`role_name`,`roles_descripion`,`is_deleted`) VALUES ('shipper', 'view product, view order, update order status','0');
INSERT INTO `roles` (`role_name`,`roles_descripion`,`is_deleted`) VALUES ('assistant', 'manage question and views','0');
INSERT INTO `roles` (`role_name`,`roles_descripion`,`is_deleted`) VALUES ('client', 'only order product and update user','0');






