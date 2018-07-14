-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: fdroid
-- ------------------------------------------------------
-- Server version	5.7.16-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `api_class`
--

DROP TABLE IF EXISTS `api_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_class` (
  `api_class_id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `author` varchar(45) DEFAULT NULL,
  `first_version` varchar(45) DEFAULT NULL,
  `extend_class` int(11) DEFAULT NULL,
  PRIMARY KEY (`api_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_class`
--

LOCK TABLES `api_class` WRITE;
/*!40000 ALTER TABLE `api_class` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_library`
--

DROP TABLE IF EXISTS `api_library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_library` (
  `library_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `orgnization` varchar(255) DEFAULT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  `version` varchar(45) DEFAULT NULL,
  `jdk_version` varchar(45) DEFAULT NULL,
  `pom_file` varchar(45) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `doc_website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`library_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_library`
--

LOCK TABLES `api_library` WRITE;
/*!40000 ALTER TABLE `api_library` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_library` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_method`
--

DROP TABLE IF EXISTS `api_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_method` (
  `api_method_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `annotation` varchar(255) DEFAULT NULL,
  `return_type` varchar(45) NOT NULL,
  `first_version` varchar(45) DEFAULT NULL,
  `is_static` tinyint(1) DEFAULT NULL,
  `class_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`api_method_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_method`
--

LOCK TABLES `api_method` WRITE;
/*!40000 ALTER TABLE `api_method` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_parameter`
--

DROP TABLE IF EXISTS `api_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_parameter` (
  `parameter_id` int(11) NOT NULL AUTO_INCREMENT,
  `class_id` int(11) DEFAULT NULL,
  `method_id` int(11) DEFAULT NULL,
  `type_class` int(11) DEFAULT NULL,
  `type_string` varchar(45) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `first_version` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`parameter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_parameter`
--

LOCK TABLES `api_parameter` WRITE;
/*!40000 ALTER TABLE `api_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_parameter` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-03 18:19:33
