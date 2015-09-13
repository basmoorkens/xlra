INSERT INTO RATEFILE(deleted, kindOfRate, measurement, countryId, name, conditionId) VALUES (0, 'NORMAL','KILO', 1, 'Belgium - normal - kilo', 1);
INSERT INTO RATEFILE(deleted, kindOfRate, measurement, countryId, name) VALUES (0, 'NORMAL','PALET', 1, 'Belgium - normal - Palet');
INSERT INTO RATEFILE(deleted, kindOfRate, measurement, countryId, name) VALUES (0, 'NORMAL','LDM', 1, 'Belgium - normal - LDM');
INSERT INTO RATEFILE(deleted, kindOfRate, measurement, countryId, name) VALUES (0, 'EXPRES','KILO', 1, 'Belgium - expres - kilo');
INSERT INTO RATEFILE(deleted, kindOfRate, measurement, countryId, name) VALUES (0, 'EXPRES','LDM', 1, 'Belgium - expres - LDM');
INSERT INTO RATEFILE(deleted, kindOfRate, measurement, countryId, name) VALUES (0, 'EXPRES','PALET', 1, 'Belgium - expres - palet');

insert into rateline(deleted, measurement, zone, value, ratefileid) values (0, 'KILO', '1000', 250, 1);
insert into rateline(deleted, measurement, zone, value, ratefileid) values (0, 'KILO', '2000', 260, 1);
insert into rateline(deleted, measurement, zone, value, ratefileid) values (0, 'KILO', '3000', 270, 1);
insert into rateline(deleted, measurement, zone, value, ratefileid) values (0, 'KILO', '4000', 300, 1);
insert into rateline(deleted, measurement, zone, value, ratefileid) values (0, 'KILO', '5000', 310, 1);
insert into rateline(deleted, measurement, zone, value, ratefileid) values (0, 'KILO', '6000', 350, 1);