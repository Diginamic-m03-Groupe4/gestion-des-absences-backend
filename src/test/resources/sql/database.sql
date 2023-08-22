-- MySQL dump 10.19  Distrib 10.3.38-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: absencesDB
-- ------------------------------------------------------
-- Server version	10.3.38-MariaDB-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `absence`
--

DROP TABLE IF EXISTS `absence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `absence` (
  `date_debut` date DEFAULT NULL,
  `date_demande` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL CHECK (`status` between 0 and 3),
  `type_conge` tinyint(4) DEFAULT NULL CHECK (`type_conge` between 0 and 2),
  `employee_id` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `motif` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs69fc883x11wl5lkx9vjhf5ym` (`employee_id`),
  CONSTRAINT `FKs69fc883x11wl5lkx9vjhf5ym` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `absence`
--

LOCK TABLES `absence` WRITE;
/*!40000 ALTER TABLE `absence` DISABLE KEYS */;
INSERT INTO `absence` VALUES ('2023-12-05','2023-08-22','2023-12-05',3,1,5,1,'flemme'),('2023-09-05','2023-08-22','2023-09-06',3,1,5,2,'flemme'),('2023-11-06','2023-08-22','2023-11-06',3,1,5,3,'flemme'),('2023-12-05','2023-08-17','2023-12-06',1,1,6,4,'flemme'),('2023-09-05','2023-08-17','2023-09-06',1,1,6,5,'flemme'),('2023-11-06','2023-08-17','2023-11-07',1,1,6,6,'flemme'),('2023-12-05','2023-08-17','2023-12-06',1,1,7,7,'flemme'),('2023-09-05','2023-08-17','2023-09-06',1,1,7,8,'flemme'),('2023-11-06','2023-08-17','2023-11-07',1,1,7,9,'flemme'),('2023-12-05','2023-08-17','2023-12-06',1,1,8,10,'flemme'),('2023-09-05','2023-08-17','2023-09-06',1,1,8,11,'flemme'),('2023-11-06','2023-08-17','2023-11-07',1,1,8,12,'flemme'),('2023-12-05','2023-08-17','2023-12-06',1,1,9,13,'flemme'),('2023-09-05','2023-08-17','2023-09-06',1,1,9,14,'flemme'),('2023-11-06','2023-08-17','2023-11-07',1,1,9,15,'flemme'),('2023-12-05','2023-08-17','2023-12-06',1,1,10,16,'flemme'),('2023-09-05','2023-08-17','2023-09-06',1,1,10,17,'flemme'),('2023-11-06','2023-08-17','2023-11-07',1,1,10,18,'flemme'),('2023-12-05','2023-08-17','2023-12-06',1,1,11,19,'flemme'),('2023-09-05','2023-08-17','2023-09-06',1,1,11,20,'flemme'),('2023-11-06','2023-08-17','2023-11-07',1,1,11,21,'flemme'),('2023-12-05','2023-08-17','2023-12-06',1,1,12,22,'flemme'),('2023-09-05','2023-08-17','2023-09-06',1,1,12,23,'flemme'),('2023-11-06','2023-08-17','2023-11-07',1,1,12,24,'flemme'),('2023-12-05','2023-08-17','2023-12-06',1,1,13,25,'flemme'),('2023-09-05','2023-08-17','2023-09-06',1,1,13,26,'flemme'),('2023-11-06','2023-08-17','2023-11-07',1,1,13,27,'flemme');
/*!40000 ALTER TABLE `absence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `absence_rejetee`
--

DROP TABLE IF EXISTS `absence_rejetee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `absence_rejetee` (
  `date_debut` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  `date_rejet` date DEFAULT NULL,
  `type_conge` tinyint(4) DEFAULT NULL CHECK (`type_conge` between 0 and 2),
  `employee_id` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `motif` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2b52uilqyqoko4ljg7hegypvp` (`employee_id`),
  CONSTRAINT `FK2b52uilqyqoko4ljg7hegypvp` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `absence_rejetee`
--

LOCK TABLES `absence_rejetee` WRITE;
/*!40000 ALTER TABLE `absence_rejetee` DISABLE KEYS */;
/*!40000 ALTER TABLE `absence_rejetee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departement`
--

DROP TABLE IF EXISTS `departement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `departement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departement`
--

LOCK TABLES `departement` WRITE;
/*!40000 ALTER TABLE `departement` DISABLE KEYS */;
INSERT INTO `departement` VALUES (1,'développement'),(2,'ressources humaines'),(3,'formation'),(4,'Mayenne');
/*!40000 ALTER TABLE `departement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `date_embauche` date DEFAULT NULL,
  `role` tinyint(4) DEFAULT NULL CHECK (`role` between 0 and 2),
  `departement_id` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKehihuo8vsllwxg0raupcfm5ya` (`departement_id`),
  CONSTRAINT `FKehihuo8vsllwxg0raupcfm5ya` FOREIGN KEY (`departement_id`) REFERENCES `departement` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES ('2021-02-01',2,4,1,'vincent1@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2021-02-01',1,1,2,'vincent2@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2021-02-01',1,2,3,'vincent3@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2021-02-01',1,3,4,'vincent4@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2021-02-01',0,1,5,'vincent5@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2022-02-01',0,1,6,'vinsaucisse@vincent.vincent','Timètre','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2023-02-01',0,1,7,'vincent7@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2021-02-01',0,2,8,'vincent8@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2022-02-01',0,2,9,'vincent9@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2023-02-01',0,2,10,'vincent10@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2021-02-01',0,3,11,'vincent11@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2022-02-01',0,3,12,'vincent12@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),('2023-02-01',0,3,13,'vincent13@vincent.vincent','Vincent','{bcrypt}$2a$10$91/pgDe/oJExLAoq/kGvKO6otRjU/iNY21YjMh9udPlWW4G7JtiRG','Vincent'),(NULL,2,NULL,14,'vincent@antoine.fr','Antoine','{bcrypt}$2a$10$mh41iGURqTJGu7.36VyZous78Fccnykeq0NTkjWmSU6bXZe9zMBQ6','Vincent');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jour_ferie`
--

DROP TABLE IF EXISTS `jour_ferie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jour_ferie` (
  `date` date DEFAULT NULL,
  `is_worked` bit(1) NOT NULL,
  `libelle` tinyint(4) DEFAULT NULL CHECK (`libelle` between 0 and 12),
  `statut_absence_employeur` tinyint(4) DEFAULT NULL CHECK (`statut_absence_employeur` between 0 and 2),
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jour_ferie`
--

LOCK TABLES `jour_ferie` WRITE;
/*!40000 ALTER TABLE `jour_ferie` DISABLE KEYS */;
INSERT INTO `jour_ferie` VALUES ('2024-01-01','\0',0,1,1),('2024-04-01','\0',4,1,2),('2024-05-01','\0',2,1,3),('2024-05-08','\0',3,1,4),('2024-05-09','\0',4,1,5),('2024-05-20','',5,1,6),('2024-07-14','\0',6,1,7),('2024-08-15','\0',7,1,8),('2024-11-01','\0',8,1,9),('2024-11-11','\0',9,1,10),('2024-12-25','\0',10,1,11),('2023-01-01','\0',0,1,12),('2023-04-10','\0',4,1,13),('2023-05-01','\0',2,1,14),('2023-05-08','\0',3,1,15),('2023-05-18','\0',4,1,16),('2023-05-29','\0',5,1,17),('2023-07-14','\0',6,1,18),('2023-08-15','\0',7,1,19),('2023-11-01','\0',8,1,20),('2023-11-11','\0',9,1,21),('2023-12-25','\0',10,1,22);
/*!40000 ALTER TABLE `jour_ferie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rttemployeur`
--

DROP TABLE IF EXISTS `rttemployeur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rttemployeur` (
  `date` date DEFAULT NULL,
  `statut_absence_employeur` tinyint(4) DEFAULT NULL CHECK (`statut_absence_employeur` between 0 and 2),
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rttemployeur`
--

LOCK TABLES `rttemployeur` WRITE;
/*!40000 ALTER TABLE `rttemployeur` DISABLE KEYS */;
/*!40000 ALTER TABLE `rttemployeur` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-22 14:34:04
