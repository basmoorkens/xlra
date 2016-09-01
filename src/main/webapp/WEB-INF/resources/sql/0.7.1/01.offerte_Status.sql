alter table quotationresult add column status varchar(20);

update quotationresult set status = 'FINALIZED';