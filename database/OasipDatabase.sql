CREATE DATABASE  IF NOT EXISTS `oasip` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `oasip`;
-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: oasip
-- ------------------------------------------------------
-- Server version	8.0.26

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
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `eventID` int NOT NULL AUTO_INCREMENT,
  `bookingName` varchar(100) NOT NULL,
  `bookingEmail` varchar(100) NOT NULL,
  `eventStartTime` datetime NOT NULL,
  `eventDuration` int NOT NULL,
  `eventNotes` varchar(500) DEFAULT NULL,
  `eventCategoryID` int NOT NULL,
  PRIMARY KEY (`eventID`),
  KEY `fk_Event_EventCategory1_idx` (`eventCategoryID`),
  CONSTRAINT `fk_Event_EventCategory1` FOREIGN KEY (`eventCategoryID`) REFERENCES `eventcategory` (`eventCategoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'Somchai Jaidee(OR-7)','somchai.jai@mail.kmutt.ac.th','2022-05-23 13:30:00',30,'',2),(2,'Somsri Rakdee(SJ-3)','somsri.rak@mail.kmutt.ac.th','2022-04-27 09:30:00',30,'ขอปรึกษาปัญหาเพื่อนไม่ช่วยงาน',1),(3,'สมเกียรติ ขยันเรียน กลุ่ม TT-4','somkiat.kay@kmutt.ac.th','2022-05-23 16:30:05',15,'',3);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eventcategory`
--

DROP TABLE IF EXISTS `eventcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eventcategory` (
  `eventCategoryID` int NOT NULL,
  `eventCategoryName` varchar(100) NOT NULL,
  `eventCategoryDescription` varchar(500) DEFAULT NULL,
  `eventDuration` int NOT NULL,
  PRIMARY KEY (`eventCategoryID`),
  UNIQUE KEY `eventCategoryName_UNIQUE` (`eventCategoryName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eventcategory`
--

LOCK TABLES `eventcategory` WRITE;
/*!40000 ALTER TABLE `eventcategory` DISABLE KEYS */;
INSERT INTO `eventcategory` VALUES (1,'Project Management Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 integrated project I ให้นักศึกษาเตรียมเอกสารที่เกี่ยวข้องเพื่อแสดงระหว่างขอคำปรึกษา',30),(2,' DevOps/Infra Clinic','Use this event category for DevOps/Infra clinic.',20),(3,' Database Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย database clinic ในวิชา INT221 integrated project I',15),(4,' Client-side Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย client-side clinic ในวิชา INT221 integrated project I',30),(5,' Server-side Clinic','',30);
/*!40000 ALTER TABLE `eventcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eventcategoryowner`
--

DROP TABLE IF EXISTS `eventcategoryowner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eventcategoryowner` (
  `EventCategoryOwnerID` int NOT NULL,
  `eventCategoryID` int NOT NULL,
  `userID` int NOT NULL,
  PRIMARY KEY (`EventCategoryOwnerID`,`eventCategoryID`,`userID`),
  KEY `fk_EventCategory_has_User_User1_idx` (`userID`),
  KEY `fk_EventCategory_has_User_EventCategory1_idx` (`eventCategoryID`),
  CONSTRAINT `fk_EventCategory_has_User_EventCategory1` FOREIGN KEY (`eventCategoryID`) REFERENCES `eventcategory` (`eventCategoryID`),
  CONSTRAINT `fk_EventCategory_has_User_User1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eventcategoryowner`
--

LOCK TABLES `eventcategoryowner` WRITE;
/*!40000 ALTER TABLE `eventcategoryowner` DISABLE KEYS */;
INSERT INTO `eventcategoryowner` VALUES (1,1,2),(2,2,2),(5,5,2),(5,5,3),(4,4,4),(2,2,5),(3,3,6);
/*!40000 ALTER TABLE `eventcategoryowner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('admin','lecturer','student') NOT NULL,
  `createdOn` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedOn` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'OASIP ADMIN','oasip.admin@kmutt.ac.th','$argon2id$v=19$m=4096,t=3,p=1$sYXzbUOqBoHY1NfhJ8cjnw$H6+adWySiFPgcUogJK3hEhcF6Y4fusy7tcXYEL+f0cQ','admin','2022-08-01 00:00:00','2022-08-01 00:00:00'),(2,'Olarn Rojanapornpun','olarn.roj@kmutt.ac.th','$argon2id$v=19$m=4096,t=3,p=1$Sx7y2jxKZSjpWUV4srd8eg$AMH09iFiPQgAZ00cAdN3Gucqfhx2kRo3tQbHeLSR0RE','lecturer','2022-08-08 15:00:00','2022-08-08 15:00:00'),(3,'Pichet Limvachiranan','pichet.limv@kmutt.ac.th','$argon2id$v=19$m=4096,t=3,p=1$46EB43gQ46Z1/EmdqxtKNA$7m6cWGO2iDlFl/ETDYuYf+ArnSjRnsNwXLIP18DTYQY','lecturer','2022-08-08 15:00:01','2022-08-08 15:00:01'),(4,'Umaporn Supasitthimethee','umaporn.sup@kmutt.ac.th','$argon2id$v=19$m=4096,t=3,p=1$1Z2UK1zC76FIQeLH54GVAQ$qfXcHF31LnuWpt37QAcWyNp8PdbOQ+jjaV1xWXixS0M','lecturer','2022-08-08 15:00:02','2022-08-08 15:00:02'),(5,'Siam Yamsaengsung','siam.yam@kmutt.ac.th','$argon2id$v=19$m=4096,t=3,p=1$C4pPaNWKTnZQX2mPs14jlg$rQ5W5NYKqGOu1B4GkUWq8cFbcg2peFWGjpUMr9Nkm8g','lecturer','2022-08-08 15:00:03','2022-08-08 15:00:03'),(6,'Sunisa Sathapornvajana','sunisa.sat@kmutt.ac.th','$argon2id$v=19$m=4096,t=3,p=1$29/ffaszvjvi3CZO45bSCg$kKpfq5WEswoqa/LfyIZzQaQ6AFdjhyiYjXRCfMiTnwg','lecturer','2022-08-08 15:00:04','2022-08-08 15:00:04'),(7,'Somchai Jaidee','somchai.jai@kmutt.ac.th','$argon2id$v=19$m=4096,t=3,p=1$dmsOy7LPTjmooPu+P2oTZA$NZFTFd3f0K1Sp19aaUwyn3jgiy15yFcXhp8E4/1yXoI','student','2022-08-08 16:00:00','2022-08-08 16:00:00'),(8,'Komkrid Rakdee','komkrid.rak@mail.kmutt.ac.th','$argon2id$v=19$m=4096,t=3,p=1$8W61ZOC5RU7sJP5kKRbSqg$OLwZNPeMqxp+g0Vbn+odcA47XMClFN+IswTueVah7F0','student','2022-08-08 16:00:00','2022-08-08 16:00:00'),(9,'สมเกียรติ ขยันเรียน','somkiat.kay@kmutt.ac.th','$argon2id$v=19$m=4096,t=3,p=1$gBqgjspF45FcIKQEw8GmaQ$alrOCZ0YrDqOu8/aZiLDMGZo4vFkSEAXA0YoHhY0BDQ','student','2022-08-16 09:00:00','2022-08-16 09:00:00');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-10-18 23:46:08

CREATE USER 'admin'@'%' IDENTIFIED WITH mysql_native_password BY 'trimitr56';
GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%';
flush privileges;
set time_zone = '+7:00';
set global time_zone = '+7:00';
