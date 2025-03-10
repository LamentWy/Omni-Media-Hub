-- MySQL dump 10.13  Distrib 8.3.0, for macos12.6 (x86_64)
--
-- Host: localhost    Database: OmniDB
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Book`
--

DROP TABLE IF EXISTS `Book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Book` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `author` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `translator` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `publisher` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_zh_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Book`
--

LOCK TABLES `Book` WRITE;
/*!40000 ALTER TABLE `Book` DISABLE KEYS */;
/*!40000 ALTER TABLE `Book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Resource_Collection`
--

DROP TABLE IF EXISTS `Resource_Collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Resource_Collection` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `visible` tinyint DEFAULT '0' COMMENT '0 private, 1 public',
  `created_by` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_zh_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `Resource_Relation`
--

DROP TABLE IF EXISTS `Resource_Relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Resource_Relation` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `c_id` int unsigned NOT NULL COMMENT 'collection id',
  `v_id` int unsigned DEFAULT NULL COMMENT 'video id',
  `a_id` int unsigned DEFAULT NULL COMMENT 'audio id',
  `b_id` int unsigned DEFAULT NULL COMMENT 'book id',
  `p_id` int unsigned DEFAULT NULL COMMENT 'pic id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_zh_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) COLLATE utf8mb4_zh_0900_as_cs NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_zh_0900_as_cs NOT NULL,
  `activated` tinyint DEFAULT '0' COMMENT '1 for active,0 not ',
  `created_date` datetime NOT NULL,
  `created_by` varchar(255) COLLATE utf8mb4_zh_0900_as_cs NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `last_modified_by` varchar(255) COLLATE utf8mb4_zh_0900_as_cs NOT NULL,
  `nick_name` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT '家庭成员' COMMENT 'name for show',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_zh_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'admin','$2a$10$wLoE6HqyR0Hs.nn.XQia4OyLjfS57Bv1fbbORifVhRR.LDwUnDRWi',1,'2024-12-21 14:29:30','SYSTEM','2024-12-24 19:55:33','SYSTEM','默认管理员');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_Role`
--

DROP TABLE IF EXISTS `User_Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User_Role` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned NOT NULL,
  `role_name` varchar(255) COLLATE utf8mb4_zh_0900_as_cs NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_zh_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_Role`
--

LOCK TABLES `User_Role` WRITE;
/*!40000 ALTER TABLE `User_Role` DISABLE KEYS */;
INSERT INTO `User_Role` VALUES (1,1,'ROLE_ADMIN');
/*!40000 ALTER TABLE `User_Role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Video`
--

DROP TABLE IF EXISTS `Video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Video` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `file_name` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `file_path` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `cover` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  `desc` varchar(255) COLLATE utf8mb4_zh_0900_as_cs DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_zh_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-07 22:36:17
