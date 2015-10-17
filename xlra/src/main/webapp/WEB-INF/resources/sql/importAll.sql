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

INSERT INTO COUNTRY (deleted, name, shortname, version, zonetype) VALUES (0, 'Belgium', 'BE', 0, 'NUMERIC_CODES');
INSERT INTO COUNTRY (deleted, name, shortname, version, zonetype) VALUES (0, 'Netherlands', 'NL', 0, 'NUMERIC_CODES');
INSERT INTO COUNTRY (deleted, name, shortname, version, zonetype) VALUES (0, 'Switzerland', 'CH', 0, 'ALPHANUMERIC_LIST');
INSERT INTO COUNTRY (deleted, name, shortname, version, zonetype) VALUES (0, 'United kingdom', 'UK', 0, 'ALPHANUMERIC_LIST');