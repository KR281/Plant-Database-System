# Basic format for the database. Add data as needed.


CREATE DATABASE `Plants`;
USE `Plants`;

#Data formats. Refers to character sets. Such as latin1 and ascii.
#Client will send data in utf8 format. Server expects data from client in format utf8mb4 
SET NAMES utf8 ;
SET character_set_client = utf8mb4 ;

CREATE TABLE `Feed` (
  `Feed_ID` TINYINT(4) NOT NULL AUTO_INCREMENT,
  `Feed_Type` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`Feed_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
#Engine = storage engine. Auto_increment = where it starts. 
# Collate -> utf8mb4 = charset, 0900 version of alogorithm ai= accent insensitive, ci = case insensitive

CREATE TABLE `Pruning`(
	`Prune_id` tinyint(4) NOT NULL AUTO_INCREMENT,
    `Prune_type` VARCHAR(150),
    PRIMARY KEY (`Prune_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Plants` (
  `Plant_id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Nickname` VARCHAR(30),
  `PH_Start` FLOAT(3,1) CHECK (`PH_Start` BETWEEN 0.0 AND 14.0),
  `PH_END` FLOAT(3,1) CHECK (`PH_END` BETWEEN 0.0 AND 14.0),
  `Frost` BOOLEAN,
  `Perennial` BOOLEAN,
  `Edible` ENUM('Leaves','Fruit','None','All') NOT NULL,
  `Feed_id` tinyint (4),
  `Feed_Time` VARCHAR(15),
  `Prune_Time` VARCHAR(15),
  `Mulch` VARCHAR (30),
  `Notes` VARCHAR(255),
  PRIMARY KEY (`plant_id`),
  CONSTRAINT `fk_Feed_id` FOREIGN KEY (`Feed_id`) REFERENCES `Feed` (`Feed_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Plant_Pruning` (
  `Plant_id` tinyint(4) NOT NULL,
  `Prune_id` tinyint(4) NOT NULL,
  PRIMARY KEY (`Plant_id`, `Prune_id`),
  CONSTRAINT `fk_plant_pruning_Plant_id` FOREIGN KEY (`Plant_id`) REFERENCES `Plants` (`Plant_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_plant_pruning_Prune_id` FOREIGN KEY (`Prune_id`) REFERENCES `Pruning` (`Prune_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Health_benefits` (
  `benefit_id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `benefit` varchar(50) NOT NULL,
  PRIMARY KEY (`benefit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Plant_health_benefits` (
  `Plant_id` tinyint(4) NOT NULL,
  `benefit_id` tinyint(4) NOT NULL,
  PRIMARY KEY (`Plant_id`, `benefit_id`),
  CONSTRAINT `fk_plant_health_plant_id` FOREIGN KEY (`Plant_id`) REFERENCES `Plants` (`Plant_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_plant_health_benefit_id` FOREIGN KEY (`benefit_id`) REFERENCES `Health_benefits` (`benefit_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `plant_pics`(
	`Pic_id` tinyint(4) NOT NULL AUTO_INCREMENT,
	`Plant_id` tinyint(4) NOT NULL,
    `filename` VARCHAR (100) NOT NULL,
    PRIMARY KEY (`Pic_id`),
    CONSTRAINT `fk_Plant_id` FOREIGN KEY (`Plant_id`) REFERENCES `Plants` (`Plant_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Level_Descriptions` (
  `Level_id` TINYINT(4) NOT NULL AUTO_INCREMENT,
  `Level_Desc` VARCHAR(50) NOT NULL,
  `Category` ENUM('Sunlight','Water') NOT NULL,
  PRIMARY KEY (`Level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Plant_Levels`(
	`Plant_id` tinyint(4) NOT NULL,
    `Level_id` tinyint(4) NOT NULL,
    PRIMARY KEY (`Plant_id`,`Level_id`),
    CONSTRAINT `fk_Plant_levels_Plant_id` FOREIGN KEY (`Plant_id`) REFERENCES `Plants` (`Plant_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_Plant_levels_Level_id` FOREIGN KEY (`Level_id`) REFERENCES `Level_Descriptions` (`Level_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
