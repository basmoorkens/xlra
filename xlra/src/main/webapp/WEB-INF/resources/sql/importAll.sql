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
