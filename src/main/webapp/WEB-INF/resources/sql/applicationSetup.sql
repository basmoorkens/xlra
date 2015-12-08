INSERT INTO CONFIGURATION (deleted, configurationName, currentChfValue, currentDieselPrice, version)
VALUES (0, 'mainconfig', 0, 0, 0);


insert into currencyrate (start, end, surchargepercentage, deleted, currencyType, version) values (1.1, 1.2, 1,0, 'CHF', 0);
insert into currencyrate (start, end, surchargepercentage, deleted, currencyType, version) values (1.2, 1.3, 1.1,0, 'CHF', 0);
insert into currencyrate (start, end, surchargepercentage, deleted, currencyType, version) values (1.3, 1.4, 1.3,0, 'CHF', 0);
insert into currencyrate (start, end, surchargepercentage, deleted, currencyType, version) values (1.4, 1.5, 1.5,0, 'CHF', 0);
insert into currencyrate (start, end, surchargepercentage, deleted, currencyType, version) values (1.5, 2.0, 2, 0, 'CHF', 0);


INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0, 'BE', 0, 'NUMERIC_CODES');
INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0, 'NL', 0, 'NUMERIC_CODES');
INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0, 'CH', 0, 'NUMERIC_CODES');
INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0, 'UK', 0, 'ALPHANUMERIC_LIST');

INSERT INTO countrynames (country_id, name, language)
VALUES
	(1,'Belgien','DE'),
	(1,'Belgium','EN'),
	(1,'Belgique','FR'),
	(1,'België','NL'),
	(2,'Niederlande','DE'),
	(2,'The Netherlands','EN'),
	(2,'Les pays-bas','FR'),
	(2,'Nederland','NL'),
	(3,'Schweiz','DE'),
	(3,'Switzerland','EN'),
	(3,'Suisse','FR'),
	(3,'Zwitserland','NL'),
	(4,'England','DE'),
	(4,'England','EN'),
	(4,'Allemagne','FR'),
	(4,'Engeland','NL');
	
insert into DieselRate (start, end, surchargepercentage, deleted, version) values (1.1, 1.2, 1,0, 0);
insert into DieselRate (start, end, surchargepercentage, deleted, version) values (1.2, 1.3, 1.1,0, 0);
insert into DieselRate (start, end, surchargepercentage, deleted, version) values (1.3, 1.4, 1.3,0, 0);
insert into DieselRate (start, end, surchargepercentage, deleted, version) values (1.4, 1.5, 1.5,0, 0);
insert into DieselRate (start, end, surchargepercentage, deleted, version) values (1.5, 2.0, 2, 0, 0);

INSERT INTO mailTemplate (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, language, subject, template, xlraConfigurationId)
VALUES
	(1,NULL,00000000,NULL,'2015-12-06 15:41:44',4,'NL','Extra logistics offerte','<font face=\"Arial, Verdana\"><span style=\"font-size: 10pt;\">Beste ${customer},</span></font><div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\"><br></div><div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\">wij hebben uw aanvraag tot offerte goed ontvangen.&nbsp;</div><div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\">Uw aanvraag werd verwerkt door ${createdUserFullName}.</div><div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\">Gelieve in uw verdere communicatie volgend kenmerk te vermelden: ${offerteKey}.</div>\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n<div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\">Hieronder volgt onze offerte voor ${quantity} ${measurement} naar ${destination} :</div><div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\"><br></div><div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\">${detailCalculation}</div><div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\"><br></div><div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\">${additionalConditions}</div>\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n<div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\"><br></div><div style=\"font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;\">Met vriendelijke groeten,</div><div><div><font face=\"Arial, Verdana\"><span style=\"font-size: 13.3333px;\">Extra logistics NV</span></font></div><div><font face=\"Arial, Verdana\"><span style=\"font-size: 13.3333px;\">Cyriel Buyssestraat 1</span></font></div><div><font face=\"Arial, Verdana\"><span style=\"font-size: 13.3333px;\">1800 Vilvoorde</span></font></div><div><font face=\"Arial, Verdana\"><span style=\"font-size: 13.3333px;\">BE0475.454.606</span></font></div></div>',1),
	(2,NULL,00000000,NULL,'2015-12-06 15:41:44',6,'EN','Extra logistics quotation','<font face=\"Arial, Verdana\" style=\"font-size: 13.3333px;\"><span style=\"font-size: 10pt;\">Dear ${customer},</span></font><div style=\"font-size: 10pt;\"><br></div><div style=\"font-size: 10pt;\">we have received your request for quotation.&nbsp;</div><div style=\"font-size: 10pt;\">Your request was served by ${createdUserFullName}.</div><div style=\"font-size: 10pt;\"><span style=\"font-size: 13.3333px;\">Please use following reference in your future communication: ${offerteKey}.</div>\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n<div style=\"font-size: 10pt;\">Following is our quotation for&nbsp;<span style=\"font-size: 13.3333px;\">${quantity} ${measurement} to ${destination} :</span></div><div style=\"font-size: 10pt;\"><br></div><div style=\"font-size: 10pt;\">${detailCalculation}</div><div style=\"font-size: 10pt;\"><br></div><div style=\"font-size: 10pt;\">${additionalConditions}</div>\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n<div style=\"font-size: 10pt;\"><br></div><div style=\"font-size: 10pt;\">Kind regards,</div><div style=\"font-size: 13.3333px;\"><div><font face=\"Arial, Verdana\">Extra logistics NV</font></div><div><font face=\"Arial, Verdana\">Cyriel Buyssestraat 1</font></div><div><font face=\"Arial, Verdana\">1800 Vilvoorde</font></div><div><font face=\"Arial, Verdana\">BE0475.454.606</font></div></div>',1),
	(3,NULL,00000000,NULL,'2015-10-20 21:10:46',1,'FR','','ok',1),
	(4,NULL,00000000,NULL,'2015-10-20 21:10:46',1,'DE','','ok',1);

	INSERT INTO permissions (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, permission_description, permission_key)
