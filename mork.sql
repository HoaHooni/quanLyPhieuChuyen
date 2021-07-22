-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: localhost    Database: sapo1
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` text,
  `create_at` date DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'category001','Quần áo nam','quần áo nam',NULL,NULL),(2,'category002','Quần áo nữ','Quần áo nữ',NULL,NULL),(3,'category003','Quần áo trẻ nam','Quần áo nữ',NULL,NULL),(4,'category004','Quần áo trẻ nữ','Quần áo nữ',NULL,NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventories`
--

DROP TABLE IF EXISTS `inventories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone` char(20) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `create_at` date DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventories`
--

LOCK TABLES `inventories` WRITE;
/*!40000 ALTER TABLE `inventories` DISABLE KEYS */;
INSERT INTO `inventories` VALUES (1,'inventory001','kho 1','Nam Từ liêm - Hà Nội','113',NULL,NULL,NULL),(2,'inventory002','kho 2','Bắc Từ Liêm - Hà Nội','113',NULL,NULL,NULL),(3,'inventory003','kho 3','Cầu Giấy - Hồ Nội','113',NULL,NULL,NULL);
/*!40000 ALTER TABLE `inventories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_inventory`
--

DROP TABLE IF EXISTS `product_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_inventory` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `inventory_id` int NOT NULL,
  `total` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `forignkey_product_idx` (`product_id`),
  KEY `forignkey_invetory_idx` (`inventory_id`),
  CONSTRAINT `forignkey_invetory` FOREIGN KEY (`inventory_id`) REFERENCES `inventories` (`id`),
  CONSTRAINT `forignkey_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_inventory`
--

LOCK TABLES `product_inventory` WRITE;
/*!40000 ALTER TABLE `product_inventory` DISABLE KEYS */;
INSERT INTO `product_inventory` VALUES (4,6,1,'19'),(5,2,2,'14'),(6,1,3,'22'),(7,1,3,'33'),(8,2,1,'44'),(9,2,3,'43'),(10,2,2,'27'),(11,4,3,'45'),(12,1,3,'9'),(13,5,2,'10'),(14,3,2,'27'),(15,6,2,'5'),(16,6,1,'25'),(17,4,2,'43'),(18,4,1,'21'),(19,3,1,'3'),(20,6,2,'35'),(21,2,3,'19'),(22,4,3,'35'),(23,5,1,'28'),(24,4,1,'36'),(25,6,1,'14'),(26,6,2,'32'),(27,1,1,'28');
/*!40000 ALTER TABLE `product_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `category_id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` text,
  `size` char(5) NOT NULL,
  `color` varchar(50) NOT NULL,
  `cost` decimal(17,2) NOT NULL,
  `link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_at` date DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `forignkey_product_category_idx` (`category_id`),
  CONSTRAINT `forignkey_product_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'product001',1,'Quần âu','quần âu','39','đen',300000.00,NULL,NULL,NULL),(2,'product002',2,'Áo tứ thân','Áo tứ thân','39','đen',300000.00,NULL,NULL,NULL),(3,'product003',3,'Quần âu','Quần âu','39','đen',300000.00,NULL,NULL,NULL),(4,'product004',4,'Váy trẻ nhỏ','Váy trẻ nhỏ','39','đen',300000.00,NULL,NULL,NULL),(5,'product005',1,'Áo sơ mi trắng','Áo sơ mi trắng','39','đen',300000.00,NULL,NULL,NULL),(6,'product006',2,'Váy','Váy','39','đen',300000.00,NULL,NULL,NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_MANAGER'),(2,'ROLE_INVENTORIER'),(3,'ROLE_COORDINATOR');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer`
--

DROP TABLE IF EXISTS `transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transfer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `inventory_input_id` int NOT NULL,
  `inventory_output_id` int NOT NULL,
  `note` text,
  `create_at` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `foreginkey_user_idx` (`user_id`),
  KEY `foreginkey_inventory_input_idx` (`inventory_input_id`),
  KEY `foreginkey_inventory_output_idx` (`inventory_output_id`),
  CONSTRAINT `foreginkey_inventory_input` FOREIGN KEY (`inventory_input_id`) REFERENCES `inventories` (`id`),
  CONSTRAINT `foreginkey_inventory_output` FOREIGN KEY (`inventory_output_id`) REFERENCES `inventories` (`id`),
  CONSTRAINT `foreginkey_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transfer`
--

LOCK TABLES `transfer` WRITE;
/*!40000 ALTER TABLE `transfer` DISABLE KEYS */;
INSERT INTO `transfer` VALUES (11,1,1,3,'gấp',NULL),(12,2,2,2,'gấp',NULL),(13,3,3,1,'gấp',NULL),(14,1,1,3,'gấp',NULL),(15,2,2,2,'gấp',NULL);
/*!40000 ALTER TABLE `transfer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer_product`
--

DROP TABLE IF EXISTS `transfer_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transfer_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `transfer_id` int NOT NULL,
  `product_id` int NOT NULL,
  `total` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `foreignkey_transfer_idx` (`transfer_id`),
  KEY `foreignkey_product_idx` (`product_id`),
  CONSTRAINT `foreignkey_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `foreignkey_transfer` FOREIGN KEY (`transfer_id`) REFERENCES `transfer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transfer_product`
--

LOCK TABLES `transfer_product` WRITE;
/*!40000 ALTER TABLE `transfer_product` DISABLE KEYS */;
INSERT INTO `transfer_product` VALUES (6,14,3,34),(7,13,5,43),(8,13,3,41),(9,11,4,30),(10,13,6,9),(11,15,2,12),(12,14,2,30),(13,12,4,1),(14,12,3,13),(15,11,3,2),(16,15,2,24),(17,15,1,17),(18,14,4,19),(19,13,2,36),(20,11,3,13),(21,12,1,17),(22,13,2,26),(23,12,4,1),(24,12,6,12),(25,14,2,36),(26,15,6,10),(27,12,3,17),(28,14,6,41),(29,14,3,8),(30,14,5,30),(31,11,2,12);
/*!40000 ALTER TABLE `transfer_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_foreginkey_idx` (`role_id`),
  CONSTRAINT `role_foreginkey` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `user_foreginkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` char(20) NOT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dob` date NOT NULL,
  `status` tinyint NOT NULL,
  `create_at` date DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'use001','user001','abc@123','user001','113','Ha Noi','1999-06-15',1,NULL,NULL),(2,'use002','user002','abc@123','user001','113','Ha Noi','1999-06-15',1,NULL,NULL),(3,'use003','user003','abc@123','user001','113','Ha Noi','1999-06-15',1,NULL,NULL),(4,'use004','user004','abc@123','user001','113','Ha Noi','1999-06-15',1,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-23 16:59:05
