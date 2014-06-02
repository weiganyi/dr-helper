-- MySQL dump 10.13  Distrib 5.5.33, for Linux (i686)
--
-- Host: localhost    Database: drhelper
-- ------------------------------------------------------
-- Server version	5.5.33-log

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
-- Table structure for table `dr_menu`
--

DROP TABLE IF EXISTS `dr_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dr_menu` (
  `menu_id` int(4) NOT NULL AUTO_INCREMENT,
  `menu_name` char(255) NOT NULL,
  `menu_price` int(16) NOT NULL,
  `menu_type_id` int(4) NOT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dr_menu`
--

LOCK TABLES `dr_menu` WRITE;
/*!40000 ALTER TABLE `dr_menu` DISABLE KEYS */;
INSERT INTO `dr_menu` VALUES (1,'香芹香干肉丝',12,2),(2,'宫保鸡丁',15,2),(3,'馒头',5,1),(4,'皮蛋瘦肉粥',8,1),(5,'双皮奶',6,4),(6,'凉拌三丝',3,3),(7,'紫菜蛋汤',8,5),(8,'米饭',2,1);
/*!40000 ALTER TABLE `dr_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dr_menu_type`
--

DROP TABLE IF EXISTS `dr_menu_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dr_menu_type` (
  `menu_type_id` int(4) NOT NULL AUTO_INCREMENT,
  `menu_type_name` char(255) NOT NULL,
  PRIMARY KEY (`menu_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dr_menu_type`
--

LOCK TABLES `dr_menu_type` WRITE;
/*!40000 ALTER TABLE `dr_menu_type` DISABLE KEYS */;
INSERT INTO `dr_menu_type` VALUES (1,'主食'),(2,'炒菜'),(3,'凉菜'),(4,'甜点'),(5,'汤');
/*!40000 ALTER TABLE `dr_menu_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dr_option`
--

DROP TABLE IF EXISTS `dr_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dr_option` (
  `option_id` int(4) NOT NULL AUTO_INCREMENT,
  `option_name` char(255) NOT NULL,
  `option_value` char(255) NOT NULL,
  PRIMARY KEY (`option_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dr_option`
--

LOCK TABLES `dr_option` WRITE;
/*!40000 ALTER TABLE `dr_option` DISABLE KEYS */;
INSERT INTO `dr_option` VALUES (1,'web_name','餐厅助手');
/*!40000 ALTER TABLE `dr_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dr_table`
--

DROP TABLE IF EXISTS `dr_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dr_table` (
  `table_id` int(4) NOT NULL AUTO_INCREMENT,
  `table_num` int(4) NOT NULL,
  `table_seat_num` int(4) NOT NULL,
  `table_empty` int(1) NOT NULL,
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dr_table`
--

LOCK TABLES `dr_table` WRITE;
/*!40000 ALTER TABLE `dr_table` DISABLE KEYS */;
INSERT INTO `dr_table` VALUES (1,1,4,1),(2,2,4,1),(3,3,4,1),(4,4,4,1),(5,5,8,1),(6,6,8,1);
/*!40000 ALTER TABLE `dr_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dr_user`
--

DROP TABLE IF EXISTS `dr_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dr_user` (
  `user_id` int(4) NOT NULL AUTO_INCREMENT,
  `user_name` char(255) NOT NULL,
  `user_passwd` char(255) NOT NULL,
  `user_auth` enum('waiter','chef','admin') NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dr_user`
--

LOCK TABLES `dr_user` WRITE;
/*!40000 ALTER TABLE `dr_user` DISABLE KEYS */;
INSERT INTO `dr_user` VALUES (1,'weigy','123456','waiter'),(2,'hongj','123456','chef'),(3,'admin','123456','admin');
/*!40000 ALTER TABLE `dr_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'drhelper'
--
/*!50003 DROP PROCEDURE IF EXISTS `union_table` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`drhelper`@`localhost` PROCEDURE `union_table`(in table1 int, in table2 int)
begin
    declare table_empty1, table_empty2 int;

    select table_empty from dr_table where table_num=table1 into table_empty1;
    select table_empty from dr_table where table_num=table2 into table_empty2;

    if table_empty1=0 then
        if table_empty2=0 then
            update dr_table set table_empty=1 where table_num=table1;
        end if;
    end if;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-06-02 21:12:48
