	INSERT INTO CONFIGURATION (deleted, configurationName, currentChfValue, currentDieselPrice, version)
	VALUES (0, 'mainconfig', 0, 0, 0);
	
	INSERT INTO currencyrate (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, end, start, surchargePercentage, currencyType) VALUES
	(1, NULL, b'0', NULL, NULL, 0, 1.2, 1.1, 1, 'CHF'),
	(2, NULL, b'0', NULL, NULL, 0, 1.3, 1.2, 1.1, 'CHF'),
	(3, NULL, b'0', NULL, NULL, 0, 1.4, 1.3, 1.3, 'CHF'),
	(4, NULL, b'0', NULL, NULL, 0, 1.5, 1.4, 1.5, 'CHF'),
	(5, NULL, b'0', NULL, '2015-10-19 21:57:11', 1, 1.6, 1.5, 2, 'CHF'),
	(6, '2015-10-19 21:42:26', b'0', NULL, '2015-10-20 21:07:07', 3, 1.7, 1.6, 2.5, 'CHF'),
	(7, '2015-10-20 21:06:59', b'0', NULL, NULL, 0, 1.8, 1.7, 3, 'CHF');
	
	INSERT INTO dieselrate (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, end, start, surchargePercentage) VALUES
	(1, NULL, b'0', NULL, NULL, 0, 1.2, 1.1, 1),
	(2, NULL, b'0', NULL, NULL, 0, 1.3, 1.2, 1.1),
	(3, NULL, b'0', NULL, NULL, 0, 1.4, 1.3, 1.3),
	(4, NULL, b'0', NULL, NULL, 0, 1.5, 1.4, 1.5),
	(5, NULL, b'0', NULL, '2015-10-19 21:24:12', 1, 1.6, 1.5, 2),
	(6, '2015-10-19 21:22:47', b'0', NULL, '2015-10-19 21:24:22', 1, 1.7, 1.6, 2.5),
	(7, '2015-10-20 21:07:25', b'0', NULL, NULL, 0, 1.8, 1.7, 3);
	
	INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0,  'BE', 0, 'NUMERIC_CODES');
	INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0,  'NL', 0, 'NUMERIC_CODES');
	INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0,  'CH', 0, 'ALPHANUMERIC_LIST');
	INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0,  'UK', 0, 'ALPHANUMERIC_LIST');
	
	
	INSERT INTO mailtemplate (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, language, subject, template, xlraConfigurationId) VALUES
	(1, NULL, b'0', NULL, '2015-10-20 21:10:46', 1, 'NL', 'Extra logistics offerte', '<font face="Arial, Verdana"><span style="font-size: 10pt;">Beste ${customer},</span></font><div style="font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;"><br></div><div style="font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;">wij hebben uw aanvraag tot offerte goed ontvangen. Hieronder volgt onze offerte voor ${quantity} ${measurement} naar ${destination} :</div><div style="font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;"><br></div><div style="font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;">${detailCalculation}</div><div style="font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;"><br></div><div style="font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant: normal; font-weight: normal; line-height: normal;">Met vriendelijke groeten,</div><div><div><font face="Arial, Verdana"><span style="font-size: 13.3333px;">Extra logistics NV</span></font></div><div><font face="Arial, Verdana"><span style="font-size: 13.3333px;">Cyriel Buyssestraat 1</span></font></div><div><font face="Arial, Verdana"><span style="font-size: 13.3333px;">1800 Vilvoorde</span></font></div><div><font face="Arial, Verdana"><span style="font-size: 13.3333px;">BE0475.454.606</span></font></div></div>', 1),
	(2, NULL, b'0', NULL, '2015-10-20 21:10:46', 1, 'EN', '', 'ok', 1),
	(3, NULL, b'0', NULL, '2015-10-20 21:10:46', 1, 'FR', '', 'ok', 1),
	(4, NULL, b'0', NULL, '2015-10-20 21:10:46', 1, 'DE', '', 'ok', 1);
	
	
	INSERT INTO countrynames (country_id, name, language) VALUES
	(1, 'Belgien', 'DE'),
	(1, 'Belgium', 'EN'),
	(1, 'Belgique', 'FR'),
	(1, 'België', 'NL'),
	(2, 'Niederlande', 'DE'),
	(2, 'The Netherlands', 'EN'),
	(2, 'Les pays-bas', 'FR'),
	(2, 'Nederland', 'NL'),
	(3, 'Schweiz', 'DE'),
	(3, 'Switzerland', 'EN'),
	(3, 'Suisse', 'FR'),
	(3, 'Zwitserland', 'NL'),
	(4, 'England', 'DE'),
	(4, 'England', 'EN'),
	(4, 'Allemagne', 'FR'),
	(4, 'Engeland', 'NL');
	
	
	INSERT INTO permissions (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, permission_description, permission_key)
	VALUES
		(1, '2015-10-25 00:45:46', 00000000, NULL, '2015-10-25 01:13:01', 5, 'The permission to edit chf intervals / percentages and the permission to update the current chf value.', 'CHF_EDIT'),
		(2, '2015-10-25 00:47:08', 00000000, NULL, NULL, 0, 'The permission to edit diesel intervals / percentage and the permission to update the current diesel price', 'DIESEL_EDIT'),
		(3, '2015-10-25 00:47:31', 00000000, NULL, NULL, 0, 'The permission to edit email templates.', 'EMAIL_TEMPLATE_EDIT'),
		(4, '2015-10-25 00:47:54', 00000000, NULL, NULL, 0, 'The permission to raise rates.', 'RAISE_RATES'),
		(5, '2015-10-25 00:48:06', 00000000, NULL, '2015-10-25 01:14:51', 2, 'The permission to undo a raise of rates.', 'UNDO_RAISE_RATES'),
		(6, '2015-10-25 00:48:34', 00000000, NULL, NULL, 0, 'The permission to add / edit full users.', 'FULL_CUSTOMER_EDIT'),
		(7, '2015-10-25 00:48:58', 00000000, NULL, NULL, 0, 'The permission to edit the translations for countries.', 'COUNTRY_EDIT'),
		(8, '2015-10-25 00:49:16', 00000000, NULL, NULL, 0, 'The permission to be able to edit / add translations.', 'TRANSLATION_EDIT'),
		(9, '2015-10-25 00:49:40', 00000000, NULL, NULL, 0, 'The permission to add / edit users', 'USER_EDIT'),
		(10, '2015-10-25 00:49:51', 00000000, NULL, NULL, 0, 'The permission to add / edit roles.', 'ROLE_EDIT'),
		(11, '2015-10-25 00:50:04', 00000000, NULL, NULL, 0, 'The permission to add/edit permissions.', 'PERMISSION_EDIT'),
		(12, '2015-10-25 00:50:24', 00000000, NULL, NULL, 0, 'The permission to upload excel ratefiles.', 'EXCEL_UPLOAD'),
		(13, '2015-10-25 00:50:38', 00000000, NULL, NULL, 0, 'The permission to create offertes.', 'CREATE_OFFERTE'),
		(14, '2015-10-25 00:50:57', 00000000, NULL, NULL, 0, 'The permission to view offertes', 'VIEW_OFFERTE'),
		(15, '2015-10-25 00:51:22', 00000000, NULL, NULL, 0, 'The permission to view ratefiles.', 'VIEW_RATEFILE'),
		(16, '2015-10-25 00:51:38', 00000000, NULL, NULL, 0, 'The permission to edit ratefiles', 'EDIT_RATEFILE'),
		(17, '2015-10-25 00:51:52', 00000000, NULL, NULL, 0, 'The permission to create ratefiles.', 'CREATE_RATEFILE');
		INSERT INTO permissions (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, permission_description, permission_key)
	VALUES
		(22, '2015-10-28 19:42:02', 00000000, NULL, NULL, 0, 'The permission to edit a user his own profile / reset password', 'USER_EDIT_PROFILE'),
		(23, '2015-10-28 19:43:45', 00000000, NULL, NULL, 0, 'The permission to edit a full user', 'FULL_CUSTOMER_EDIT');
	
	INSERT INTO user (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, email, enabled, firstName, name, password, validTo, verificationToken, userStatus, userName) VALUES
		(1, '2015-10-27 22:51:37', 00000000, NULL, NULL, 0, 'bas_moorkens@hotmail.com', 00000001, 'bas', 'moorkens', 'c05e198412a3608e7a626e473180472d170f0f9c95c158eb0a43583e286799f3',  NULL, NULL, 'IN_OPERATION','bmoork');
	
		INSERT INTO role (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, role_description, role_name) VALUES
		(1, '2015-10-27 22:47:38', 00000000, NULL, '2015-10-28 19:43:55', 3, 'Admin role, has all permissions', 'Admin'),
		(2, '2015-10-27 23:47:30', 00000000, NULL, '2015-10-28 19:42:17', 2, 'The role for user', 'User');
	
		
		INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
		
		INSERT INTO `role_permissions` (`role_id`, `permission_id`)
	VALUES
		(1, 1),
		(1, 2),
		(1, 3),
		(1, 4),
		(1, 5),
		(1, 6),
		(1, 7),
		(1, 8),
		(1, 9),
		(1, 10),
		(1, 11),
		(1, 12),
		(1, 13),
		(1, 14),
		(1, 15),
		(1, 16),
		(1, 17),
		(1, 22),
		(1, 23),
		(2, 13),
		(2, 14),
		(2, 15),
		(2, 22);
	
	
	
	INSERT INTO xlratranslation (id, createdDateTime, deleted, deletedDateTime, lastUpdatedDateTime, version, language, text, translationKey, configurtionId) VALUES
	(1, '2015-10-18 21:25:23', b'0', NULL, NULL, 0, 'EN', 'Import formalities', 'IMPORT_FORM_KEY', NULL),
	(2, '2015-10-18 21:25:23', b'0', NULL, NULL, 0, 'NL', 'Import formaliteiten', 'IMPORT_FORM_KEY', NULL),
	(3, '2015-10-18 21:25:23', b'0', NULL, NULL, 0, 'FR', 'Frais d''import', 'IMPORT_FORM_KEY', NULL),
	(4, '2015-10-18 21:25:23', b'0', NULL, '2015-10-18 22:03:31', 1, 'DE', 'Einfuhrformalitäten', 'IMPORT_FORM_KEY', NULL),
	(5, '2015-10-18 21:27:02', b'0', NULL, NULL, 0, 'EN', 'Export formailities', 'EXPORT_FORM_KEY', NULL),
	(6, '2015-10-18 21:27:02', b'0', NULL, NULL, 0, 'NL', 'Export formaliteiten', 'EXPORT_FORM_KEY', NULL),
	(7, '2015-10-18 21:27:02', b'0', NULL, NULL, 0, 'FR', 'Frais d''export', 'EXPORT_FORM_KEY', NULL),
	(8, '2015-10-18 21:27:02', b'0', NULL, '2015-10-18 22:04:20', 1, 'DE', 'Ausfuhrformalitäten', 'EXPORT_FORM_KEY', NULL),
	(9, '2015-10-18 21:27:45', b'0', NULL, NULL, 0, 'EN', 'EUR1 document', 'EUR1_DOCUMENT_KEY', NULL),
	(10, '2015-10-18 21:27:45', b'0', NULL, NULL, 0, 'NL', 'EUR1 document', 'EUR1_DOCUMENT_KEY', NULL),
	(11, '2015-10-18 21:27:45', b'0', NULL, NULL, 0, 'FR', 'document EUR1', 'EUR1_DOCUMENT_KEY', NULL),
	(12, '2015-10-18 21:27:45', b'0', NULL, '2015-10-18 22:04:59', 1, 'DE', 'EUR1-Dokument', 'EUR1_DOCUMENT_KEY', NULL),
	(13, '2015-10-18 21:28:42', b'0', NULL, NULL, 0, 'EN', 'ADR surcharge', 'ADR_SURCHARGE_KEY', NULL),
	(14, '2015-10-18 21:28:42', b'0', NULL, NULL, 0, 'NL', 'ADR toeslag', 'ADR_SURCHARGE_KEY', NULL),
	(15, '2015-10-18 21:28:42', b'0', NULL, NULL, 0, 'FR', 'surcharge ADR', 'ADR_SURCHARGE_KEY', NULL),
	(16, '2015-10-18 21:28:42', b'0', NULL, '2015-10-18 22:05:28', 1, 'DE', 'ADR Zuschlag', 'ADR_SURCHARGE_KEY', NULL),
	(17, '2015-10-18 21:29:25', b'0', NULL, NULL, 0, 'EN', 'minimum ADR surcharge', 'ADR_MINIMUM_KEY', NULL),
	(18, '2015-10-18 21:29:25', b'0', NULL, NULL, 0, 'NL', 'minimum ADR toeslag', 'ADR_MINIMUM_KEY', NULL),
	(19, '2015-10-18 21:29:25', b'0', NULL, '2015-10-18 21:30:01', 1, 'FR', 'surcharge ADR minimum', 'ADR_MINIMUM_KEY', NULL),
	(20, '2015-10-18 21:29:25', b'0', NULL, '2015-10-18 22:06:01', 1, 'DE', 'Mindest ADR Zuschlag', 'ADR_MINIMUM_KEY', NULL),
	(21, '2015-10-18 21:35:52', b'0', NULL, NULL, 0, 'EN', '3€/per suppl. tarifcode (>3th)', 'SUPPL_TARIF_KEY', NULL),
	(22, '2015-10-18 21:35:52', b'0', NULL, NULL, 0, 'NL', '3€/per extra tariefcode (vanaf het 4de)', 'SUPPL_TARIF_KEY', NULL),
	(23, '2015-10-18 21:35:52', b'0', NULL, NULL, 0, 'FR', '3€/code supplémentaire de tarif (>3)', 'SUPPL_TARIF_KEY', NULL),
	(24, '2015-10-18 21:35:52', b'0', NULL, NULL, 0, 'DE', '', 'SUPPL_TARIF_KEY', NULL),
	(25, '2015-10-18 21:38:09', b'0', NULL, NULL, 0, 'EN', '55€/waiting hour (2 included for loading full trucks)', 'WACHT_TARIF_KEY', NULL),
	(26, '2015-10-18 21:38:09', b'0', NULL, NULL, 0, 'NL', '55€/per wachtuur (2 uur inclusief voor het laden van volle vrachtwagens)', 'WACHT_TARIF_KEY', NULL),
	(27, '2015-10-18 21:38:09', b'0', NULL, NULL, 0, 'FR', '55€/heur d''attente (2h compris pour charger/decharger un full load)', 'WACHT_TARIF_KEY', NULL),
	(28, '2015-10-18 21:38:09', b'0', NULL, NULL, 0, 'DE', '', 'WACHT_TARIF_KEY', NULL),
	(29, '2015-10-18 21:40:26', b'0', NULL, NULL, 0, 'EN', 'All invoices payable 30 days after invoicedate.', 'PAYMENT_TERMS_KEY', NULL),
	(30, '2015-10-18 21:40:26', b'0', NULL, NULL, 0, 'NL', 'Alle facturen betaalbaar 30 dagen na factuurdatum.', 'PAYMENT_TERMS_KEY', NULL),
	(31, '2015-10-18 21:40:26', b'0', NULL, NULL, 0, 'FR', 'Toutes les factures sont payables 30 jours fin de mois.', 'PAYMENT_TERMS_KEY', NULL),
	(32, '2015-10-18 21:40:26', b'0', NULL, '2015-10-18 22:02:37', 1, 'DE', 'Alle Rechnungen zahlbar 30 Tage nach Rechnungsdatum', 'PAYMENT_TERMS_KEY', NULL),
	(33, '2015-10-18 21:47:38', b'0', NULL, NULL, 0, 'EN', 'All transport is covered by CMR insurance. All Risk insurances can be demanded by customer, we offer the best rates.', 'TRANSPORT_INS_KEY', NULL),
	(34, '2015-10-18 21:47:38', b'0', NULL, NULL, 0, 'NL', 'Alle transport wordt verricht onder CMR verzekering. All Risk verzekeringen kunnen aangevraagd worden door de client, wij offreren de best prijzen.', 'TRANSPORT_INS_KEY', NULL),
	(35, '2015-10-18 21:47:38', b'0', NULL, NULL, 0, 'FR', 'Tout transport est effectué en vertu de l''assurance CMR. Assurance tous risques peut être demandé par le client, nous offrons les meilleurs prix.', 'TRANSPORT_INS_KEY', NULL),
	(36, '2015-10-18 21:47:38', b'0', NULL, '2015-10-18 22:00:30', 1, 'DE', 'Alle Transport wird durch CMR-Versicherung abgedeckt. Alle Risikoversicherungen kann vom Kunden gefordert werden, bieten wir die besten Preise.', 'TRANSPORT_INS_KEY', NULL),
	(37, '2015-10-18 21:50:36', b'0', NULL, NULL, 0, 'EN', 'All refused or returned shipments will be charged at 75% of the original price.', 'RETOUR_REFUSED_DELIVERY_KEY', NULL),
	(38, '2015-10-18 21:50:36', b'0', NULL, NULL, 0, 'NL', 'Alle geweigerde of geretourneerde zendingen worden afgerekend aan 75% van de originele vrachtprijs. ', 'RETOUR_REFUSED_DELIVERY_KEY', NULL),
	(39, '2015-10-18 21:50:36', b'0', NULL, NULL, 0, 'FR', 'Tous les envois refusés ou retournés seront facturés à 75% du fret d''origine.', 'RETOUR_REFUSED_DELIVERY_KEY', NULL),
	(40, '2015-10-18 21:50:36', b'0', NULL, '2015-10-18 22:01:39', 1, 'DE', 'Alle abzulehnen oder zurück Sendungen werden mit 75% des ursprünglichen Preises in Rechnung gestellt.', 'RETOUR_REFUSED_DELIVERY_KEY', NULL),
	(41, '2015-10-18 21:54:14', b'0', NULL, NULL, 0, 'EN', 'Rate valid until 31/12/2016', 'TARIF_VALID_TO_KEY', NULL),
	(42, '2015-10-18 21:54:14', b'0', NULL, NULL, 0, 'NL', 'Tarief geldig tot 31/12/2016', 'TARIF_VALID_TO_KEY', NULL),
	(43, '2015-10-18 21:54:14', b'0', NULL, NULL, 0, 'FR', 'Offre valable jusqu''au 31/12/2016', 'TARIF_VALID_TO_KEY', NULL),
	(44, '2015-10-18 21:54:14', b'0', NULL, NULL, 0, 'DE', 'Angebot gültig bis 2016.12.31', 'TARIF_VALID_TO_KEY', NULL),
	(45, '2015-10-18 21:57:36', b'0', NULL, NULL, 0, 'EN', 'Rate valid on DDP terms.', 'TARIF_FRANCO_HOUSE_KEY', NULL),
	(46, '2015-10-18 21:57:36', b'0', NULL, NULL, 0, 'NL', 'Tarief geldig aan Franko voorwaarden.', 'TARIF_FRANCO_HOUSE_KEY', NULL),
	(47, '2015-10-18 21:57:36', b'0', NULL, '2015-10-18 21:59:47', 2, 'FR', 'Tarif valable aux conditions DDP.', 'TARIF_FRANCO_HOUSE_KEY', NULL),
	(48, '2015-10-18 21:57:36', b'0', NULL, '2015-10-18 21:59:46', 1, 'DE', 'Bewerten gültig auf DDP-Klauseln.', 'TARIF_FRANCO_HOUSE_KEY', NULL),
	(49, '2015-10-20 23:11:53', b'0', NULL, NULL, 0, 'EN', 'Loading Meters', 'LDM_KEY', NULL),
	(50, '2015-10-20 23:11:53', b'0', NULL, NULL, 0, 'NL', 'Laadmeter', 'LDM_KEY', NULL),
	(51, '2015-10-20 23:11:53', b'0', NULL, NULL, 0, 'FR', 'mètres de chargement', 'LDM_KEY', NULL),
	(52, '2015-10-20 23:11:53', b'0', NULL, NULL, 0, 'DE', 'Lademeter', 'LDM_KEY', NULL),
	(53, '2015-10-20 23:12:38', b'0', NULL, NULL, 0, 'EN', 'kilos', 'KILO_KEY', NULL),
	(54, '2015-10-20 23:12:38', b'0', NULL, NULL, 0, 'NL', 'kilo''s', 'KILO_KEY', NULL),
	(55, '2015-10-20 23:12:38', b'0', NULL, NULL, 0, 'FR', 'kilos', 'KILO_KEY', NULL),
	(56, '2015-10-20 23:12:38', b'0', NULL, NULL, 0, 'DE', 'Kilo', 'KILO_KEY', NULL),
	(57, '2015-10-20 23:13:10', b'0', NULL, NULL, 0, 'EN', 'paletten', 'PALET_KEY', NULL),
	(58, '2015-10-20 23:13:10', b'0', NULL, NULL, 0, 'NL', 'palettes', 'PALET_KEY', NULL),
	(59, '2015-10-20 23:13:10', b'0', NULL, NULL, 0, 'FR', 'palettes', 'PALET_KEY', NULL),
	(60, '2015-10-20 23:13:10', b'0', NULL, NULL, 0, 'DE', 'palettes', 'PALET_KEY', NULL);
	
