-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 14, 2015 at 07:27 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: 'xlra'
--

-- --------------------------------------------------------

--
-- Table structure for table 'alphanumericalpostalcodes'
--

CREATE TABLE IF NOT EXISTS alphanumericalpostalcodes (
  zone_id bigint(20) NOT NULL,
  postalcode varchar(255) DEFAULT NULL,
  KEY FK_sam77d7nwclmvx1k0e5d0rjph (zone_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table 'appliedoperations'
--

CREATE TABLE IF NOT EXISTS appliedoperations (
  calculation_id bigint(20) NOT NULL,
  appliedOperations varchar(255) DEFAULT NULL,
  KEY FK_5q848jiu7u3hqu2vy117k72og (calculation_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table 'conditions'
--

CREATE TABLE IF NOT EXISTS conditions (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  conditionKey varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  offerte_standard_selected bit(1) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  rateFileId bigint(20) DEFAULT NULL,
  calculation_value_type varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_i2piwrp3nji49ekj8kgb65ifo (rateFileId)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=63 ;

-- --------------------------------------------------------

--
-- Table structure for table 'configuration'
--

CREATE TABLE IF NOT EXISTS configuration (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  configurationName varchar(255) DEFAULT NULL,
  currentChfValue decimal(19,2) DEFAULT NULL,
  currentDieselPrice decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table 'country'
--

CREATE TABLE IF NOT EXISTS country (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  shortName varchar(255) DEFAULT NULL,
  zoneType varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table 'countrynames'
--

CREATE TABLE IF NOT EXISTS countrynames (
  country_id bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `language` varchar(255) NOT NULL,
  PRIMARY KEY (country_id,`language`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table 'currencyrate'
--

CREATE TABLE IF NOT EXISTS currencyrate (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  `end` double DEFAULT NULL,
  `start` double DEFAULT NULL,
  surchargePercentage double NOT NULL,
  currencyType varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

-- --------------------------------------------------------

--
-- Table structure for table 'customer'
--

CREATE TABLE IF NOT EXISTS customer (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  city varchar(255) DEFAULT NULL,
  country varchar(255) DEFAULT NULL,
  number varchar(255) DEFAULT NULL,
  street varchar(255) DEFAULT NULL,
  zip varchar(255) DEFAULT NULL,
  btwNumber varchar(100) DEFAULT NULL,
  email varchar(100) NOT NULL,
  hasOwnRateFile bit(1) NOT NULL,
  `language` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  phone varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table 'dieselrate'
--

CREATE TABLE IF NOT EXISTS dieselrate (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  `end` double DEFAULT NULL,
  `start` double DEFAULT NULL,
  surchargePercentage double NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

-- --------------------------------------------------------

--
-- Table structure for table 'emailhistoryrecord'
--

CREATE TABLE IF NOT EXISTS emailhistoryrecord (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  `dateTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  offerte_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_6qlbshb6co5bjuwi4bwpio2mp (offerte_id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table 'incotermtranslation'
--

CREATE TABLE IF NOT EXISTS incotermtranslation (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  incoTermType varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table 'mailtemplate'
--

CREATE TABLE IF NOT EXISTS mailtemplate (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  template longtext,
  xlraConfigurationId bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_icnt9kow3d4ywygsy9okowf2l (xlraConfigurationId)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table 'numericalpostalcodes'
--

CREATE TABLE IF NOT EXISTS numericalpostalcodes (
  zone_id bigint(20) NOT NULL,
  `end` double NOT NULL,
  `start` double NOT NULL,
  KEY FK_68r5wsfu3qaipmh6pen1v7kf2 (zone_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table 'permissions'
--

CREATE TABLE IF NOT EXISTS permissions (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  permission_description varchar(255) DEFAULT NULL,
  permission_key varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=24 ;

-- --------------------------------------------------------

--
-- Table structure for table 'pricecalculation'
--

CREATE TABLE IF NOT EXISTS pricecalculation (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  adrSurchargeMinimum decimal(19,2) DEFAULT NULL,
  basePrice decimal(19,2) DEFAULT NULL,
  calculatedAdrSurcharge decimal(19,2) DEFAULT NULL,
  chfPrice decimal(19,2) DEFAULT NULL,
  dieselPrice decimal(19,2) DEFAULT NULL,
  exportFormalities decimal(19,2) DEFAULT NULL,
  finalPrice decimal(19,2) DEFAULT NULL,
  importFormalities decimal(19,2) DEFAULT NULL,
  resultingPriceSurcharge decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table 'quotationquery'
--

CREATE TABLE IF NOT EXISTS quotationquery (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  adrSurcharge bit(1) NOT NULL,
  exportFormality bit(1) NOT NULL,
  importFormality bit(1) NOT NULL,
  kindOfRate varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  measurement varchar(255) DEFAULT NULL,
  postalCode varchar(255) DEFAULT NULL,
  quantity double NOT NULL,
  quotation_date datetime DEFAULT NULL,
  transportType varchar(255) DEFAULT NULL,
  countryId bigint(20) DEFAULT NULL,
  customer_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_7x73x5hnl9mdjuvbolvjon6pq (countryId),
  KEY FK_74jhgeinbnqu1r03ewedxv7by (customer_id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table 'quotationresult'
--

CREATE TABLE IF NOT EXISTS quotationresult (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  created_user_full_name varchar(255) DEFAULT NULL,
  email longtext,
  send bit(1) NOT NULL,
  `subject` varchar(255) DEFAULT NULL,
  toAddress varchar(255) DEFAULT NULL,
  offerteUniqueIdentifier varchar(255) DEFAULT NULL,
  pdf_file_name varchar(255) DEFAULT NULL,
  calculation_id bigint(20) DEFAULT NULL,
  quotation_query_id bigint(20) DEFAULT NULL,
  ratefileid bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_r87gribu44m3p0bbgjnlf0u2a (calculation_id),
  KEY FK_92ei8ycf4hhheqmj1vsb6nx5c (quotation_query_id),
  KEY FK_spqnsq94ke145pvtyfh68jedq (ratefileid)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table 'raiseratelogrecord'
--

CREATE TABLE IF NOT EXISTS raiseratelogrecord (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  logDate datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  userName varchar(255) DEFAULT NULL,
  operation varchar(255) DEFAULT NULL,
  percentage double NOT NULL,
  undone bit(1) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table 'raiseratesratefilerecords'
--

CREATE TABLE IF NOT EXISTS raiseratesratefilerecords (
  raiseRateRecordId bigint(20) NOT NULL,
  rateFileId bigint(20) NOT NULL,
  KEY FK_hqxk8un9k1cp5efoeaf4fq3fj (rateFileId),
  KEY FK_kqscn18tubf8ncfnx0buijiso (raiseRateRecordId)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table 'ratefile'
--

CREATE TABLE IF NOT EXISTS ratefile (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  kindOfRate varchar(255) DEFAULT NULL,
  measurement varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  transportType varchar(255) DEFAULT NULL,
  countryId bigint(20) DEFAULT NULL,
  customerId bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_qlw6kytevp1w9jryldke0f1rt (countryId),
  KEY FK_kayr33osml61pvo7kw5psoxkh (customerId)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

-- --------------------------------------------------------

--
-- Table structure for table 'rateline'
--

CREATE TABLE IF NOT EXISTS rateline (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  measurement double NOT NULL,
  `value` decimal(19,2) DEFAULT NULL,
  rateFileId bigint(20) DEFAULT NULL,
  zoneId bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_rx3kqcqn1r6m3ljtiosk87xbm (rateFileId),
  KEY FK_ke6qo0psrrsu7xm48cdekju3t (zoneId)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=681 ;

-- --------------------------------------------------------

--
-- Table structure for table 'ratelogrecord'
--

CREATE TABLE IF NOT EXISTS ratelogrecord (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  logDate datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  userName varchar(255) DEFAULT NULL,
  rate decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table 'role'
--

CREATE TABLE IF NOT EXISTS role (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  role_description varchar(255) DEFAULT NULL,
  role_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table 'role_permissions'
--

CREATE TABLE IF NOT EXISTS role_permissions (
  role_id bigint(20) NOT NULL,
  permission_id bigint(20) NOT NULL,
  KEY FK_qfkbccnh2c5o4tc7akq5x11wv (permission_id),
  KEY FK_d4atqq8ege1sij0316vh2mxfu (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table 'translationsforlanguages'
--

CREATE TABLE IF NOT EXISTS translationsforlanguages (
  translation_id bigint(20) DEFAULT NULL,
  `language` int(11) DEFAULT NULL,
  translation varchar(255) DEFAULT NULL,
  condition_id bigint(20) NOT NULL,
  KEY FK_rm3s0atp1gf45daw8cn0rselw (condition_id),
  KEY translation_id (translation_id),
  KEY translation_id_2 (translation_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table 'user'
--

CREATE TABLE IF NOT EXISTS `user` (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  email varchar(255) NOT NULL,
  firstName varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  validTo datetime DEFAULT NULL,
  verificationToken varchar(255) DEFAULT NULL,
  userName varchar(255) NOT NULL,
  userStatus varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_ob8kqyqqgmefl0aco34akdtpe (email),
  UNIQUE KEY UK_4bakctviobmdk6ddh2nwg08c2 (userName)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table 'userlogrecord'
--

CREATE TABLE IF NOT EXISTS userlogrecord (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  logDate datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  userName varchar(255) DEFAULT NULL,
  affectedAccount varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table 'user_roles'
--

CREATE TABLE IF NOT EXISTS user_roles (
  user_id bigint(20) NOT NULL,
  role_id bigint(20) NOT NULL,
  KEY FK_5q4rc4fh1on6567qk69uesvyf (role_id),
  KEY FK_g1uebn6mqk9qiaw45vnacmyo2 (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table 'xlratranslation'
--

CREATE TABLE IF NOT EXISTS xlratranslation (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  translationKey varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table 'zone'
--

CREATE TABLE IF NOT EXISTS zone (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createdDateTime datetime DEFAULT NULL,
  deleted bit(1) NOT NULL,
  deletedDateTime datetime DEFAULT NULL,
  lastUpdatedDateTime datetime DEFAULT NULL,
  version bigint(20) NOT NULL,
  extraInfo varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  zoneType varchar(255) DEFAULT NULL,
  countryId bigint(20) DEFAULT NULL,
  rateFileId bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_aoo0ygicevhtlrileo2bfw1yg (countryId),
  KEY FK_ck1wceun2fk0n5ejvumx5lm9y (rateFileId)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=39 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `alphanumericalpostalcodes`
--
ALTER TABLE `alphanumericalpostalcodes`
  ADD CONSTRAINT FK_sam77d7nwclmvx1k0e5d0rjph FOREIGN KEY (zone_id) REFERENCES zone (id);

--
-- Constraints for table `appliedoperations`
--
ALTER TABLE `appliedoperations`
  ADD CONSTRAINT FK_5q848jiu7u3hqu2vy117k72og FOREIGN KEY (calculation_id) REFERENCES pricecalculation (id);

--
-- Constraints for table `conditions`
--
ALTER TABLE `conditions`
  ADD CONSTRAINT FK_i2piwrp3nji49ekj8kgb65ifo FOREIGN KEY (rateFileId) REFERENCES ratefile (id);

--
-- Constraints for table `countrynames`
--
ALTER TABLE `countrynames`
  ADD CONSTRAINT FK_5d8lgq5iw7y57osoh9xpq72hu FOREIGN KEY (country_id) REFERENCES country (id);

--
-- Constraints for table `emailhistoryrecord`
--
ALTER TABLE `emailhistoryrecord`
  ADD CONSTRAINT FK_6qlbshb6co5bjuwi4bwpio2mp FOREIGN KEY (offerte_id) REFERENCES quotationresult (id);

--
-- Constraints for table `mailtemplate`
--
ALTER TABLE `mailtemplate`
  ADD CONSTRAINT FK_icnt9kow3d4ywygsy9okowf2l FOREIGN KEY (xlraConfigurationId) REFERENCES configuration (id);

--
-- Constraints for table `numericalpostalcodes`
--
ALTER TABLE `numericalpostalcodes`
  ADD CONSTRAINT FK_68r5wsfu3qaipmh6pen1v7kf2 FOREIGN KEY (zone_id) REFERENCES zone (id);

--
-- Constraints for table `quotationquery`
--
ALTER TABLE `quotationquery`
  ADD CONSTRAINT FK_74jhgeinbnqu1r03ewedxv7by FOREIGN KEY (customer_id) REFERENCES customer (id),
  ADD CONSTRAINT FK_7x73x5hnl9mdjuvbolvjon6pq FOREIGN KEY (countryId) REFERENCES country (id);

--
-- Constraints for table `quotationresult`
--
ALTER TABLE `quotationresult`
  ADD CONSTRAINT FK_92ei8ycf4hhheqmj1vsb6nx5c FOREIGN KEY (quotation_query_id) REFERENCES quotationquery (id),
  ADD CONSTRAINT FK_r87gribu44m3p0bbgjnlf0u2a FOREIGN KEY (calculation_id) REFERENCES pricecalculation (id),
  ADD CONSTRAINT FK_spqnsq94ke145pvtyfh68jedq FOREIGN KEY (ratefileid) REFERENCES ratefile (id);

--
-- Constraints for table `raiseratesratefilerecords`
--
ALTER TABLE `raiseratesratefilerecords`
  ADD CONSTRAINT FK_hqxk8un9k1cp5efoeaf4fq3fj FOREIGN KEY (rateFileId) REFERENCES ratefile (id),
  ADD CONSTRAINT FK_kqscn18tubf8ncfnx0buijiso FOREIGN KEY (raiseRateRecordId) REFERENCES raiseratelogrecord (id);

--
-- Constraints for table `ratefile`
--
ALTER TABLE `ratefile`
  ADD CONSTRAINT FK_kayr33osml61pvo7kw5psoxkh FOREIGN KEY (customerId) REFERENCES customer (id),
  ADD CONSTRAINT FK_qlw6kytevp1w9jryldke0f1rt FOREIGN KEY (countryId) REFERENCES country (id);

--
-- Constraints for table `rateline`
--
ALTER TABLE `rateline`
  ADD CONSTRAINT FK_ke6qo0psrrsu7xm48cdekju3t FOREIGN KEY (zoneId) REFERENCES zone (id),
  ADD CONSTRAINT FK_rx3kqcqn1r6m3ljtiosk87xbm FOREIGN KEY (rateFileId) REFERENCES ratefile (id);

--
-- Constraints for table `role_permissions`
--
ALTER TABLE `role_permissions`
  ADD CONSTRAINT FK_d4atqq8ege1sij0316vh2mxfu FOREIGN KEY (role_id) REFERENCES role (id),
  ADD CONSTRAINT FK_qfkbccnh2c5o4tc7akq5x11wv FOREIGN KEY (permission_id) REFERENCES permissions (id);

--
-- Constraints for table `translationsforlanguages`
--
ALTER TABLE `translationsforlanguages`
  ADD CONSTRAINT FK_o12h3d6mnvx40jhgbghc17cpn FOREIGN KEY (translation_id) REFERENCES xlratranslation (id),
  ADD CONSTRAINT FK_rm3s0atp1gf45daw8cn0rselw FOREIGN KEY (condition_id) REFERENCES `conditions` (id);

--
-- Constraints for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT FK_5q4rc4fh1on6567qk69uesvyf FOREIGN KEY (role_id) REFERENCES role (id),
  ADD CONSTRAINT FK_g1uebn6mqk9qiaw45vnacmyo2 FOREIGN KEY (user_id) REFERENCES `user` (id);

--
-- Constraints for table `zone`
--
ALTER TABLE `zone`
  ADD CONSTRAINT FK_aoo0ygicevhtlrileo2bfw1yg FOREIGN KEY (countryId) REFERENCES country (id),
  ADD CONSTRAINT FK_ck1wceun2fk0n5ejvumx5lm9y FOREIGN KEY (rateFileId) REFERENCES ratefile (id);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
