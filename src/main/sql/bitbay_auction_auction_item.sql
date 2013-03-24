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
-- Table structure for table `auction_item`
--

DROP TABLE IF EXISTS `auction_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auction_item` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `cat_id` int(11) DEFAULT NULL,
  `seller_id` varchar(50) DEFAULT NULL,
  `title` varchar(80) DEFAULT NULL,
  `description` mediumtext,
  `image1` varchar(180) DEFAULT NULL COMMENT 'relative url',
  `image2` varchar(180) DEFAULT NULL,
  `image3` varchar(180) DEFAULT NULL,
  `image4` varchar(180) DEFAULT NULL,
  `image5` varchar(180) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `win_bidder_id` varchar(50) DEFAULT NULL,
  `payment` decimal(6,2) DEFAULT NULL,
  `pay_date` date DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `fk_cat_id` (`cat_id`),
  KEY `fk_seller_id` (`seller_id`),
  CONSTRAINT `fk_cat_id` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_seller_id` FOREIGN KEY (`seller_id`) REFERENCES `users` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction_item`
--

LOCK TABLES `auction_item` WRITE;
/*!40000 ALTER TABLE `auction_item` DISABLE KEYS */;
INSERT INTO `auction_item` VALUES (1,2,'admin@isp.com','My Favorite Logos','Fav logos are cool','/imgvault/guitar.jpg','','','','','2013-03-01 12:30:30','2013-03-15 10:35:33',NULL,NULL,NULL,NULL),(2,3,'admin@isp.com','Test Title','Test description','/imgvault/fender_amp.jpg',NULL,NULL,NULL,NULL,'2013-03-02 11:45:12','2013-03-16 13:23:44',NULL,NULL,NULL,NULL),(7,2,'admin@isp.com','Cool photos','now is the time<br/>','/imgvault/pic13.jpg',NULL,NULL,NULL,NULL,'2013-03-02 13:37:00','2013-03-16 00:00:00',NULL,NULL,NULL,NULL),(8,21,'member@isp.com','Wisconsin Golf Course on Beautiful Wooded Land','Enjoy this beautiful golf course on a wooded lot overlooking Lake Michigan in Wisconsin\'s upper peninsula. You love the views.','/imgvault/IMG_0631.jpg','/imgvault/IMG_0626.jpg','/imgvault/IMG_0629.jpg','http://localhost:8080/bitbay/imgvault/IMG_0633.jpg',NULL,'2013-03-03 21:15:00','2013-03-17 14:30:00',NULL,NULL,NULL,NULL),(9,29,'admin@isp.com','xxxxxxx','zxcxzczxcxc','/imgvault/pic6.jpg',NULL,NULL,NULL,NULL,'2013-03-04 11:18:00','2013-03-18 18:18:00',NULL,NULL,NULL,NULL),(10,24,'jlombardo@wi.rr.com','Blade 400 3D Helicopter','Used Blade 400 is in great condition except for a bend main shaft which is easy to replace. Comes with 3 extra batteries.<br/>','/imgvault/Blade_400_action1.jpg','/imgvault/Blade_400_components_500pics.jpg',NULL,NULL,NULL,'2013-02-08 09:32:00','2013-03-05 07:32:00','jlombardo@wi.rr.com',NULL,NULL,NULL);
/*!40000 ALTER TABLE `auction_item` ENABLE KEYS */;
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
