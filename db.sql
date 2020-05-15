CREATE DATABASE IF NOT EXISTS db;

USE db;

DROP TABLE  IF EXISTS `db`.`users`;

CREATE TABLE `db`.`users`(
  `userName` varchar(30) NOT NULL,
  `hashedPassword2nd` varchar(200) NOT NULL,
  `salt` varchar(50) NOT NULL,
  `editUsers` int,
  `editAllBillboard` int,
  `createBillboard` int,
  `scheduleBillboard` int,
  PRIMARY KEY(`userName`)
)ENGINE=MyISAM DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `db`.`billboards`;

CREATE TABLE `db`.`billboards`(
  `userName` varchar(30) NOT NULL,
  `billboardName` varchar(30) NOT NULL,
  `billboardBackground` varchar(10),
  `messageText` varchar(500),
  `messageColour` varchar(10),
  `infoText` varchar(500),
  `infoColor` varchar(10),
  `pictureUrl` BLOB(1000),
  `pictureData` LONGBLOB
);

DROP TABLE IF EXISTS `db`.`schedules`;

CREATE TABLE `db`.`schedules`(
  `userName` varchar(30) NOT NULL,
  `billboardName` varchar(30) NOT NULL,
  `time` time NOT NULL,
  `date` date NOT NULL,
  `duration` time NOT NULL,
  `recurTime` time
);
