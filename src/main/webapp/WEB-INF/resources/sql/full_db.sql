-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 18, 2015 at 02:14 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `xlra`
--

-- --------------------------------------------------------

--
-- Table structure for table `alphanumericalpostalcodes`
--

CREATE TABLE IF NOT EXISTS `alphanumericalpostalcodes` (
  `zone_id` bigint(20) NOT NULL,
  `postalcode` varchar(255) DEFAULT NULL,
  KEY `FK_sam77d7nwclmvx1k0e5d0rjph` (`zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `conditions`
--

CREATE TABLE IF NOT EXISTS `conditions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `conditionKey` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `rateFileId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_i2piwrp3nji49ekj8kgb65ifo` (`rateFileId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `conditions`
--

INSERT INTO `conditions` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `conditionKey`, `value`, `rateFileId`) VALUES
(1, '2015-10-18 16:02:18', b'0', NULL, NULL, 0, 'WACHT_TARIF', 'zelfde tarieven van toepassing.', 1),
(2, '2015-10-18 16:02:18', b'0', NULL, NULL, 0, 'EUR1_DOCUMENT', 'NVT', 1),
(3, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:55', 1, 'ADR_MINIMUM', '20.0', 1),
(4, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:55', 3, 'ADR_SURCHARGE', '10', 1),
(5, '2015-10-18 16:02:18', b'0', NULL, NULL, 0, 'IMPORT_FORM', 'NVT', 1),
(6, '2015-10-18 16:02:18', b'0', NULL, NULL, 0, 'EXPORT_FORM', 'NVT', 1),
(7, '2015-10-18 16:02:18', b'0', NULL, NULL, 0, 'RETOUR_REFUSED_DELIVERY', '42735.0', 1),
(8, '2015-10-18 16:02:18', b'0', NULL, NULL, 0, 'TARIF_FRANCO_HOUSE', '', 1),
(9, '2015-10-18 16:02:18', b'0', NULL, NULL, 0, 'TRANSPORT_INSURANCE', 'CMR- Verzekering (all risk op aanvraag)', 1),
(10, '2015-10-18 16:02:18', b'0', NULL, NULL, 0, 'PAYMENT_TERMS', '30 dagen na factuurdatum', 1);

-- --------------------------------------------------------

--
-- Table structure for table `configuration`
--

CREATE TABLE IF NOT EXISTS `configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `configurationName` varchar(255) DEFAULT NULL,
  `currentChfValue` decimal(19,2) DEFAULT NULL,
  `currentDieselPrice` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `configuration`
--

INSERT INTO `configuration` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `configurationName`, `currentChfValue`, `currentDieselPrice`) VALUES
(1, NULL, b'0', NULL, '2015-10-18 16:03:15', 1, 'mainconfig', '0.00', '1.45');

-- --------------------------------------------------------

--
-- Table structure for table `country`
--

CREATE TABLE IF NOT EXISTS `country` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `shortName` varchar(255) DEFAULT NULL,
  `zoneType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `country`
--

INSERT INTO `country` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `name`, `shortName`, `zoneType`) VALUES
(1, NULL, b'0', NULL, NULL, 0, 'Belgium', 'BE', 'NUMERIC_CODES'),
(2, NULL, b'0', NULL, NULL, 0, 'Netherlands', 'NL', 'NUMERIC_CODES'),
(3, NULL, b'0', NULL, NULL, 0, 'Switzerland', 'CH', 'ALPHANUMERIC_LIST'),
(4, NULL, b'0', NULL, NULL, 0, 'United kingdom', 'UK', 'ALPHANUMERIC_LIST');

-- --------------------------------------------------------

--
-- Table structure for table `countrynames`
--

CREATE TABLE IF NOT EXISTS `countrynames` (
  `country_id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `language` int(11) NOT NULL,
  PRIMARY KEY (`country_id`,`language`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `currencyrate`
--

CREATE TABLE IF NOT EXISTS `currencyrate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `end` double DEFAULT NULL,
  `start` double DEFAULT NULL,
  `surchargePercentage` double NOT NULL,
  `currencyType` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `currencyrate`
--

INSERT INTO `currencyrate` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `end`, `start`, `surchargePercentage`, `currencyType`) VALUES
(1, NULL, b'0', NULL, NULL, 0, 1.2, 1.1, 1, 'CHF'),
(2, NULL, b'0', NULL, NULL, 0, 1.3, 1.2, 1.1, 'CHF'),
(3, NULL, b'0', NULL, NULL, 0, 1.4, 1.3, 1.3, 'CHF'),
(4, NULL, b'0', NULL, NULL, 0, 1.5, 1.4, 1.5, 'CHF'),
(5, NULL, b'0', NULL, NULL, 0, 2, 1.5, 2, 'CHF');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `DTYPE` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `language` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  `btwNumber` varchar(100) DEFAULT NULL,
  `fullCustomer` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`DTYPE`, `id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `email`, `language`, `name`, `phone`, `city`, `country`, `number`, `street`, `zip`, `btwNumber`, `fullCustomer`) VALUES
('BaseCustomer', 1, '2015-10-18 16:02:00', b'0', NULL, NULL, 0, 'bas_moorkens@hotmail.com', 'NL', 'bas test', '0499230080', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `dieselrate`
--

CREATE TABLE IF NOT EXISTS `dieselrate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `end` double DEFAULT NULL,
  `start` double DEFAULT NULL,
  `surchargePercentage` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `dieselrate`
--

INSERT INTO `dieselrate` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `end`, `start`, `surchargePercentage`) VALUES
(1, NULL, b'0', NULL, NULL, 0, 1.2, 1.1, 1),
(2, NULL, b'0', NULL, NULL, 0, 1.3, 1.2, 1.1),
(3, NULL, b'0', NULL, NULL, 0, 1.4, 1.3, 1.3),
(4, NULL, b'0', NULL, NULL, 0, 1.5, 1.4, 1.5),
(5, NULL, b'0', NULL, NULL, 0, 2, 1.5, 2);

-- --------------------------------------------------------

--
-- Table structure for table `incotermtranslation`
--

CREATE TABLE IF NOT EXISTS `incotermtranslation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `incoTermType` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `mailtemplate`
--

CREATE TABLE IF NOT EXISTS `mailtemplate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `template` longtext,
  `xlraConfigurationId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_icnt9kow3d4ywygsy9okowf2l` (`xlraConfigurationId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `mailtemplate`
--

INSERT INTO `mailtemplate` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `language`, `subject`, `template`, `xlraConfigurationId`) VALUES
(1, NULL, b'0', NULL, NULL, 0, 'NL', NULL, 'ok', 1),
(2, NULL, b'0', NULL, NULL, 0, 'EN', NULL, 'ok', 1),
(3, NULL, b'0', NULL, NULL, 0, 'FR', NULL, 'ok', 1),
(4, NULL, b'0', NULL, NULL, 0, 'DE', NULL, 'ok', 1);

-- --------------------------------------------------------

--
-- Table structure for table `numericalpostalcodes`
--

CREATE TABLE IF NOT EXISTS `numericalpostalcodes` (
  `zone_id` bigint(20) NOT NULL,
  `end` double NOT NULL,
  `start` double NOT NULL,
  KEY `FK_68r5wsfu3qaipmh6pen1v7kf2` (`zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `numericalpostalcodes`
--

INSERT INTO `numericalpostalcodes` (`zone_id`, `end`, `start`) VALUES
(1, 6499, 1000),
(1, 9999, 7000),
(2, 6599, 6500);

-- --------------------------------------------------------

--
-- Table structure for table `quotationquery`
--

CREATE TABLE IF NOT EXISTS `quotationquery` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `adrSurcharge` bit(1) NOT NULL,
  `customer` tinyblob,
  `exportFormality` bit(1) NOT NULL,
  `importFormality` bit(1) NOT NULL,
  `kindOfRate` varchar(255) DEFAULT NULL,
  `measurement` varchar(255) DEFAULT NULL,
  `postalCode` varchar(255) DEFAULT NULL,
  `quantity` double NOT NULL,
  `countryId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_7x73x5hnl9mdjuvbolvjon6pq` (`countryId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `quotationresult`
--

CREATE TABLE IF NOT EXISTS `quotationresult` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `email` longtext,
  `pdf` longblob,
  `ratefileid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_spqnsq94ke145pvtyfh68jedq` (`ratefileid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `raiseratelogrecord`
--

CREATE TABLE IF NOT EXISTS `raiseratelogrecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `logDate` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `operation` varchar(255) DEFAULT NULL,
  `percentage` double NOT NULL,
  `undone` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `raiseratesratefilerecords`
--

CREATE TABLE IF NOT EXISTS `raiseratesratefilerecords` (
  `raiseRateRecordId` bigint(20) NOT NULL,
  `rateFileId` bigint(20) NOT NULL,
  KEY `FK_hqxk8un9k1cp5efoeaf4fq3fj` (`rateFileId`),
  KEY `FK_kqscn18tubf8ncfnx0buijiso` (`raiseRateRecordId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ratefile`
--

CREATE TABLE IF NOT EXISTS `ratefile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `kindOfRate` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `measurement` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `countryId` bigint(20) DEFAULT NULL,
  `customerId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qlw6kytevp1w9jryldke0f1rt` (`countryId`),
  KEY `FK_kayr33osml61pvo7kw5psoxkh` (`customerId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `ratefile`
--

INSERT INTO `ratefile` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `kindOfRate`, `language`, `measurement`, `name`, `countryId`, `customerId`) VALUES
(1, '2015-10-18 16:02:18', b'0', NULL, NULL, 0, 'NORMAL', 'NL', 'PALET', 'belgie - palet - normal', 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `rateline`
--

CREATE TABLE IF NOT EXISTS `rateline` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `measurement` double NOT NULL,
  `value` decimal(19,2) DEFAULT NULL,
  `rateFileId` bigint(20) DEFAULT NULL,
  `zoneId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_rx3kqcqn1r6m3ljtiosk87xbm` (`rateFileId`),
  KEY `FK_ke6qo0psrrsu7xm48cdekju3t` (`zoneId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=69 ;

--
-- Dumping data for table `rateline`
--

INSERT INTO `rateline` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `measurement`, `value`, `rateFileId`, `zoneId`) VALUES
(1, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 3, '95.94', 1, 1),
(2, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 3, '145.00', 1, 2),
(3, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 15, '229.73', 1, 1),
(4, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 15, '310.14', 1, 2),
(5, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 31, '332.41', 1, 1),
(6, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 31, '448.75', 1, 2),
(7, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 32, '338.77', 1, 1),
(8, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 32, '457.34', 1, 2),
(9, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 13, '212.54', 1, 1),
(10, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 13, '286.93', 1, 2),
(11, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 27, '309.77', 1, 1),
(12, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 27, '418.19', 1, 2),
(13, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 11, '193.79', 1, 1),
(14, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 11, '261.61', 1, 2),
(15, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 2, '67.00', 1, 1),
(16, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 2, '130.00', 1, 2),
(17, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 1, '35.00', 1, 1),
(18, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 1, '65.00', 1, 2),
(19, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 23, '284.99', 1, 1),
(20, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 23, '384.74', 1, 2),
(21, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 33, '345.12', 1, 1),
(22, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 33, '465.91', 1, 2),
(23, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 9, '168.78', 1, 1),
(24, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 9, '227.86', 1, 2),
(25, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 19, '257.86', 1, 1),
(26, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 19, '348.11', 1, 2),
(27, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 30, '326.74', 1, 1),
(28, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 30, '441.10', 1, 2),
(29, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 6, '131.28', 1, 1),
(30, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 6, '177.22', 1, 2),
(31, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 34, '350.00', 1, 1),
(32, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 34, '472.50', 1, 2),
(33, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 26, '305.44', 1, 1),
(34, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 26, '412.34', 1, 2),
(35, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 7, '143.78', 1, 1),
(36, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 7, '194.10', 1, 2),
(37, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 4, '114.64', 1, 1),
(38, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 4, '154.76', 1, 2),
(39, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 22, '281.40', 1, 1),
(40, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 22, '379.89', 1, 2),
(41, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 5, '125.46', 1, 1),
(42, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 5, '169.37', 1, 2),
(43, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 18, '251.61', 1, 1),
(44, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 18, '339.68', 1, 2),
(45, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 29, '321.22', 1, 1),
(46, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 29, '433.65', 1, 2),
(47, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 14, '221.92', 1, 1),
(48, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 14, '299.59', 1, 2),
(49, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 25, '299.92', 1, 1),
(50, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 25, '404.89', 1, 2),
(51, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 12, '203.16', 1, 1),
(52, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 12, '274.27', 1, 2),
(53, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 21, '275.75', 1, 1),
(54, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 21, '372.26', 1, 2),
(55, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 10, '181.29', 1, 1),
(56, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 10, '244.73', 1, 2),
(57, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 17, '245.36', 1, 1),
(58, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 17, '331.24', 1, 2),
(59, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 8, '156.28', 1, 1),
(60, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 8, '210.98', 1, 2),
(61, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 28, '315.72', 1, 1),
(62, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 28, '426.22', 1, 2),
(63, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 24, '294.01', 1, 1),
(64, '2015-10-18 16:02:18', b'0', NULL, '2015-10-18 16:02:39', 2, 24, '396.91', 1, 2),
(65, '2015-10-18 16:02:19', b'0', NULL, '2015-10-18 16:02:39', 2, 20, '269.44', 1, 1),
(66, '2015-10-18 16:02:19', b'0', NULL, '2015-10-18 16:02:39', 2, 20, '363.74', 1, 2),
(67, '2015-10-18 16:02:19', b'0', NULL, '2015-10-18 16:02:39', 2, 16, '237.55', 1, 1),
(68, '2015-10-18 16:02:19', b'0', NULL, '2015-10-18 16:02:39', 2, 16, '320.69', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `ratelogrecord`
--

CREATE TABLE IF NOT EXISTS `ratelogrecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `logDate` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `rate` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `ratelogrecord`
--

INSERT INTO `ratelogrecord` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `logDate`, `type`, `rate`) VALUES
(1, '2015-10-18 16:03:15', b'0', NULL, NULL, 0, '2015-10-18 16:03:15', 'DIESELRATE', '0.00');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `roleName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `xlratranslation`
--

CREATE TABLE IF NOT EXISTS `xlratranslation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `translationKey` varchar(255) DEFAULT NULL,
  `configurtionId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_eax9wd4iqklr7pyohna88034l` (`configurtionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `zone`
--

CREATE TABLE IF NOT EXISTS `zone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDateTime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deletedDateTime` datetime DEFAULT NULL,
  `lastUpdatedDateTime` datetime DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `extraInfo` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `zoneType` varchar(255) DEFAULT NULL,
  `countryId` bigint(20) DEFAULT NULL,
  `rateFileId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_aoo0ygicevhtlrileo2bfw1yg` (`countryId`),
  KEY `FK_ck1wceun2fk0n5ejvumx5lm9y` (`rateFileId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `zone`
--

INSERT INTO `zone` (`id`, `createdDateTime`, `deleted`, `deletedDateTime`, `lastUpdatedDateTime`, `version`, `extraInfo`, `name`, `zoneType`, `countryId`, `rateFileId`) VALUES
(1, '2015-10-18 16:02:19', b'0', NULL, '2015-10-18 16:02:55', 3, 'Belgie', '1', 'NUMERIC_CODES', NULL, 1),
(2, '2015-10-18 16:02:19', b'0', NULL, '2015-10-18 16:02:55', 3, 'Luxemburg', '2', 'NUMERIC_CODES', NULL, 1),
(3, '2015-10-18 16:02:19', b'0', NULL, NULL, 0, '', '3', 'NUMERIC_CODES', NULL, 1),
(4, '2015-10-18 16:02:19', b'0', NULL, NULL, 0, '', '4', 'NUMERIC_CODES', NULL, 1),
(5, '2015-10-18 16:02:19', b'0', NULL, NULL, 0, '', '5', 'NUMERIC_CODES', NULL, 1),
(6, '2015-10-18 16:02:19', b'0', NULL, NULL, 0, '', '6', 'NUMERIC_CODES', NULL, 1);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `alphanumericalpostalcodes`
--
ALTER TABLE `alphanumericalpostalcodes`
  ADD CONSTRAINT `FK_sam77d7nwclmvx1k0e5d0rjph` FOREIGN KEY (`zone_id`) REFERENCES `zone` (`id`);

--
-- Constraints for table `conditions`
--
ALTER TABLE `conditions`
  ADD CONSTRAINT `FK_i2piwrp3nji49ekj8kgb65ifo` FOREIGN KEY (`rateFileId`) REFERENCES `ratefile` (`id`);

--
-- Constraints for table `countrynames`
--
ALTER TABLE `countrynames`
  ADD CONSTRAINT `FK_5d8lgq5iw7y57osoh9xpq72hu` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`);

--
-- Constraints for table `mailtemplate`
--
ALTER TABLE `mailtemplate`
  ADD CONSTRAINT `FK_icnt9kow3d4ywygsy9okowf2l` FOREIGN KEY (`xlraConfigurationId`) REFERENCES `configuration` (`id`);

--
-- Constraints for table `numericalpostalcodes`
--
ALTER TABLE `numericalpostalcodes`
  ADD CONSTRAINT `FK_68r5wsfu3qaipmh6pen1v7kf2` FOREIGN KEY (`zone_id`) REFERENCES `zone` (`id`);

--
-- Constraints for table `quotationquery`
--
ALTER TABLE `quotationquery`
  ADD CONSTRAINT `FK_7x73x5hnl9mdjuvbolvjon6pq` FOREIGN KEY (`countryId`) REFERENCES `country` (`id`);

--
-- Constraints for table `quotationresult`
--
ALTER TABLE `quotationresult`
  ADD CONSTRAINT `FK_spqnsq94ke145pvtyfh68jedq` FOREIGN KEY (`ratefileid`) REFERENCES `ratefile` (`id`);

--
-- Constraints for table `raiseratesratefilerecords`
--
ALTER TABLE `raiseratesratefilerecords`
  ADD CONSTRAINT `FK_kqscn18tubf8ncfnx0buijiso` FOREIGN KEY (`raiseRateRecordId`) REFERENCES `raiseratelogrecord` (`id`),
  ADD CONSTRAINT `FK_hqxk8un9k1cp5efoeaf4fq3fj` FOREIGN KEY (`rateFileId`) REFERENCES `ratefile` (`id`);

--
-- Constraints for table `ratefile`
--
ALTER TABLE `ratefile`
  ADD CONSTRAINT `FK_kayr33osml61pvo7kw5psoxkh` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`),
  ADD CONSTRAINT `FK_qlw6kytevp1w9jryldke0f1rt` FOREIGN KEY (`countryId`) REFERENCES `country` (`id`);

--
-- Constraints for table `rateline`
--
ALTER TABLE `rateline`
  ADD CONSTRAINT `FK_ke6qo0psrrsu7xm48cdekju3t` FOREIGN KEY (`zoneId`) REFERENCES `zone` (`id`),
  ADD CONSTRAINT `FK_rx3kqcqn1r6m3ljtiosk87xbm` FOREIGN KEY (`rateFileId`) REFERENCES `ratefile` (`id`);

--
-- Constraints for table `xlratranslation`
--
ALTER TABLE `xlratranslation`
  ADD CONSTRAINT `FK_eax9wd4iqklr7pyohna88034l` FOREIGN KEY (`configurtionId`) REFERENCES `configuration` (`id`);

--
-- Constraints for table `zone`
--
ALTER TABLE `zone`
  ADD CONSTRAINT `FK_ck1wceun2fk0n5ejvumx5lm9y` FOREIGN KEY (`rateFileId`) REFERENCES `ratefile` (`id`),
  ADD CONSTRAINT `FK_aoo0ygicevhtlrileo2bfw1yg` FOREIGN KEY (`countryId`) REFERENCES `country` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
