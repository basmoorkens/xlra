INSERT INTO CONFIGURATION (deleted, configurationName, currentChfValue, currentDieselPrice, version)
VALUES (0, 'mainconfig', 0, 0, 0);

insert into currencyrate (start, end, surchargePercentage, deleted, currencyType, version) values (1.1, 1.2, 1,0, 'CHF', 0);
insert into currencyrate (start, end, surchargePercentage, deleted, currencyType, version) values (1.2, 1.3, 1.1,0, 'CHF', 0);
insert into currencyrate (start, end, surchargePercentage, deleted, currencyType, version) values (1.3, 1.4, 1.3,0, 'CHF', 0);
insert into currencyrate (start, end, surchargePercentage, deleted, currencyType, version) values (1.4, 1.5, 1.5,0, 'CHF', 0);
insert into currencyrate (start, end, surchargePercentage, deleted, currencyType, version) values (1.5, 2.0, 2, 0, 'CHF', 0);

insert into DieselRate (start, end, surchargePercentage, deleted, version) values (1.1, 1.2, 1,0, 0);
insert into DieselRate (start, end, surchargePercentage, deleted, version) values (1.2, 1.3, 1.1,0, 0);
insert into DieselRate (start, end, surchargePercentage, deleted, version) values (1.3, 1.4, 1.3,0, 0);
insert into DieselRate (start, end, surchargePercentage, deleted, version) values (1.4, 1.5, 1.5,0, 0);
insert into DieselRate (start, end, surchargePercentage, deleted, version) values (1.5, 2.0, 2, 0, 0);

INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0,  'BE', 0, 'NUMERIC_CODES');
INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0,  'NL', 0, 'NUMERIC_CODES');
INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0,  'CH', 0, 'ALPHANUMERIC_LIST');
INSERT INTO COUNTRY (deleted,  shortname, version, zonetype) VALUES (0,  'UK', 0, 'ALPHANUMERIC_LIST');

insert into mailTemplate(deleted, version, language, template, xlraconfigurationid) values (0, 0, 'NL', 'ok',1);
insert into mailTemplate(deleted, version, language, template, xlraconfigurationid) values (0, 0, 'EN', 'ok',1);
insert into mailTemplate(deleted, version, language, template, xlraconfigurationid) values (0, 0, 'FR', 'ok',1);
insert into mailTemplate(deleted, version, language, template, xlraconfigurationid) values (0, 0, 'DE', 'ok',1);