VALUES
	(1,'2015-10-25 00:45:46',00000000,NULL,'2015-10-25 01:13:01',5,'The permission to edit chf intervals / percentages and the permission to update the current chf value.','CHF_EDIT'),
	(2,'2015-10-25 00:47:08',00000000,NULL,NULL,0,'The permission to edit diesel intervals / percentage and the permission to update the current diesel price','DIESEL_EDIT'),
	(3,'2015-10-25 00:47:31',00000000,NULL,NULL,0,'The permission to edit email templates.','EMAIL_TEMPLATE_EDIT'),
	(4,'2015-10-25 00:47:54',00000000,NULL,NULL,0,'The permission to raise rates.','RAISE_RATES'),
	(5,'2015-10-25 00:48:06',00000000,NULL,'2015-10-25 01:14:51',2,'The permission to undo a raise of rates.','UNDO_RAISE_RATES'),
	(6,'2015-10-25 00:48:34',00000000,NULL,NULL,0,'The permission to add / edit full users.','FULL_CUSTOMER_EDIT'),
	(7,'2015-10-25 00:48:58',00000000,NULL,NULL,0,'The permission to edit the translations for countries.','COUNTRY_EDIT'),
	(8,'2015-10-25 00:49:16',00000000,NULL,NULL,0,'The permission to be able to edit / add translations.','TRANSLATION_EDIT'),
	(9,'2015-10-25 00:49:40',00000000,NULL,NULL,0,'The permission to add / edit users','USER_EDIT'),
	(10,'2015-10-25 00:49:51',00000000,NULL,NULL,0,'The permission to add / edit roles.','ROLE_EDIT'),
	(11,'2015-10-25 00:50:04',00000000,NULL,NULL,0,'The permission to add/edit permissions.','PERMISSION_EDIT'),
	(12,'2015-10-25 00:50:24',00000000,NULL,NULL,0,'The permission to upload excel ratefiles.','EXCEL_UPLOAD'),
	(13,'2015-10-25 00:50:38',00000000,NULL,NULL,0,'The permission to create offertes.','CREATE_OFFERTE'),
	(14,'2015-10-25 00:50:57',00000000,NULL,NULL,0,'The permission to view offertes','VIEW_OFFERTE'),
	(15,'2015-10-25 00:51:22',00000000,NULL,NULL,0,'The permission to view ratefiles.','VIEW_RATEFILE'),
	(16,'2015-10-25 00:51:38',00000000,NULL,NULL,0,'The permission to edit ratefiles','EDIT_RATEFILE'),
	(17,'2015-10-25 00:51:52',00000000,NULL,NULL,0,'The permission to create ratefiles.','CREATE_RATEFILE'),
	(22,'2015-10-28 19:42:02',00000000,NULL,NULL,0,'The permission to edit a user his own profile / reset password','USER_EDIT_PROFILE'),
	(23,'2015-10-28 19:43:45',00000000,NULL,NULL,0,'The permission to edit a full user','FULL_CUSTOMER_EDIT');

	INSERT INTO role (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, role_description, role_name)
VALUES
	(1,'2015-10-27 22:47:38',00000000,NULL,'2015-10-28 19:43:55',3,'Admin role, has all permissions','Admin'),
	(2,'2015-10-27 23:47:30',00000000,NULL,'2015-10-28 19:42:17',2,'The role for user','User');

	INSERT INTO role_permissions (role_id, permission_id)
VALUES
	(1,1),
	(1,2),
	(1,3),
	(1,4),
	(1,5),
	(1,6),
	(1,7),
	(1,8),
	(1,9),
	(1,10),
	(1,11),
	(1,12),
	(1,13),
	(1,14),
	(1,15),
	(1,16),
	(1,17),
	(1,22),
	(1,23),
	(2,13),
	(2,14),
	(2,15),
	(2,22);
	
	INSERT INTO user (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, email, firstName, name, password, validTo, verificationToken, userName, userStatus)
VALUES
	(1,'2015-10-27 22:51:37',00000000,NULL,'2015-12-05 20:35:31',2,'bas_moorkens@hotmail.com','bas','moorkens','c05e198412a3608e7a626e473180472d170f0f9c95c158eb0a43583e286799f3',NULL,NULL,'bmoork','IN_OPERATION');
	
	INSERT INTO user_roles (user_id, role_id)
VALUES
	(1,1);