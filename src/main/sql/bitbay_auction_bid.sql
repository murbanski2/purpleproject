CREATE DATABASE  IF NOT EXISTS `bitbay_auction` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bitbay_auction`;
-- MySQL dump 10.13  Distrib 5.1.34, for apple-darwin9.5.0 (i386)
--
-- Host: 127.0.0.1    Database: bitbay_auction
-- ------------------------------------------------------
-- Server version	5.5.13

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
-- Table structure for table `bid`
--

DROP TABLE IF EXISTS `bid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bid` (
  `bid_id` int(11) NOT NULL AUTO_INCREMENT,
  `bidder_id` varchar(50) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `amount` decimal(6,2) DEFAULT NULL,
  PRIMARY KEY (`bid_id`),
  KEY `fk_bidder_id` (`bidder_id`),
  KEY `fk_item_id` (`item_id`),
  CONSTRAINT `fk_bidder_id` FOREIGN KEY (`bidder_id`) REFERENCES `users` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_id` FOREIGN KEY (`item_id`) REFERENCES `auction_item` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bid`
--

LOCK TABLES `bid` WRITE;
/*!40000 ALTER TABLE `bid` DISABLE KEYS */;
INSERT INTO `bid` VALUES (1,'admin@isp.com',1,'0.25'),(2,'member@isp.com',1,'2.95'),(3,'admin@isp.com',1,'3.40'),(4,'member@isp.com',8,'100.00'),(5,'admin@isp.com',2,'0.25'),(6,'admin@isp.com',8,'101.00'),(7,'admin@isp.com',8,'102.00'),(8,'admin@isp.com',8,'103.00'),(9,'admin@isp.com',8,'104.00'),(10,'admin@isp.com',8,'105.00'),(11,'admin@isp.com',8,'106.00'),(12,'admin@isp.com',8,'107.00'),(13,'admin@isp.com',8,'108.00'),(14,'admin@isp.com',8,'109.00'),(15,'admin@isp.com',8,'110.00'),(16,'admin@isp.com',8,'111.00'),(17,'admin@isp.com',8,'112.00'),(18,'admin@isp.com',8,'113.00'),(19,'admin@isp.com',2,'50.00'),(20,'admin@isp.com',7,'1.00'),(21,'admin@isp.com',7,'2.00'),(22,'admin@isp.com',7,'2.00'),(23,'admin@isp.com',7,'3.00'),(24,'admin@isp.com',7,'3.00'),(25,'admin@isp.com',7,'4.00'),(26,'admin@isp.com',7,'5.00'),(27,'admin@isp.com',7,'6.00'),(28,'admin@isp.com',8,'114.00'),(29,'admin@isp.com',7,'7.00'),(30,'admin@isp.com',7,'7.10'),(31,'admin@isp.com',8,'115.00'),(32,'admin@isp.com',7,'7.20'),(33,'admin@isp.com',7,'7.30'),(34,'admin@isp.com',7,'7.40'),(35,'admin@isp.com',7,'7.50'),(36,'admin@isp.com',1,'3.50'),(37,'member@isp.com',1,'3.60'),(38,'member@isp.com',1,'3.70'),(39,'admin@isp.com',1,'3.80'),(40,'admin@isp.com',1,'4.10'),(41,'admin@isp.com',1,'4.20'),(42,'member@isp.com',1,'4.30'),(43,'admin@isp.com',1,'4.40'),(44,'member@isp.com',9,'0.50'),(45,'member@isp.com',9,'0.60'),(46,'member@isp.com',10,'5.00'),(47,'admin@isp.com',10,'5.20'),(48,'member@isp.com',10,'6.00'),(49,'admin@isp.com',7,'7.90'),(50,'admin@isp.com',10,'6.20'),(51,'admin@isp.com',10,'6.30'),(52,'admin@isp.com',10,'6.50'),(53,'admin@isp.com',1,'4.50'),(54,'admin@isp.com',10,'6.70'),(55,'admin@isp.com',10,'6.80'),(56,'admin@isp.com',10,'6.90'),(57,'admin@isp.com',10,'7.10'),(58,'admin@isp.com',10,'7.20'),(59,'admin@isp.com',10,'7.30'),(60,'admin@isp.com',10,'7.40'),(61,'admin@isp.com',10,'7.50'),(62,'admin@isp.com',10,'7.80'),(63,'admin@isp.com',10,'7.90'),(64,'admin@isp.com',10,'8.00'),(65,'member@isp.com',9,'1.25'),(66,'member@isp.com',9,'1.40'),(67,'admin@isp.com',9,'1.50'),(68,'member@isp.com',9,'1.60'),(69,'member@isp.com',9,'1.70'),(70,'member@isp.com',9,'1.80'),(71,'admin@isp.com',9,'1.90'),(72,'admin@isp.com',9,'2.00'),(73,'admin@isp.com',9,'2.10'),(74,'admin@isp.com',9,'2.20'),(75,'admin@isp.com',9,'2.30'),(76,'admin@isp.com',9,'2.40'),(77,'admin@isp.com',9,'2.50'),(78,'admin@isp.com',9,'2.60'),(79,'member@isp.com',9,'2.70'),(80,'admin@isp.com',9,'2.80'),(81,'member@isp.com',9,'2.90'),(82,'admin@isp.com',9,'3.00'),(83,'admin@isp.com',10,'8.10'),(84,'member@isp.com',10,'8.50'),(85,'member@isp.com',10,'8.60'),(86,'admin@isp.com',9,'3.10'),(87,'admin@isp.com',9,'3.20'),(88,'admin@isp.com',9,'3.40'),(89,'admin@isp.com',9,'3.50'),(90,'admin@isp.com',9,'3.60'),(91,'admin@isp.com',9,'3.70'),(92,'admin@isp.com',9,'3.80'),(93,'admin@isp.com',9,'3.90'),(94,'admin@isp.com',9,'4.00'),(95,'admin@isp.com',9,'4.10'),(96,'admin@isp.com',9,'4.20'),(97,'admin@isp.com',9,'4.30'),(98,'admin@isp.com',9,'4.40'),(99,'admin@isp.com',9,'4.50'),(100,'admin@isp.com',9,'4.60'),(101,'admin@isp.com',9,'4.70'),(102,'admin@isp.com',9,'4.80'),(103,'admin@isp.com',9,'4.90'),(104,'admin@isp.com',9,'5.00'),(105,'admin@isp.com',9,'5.10'),(106,'admin@isp.com',9,'5.20'),(107,'admin@isp.com',9,'5.30'),(108,'admin@isp.com',9,'5.40'),(109,'admin@isp.com',9,'5.50'),(110,'admin@isp.com',9,'5.60'),(111,'admin@isp.com',9,'5.70'),(112,'admin@isp.com',9,'5.80'),(113,'admin@isp.com',9,'5.90'),(114,'admin@isp.com',9,'6.00'),(115,'admin@isp.com',9,'6.10'),(116,'admin@isp.com',9,'6.20'),(117,'admin@isp.com',9,'6.30'),(118,'admin@isp.com',9,'6.40'),(119,'admin@isp.com',9,'6.50'),(120,'admin@isp.com',9,'6.60'),(121,'admin@isp.com',9,'6.70'),(122,'admin@isp.com',9,'6.80'),(123,'admin@isp.com',9,'6.90'),(124,'admin@isp.com',9,'7.00'),(125,'admin@isp.com',9,'7.10'),(126,'admin@isp.com',9,'7.20'),(127,'admin@isp.com',9,'7.30'),(128,'admin@isp.com',9,'7.40'),(129,'admin@isp.com',9,'7.50'),(130,'admin@isp.com',9,'7.60'),(131,'admin@isp.com',9,'7.70'),(132,'admin@isp.com',9,'7.90'),(133,'admin@isp.com',9,'8.00'),(134,'member@isp.com',9,'8.10'),(135,'admin@isp.com',9,'8.20'),(136,'admin@isp.com',9,'8.30'),(137,'admin@isp.com',9,'8.50'),(138,'admin@isp.com',9,'8.60'),(139,'admin@isp.com',9,'8.70'),(140,'admin@isp.com',9,'8.80'),(141,'admin@isp.com',9,'8.90'),(142,'admin@isp.com',9,'9.00'),(143,'member@isp.com',9,'9.10'),(144,'admin@isp.com',9,'9.10'),(145,'admin@isp.com',9,'9.20'),(146,'member@isp.com',9,'9.30'),(147,'admin@isp.com',9,'9.30'),(148,'admin@isp.com',9,'9.40'),(149,'admin@isp.com',9,'9.50'),(150,'admin@isp.com',9,'9.60'),(151,'member@isp.com',9,'9.70'),(152,'member@isp.com',9,'9.80'),(153,'admin@isp.com',9,'9.90'),(154,'admin@isp.com',9,'10.00'),(155,'admin@isp.com',9,'10.10'),(156,'member@isp.com',9,'10.20'),(157,'admin@isp.com',9,'10.30'),(158,'admin@isp.com',9,'10.40'),(159,'admin@isp.com',9,'10.50'),(160,'admin@isp.com',9,'10.60'),(161,'admin@isp.com',9,'10.70'),(162,'admin@isp.com',9,'10.80'),(163,'admin@isp.com',9,'10.90'),(164,'admin@isp.com',9,'11.00'),(165,'admin@isp.com',9,'11.10'),(166,'admin@isp.com',9,'11.20'),(167,'admin@isp.com',9,'11.30'),(168,'admin@isp.com',9,'11.40'),(169,'admin@isp.com',9,'11.50'),(170,'admin@isp.com',9,'11.60'),(171,'admin@isp.com',9,'11.70'),(172,'admin@isp.com',9,'11.80'),(173,'admin@isp.com',9,'11.90'),(174,'admin@isp.com',9,'12.00'),(175,'admin@isp.com',9,'12.10'),(176,'admin@isp.com',9,'12.20'),(177,'admin@isp.com',9,'12.40'),(178,'admin@isp.com',9,'12.50'),(179,'admin@isp.com',9,'12.60'),(180,'admin@isp.com',9,'12.70'),(181,'admin@isp.com',9,'12.80'),(182,'admin@isp.com',9,'12.90'),(183,'admin@isp.com',9,'13.00'),(184,'admin@isp.com',9,'13.10'),(185,'admin@isp.com',9,'13.20'),(186,'admin@isp.com',9,'13.30'),(187,'admin@isp.com',9,'13.40'),(188,'admin@isp.com',9,'13.50'),(189,'admin@isp.com',9,'13.60'),(190,'admin@isp.com',9,'13.70'),(191,'admin@isp.com',9,'13.80'),(192,'admin@isp.com',9,'14.10'),(193,'admin@isp.com',9,'14.20'),(194,'admin@isp.com',9,'14.30'),(195,'admin@isp.com',9,'14.40'),(196,'admin@isp.com',9,'14.50'),(197,'admin@isp.com',9,'14.60'),(198,'member@isp.com',9,'14.70'),(199,'admin@isp.com',9,'14.80'),(200,'admin@isp.com',9,'14.90'),(201,'admin@isp.com',9,'15.00'),(202,'admin@isp.com',9,'15.10'),(203,'admin@isp.com',9,'15.20'),(204,'admin@isp.com',9,'15.30'),(205,'admin@isp.com',9,'15.50'),(206,'admin@isp.com',9,'15.60'),(207,'admin@isp.com',9,'15.70'),(208,'admin@isp.com',9,'15.80'),(209,'admin@isp.com',9,'15.90'),(210,'admin@isp.com',10,'8.70'),(211,'admin@isp.com',9,'16.00'),(212,'admin@isp.com',9,'16.10'),(213,'admin@isp.com',9,'16.20'),(214,'admin@isp.com',9,'16.30'),(215,'admin@isp.com',9,'16.40'),(216,'admin@isp.com',9,'16.50'),(217,'admin@isp.com',9,'16.60'),(218,'admin@isp.com',9,'16.70'),(219,'admin@isp.com',9,'16.80'),(220,'admin@isp.com',9,'16.90'),(221,'admin@isp.com',9,'17.00'),(222,'admin@isp.com',9,'17.10'),(223,'admin@isp.com',9,'17.20'),(224,'admin@isp.com',9,'17.30'),(225,'admin@isp.com',9,'17.40'),(226,'admin@isp.com',9,'17.50'),(227,'admin@isp.com',9,'17.60'),(228,'admin@isp.com',9,'17.70'),(229,'admin@isp.com',9,'17.80'),(230,'admin@isp.com',9,'17.90'),(231,'admin@isp.com',9,'18.00'),(232,'admin@isp.com',9,'18.10'),(233,'admin@isp.com',9,'18.20'),(234,'admin@isp.com',9,'18.30');
/*!40000 ALTER TABLE `bid` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-03-05 15:55:03
