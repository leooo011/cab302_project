CREATE DATABASE IF NOT EXISTS db;

USE db;

DROP TABLE  IF EXISTS `db`.`users`;

CREATE TABLE IF NOT EXISTS `db`.`users`(
  `userName` varchar(30) NOT NULL,
  `hashedPassword2nd` varchar(255) NOT NULL,
  `salt` varchar(50) NOT NULL,
  `editUsers` int,
  `editAllBillboard` int,
  `createBillboard` int,
  `scheduleBillboard` int,
  PRIMARY KEY(`userName`)
)ENGINE=MyISAM DEFAULT CHARSET=latin1;