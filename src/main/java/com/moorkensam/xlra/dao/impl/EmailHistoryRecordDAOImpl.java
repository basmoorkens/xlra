package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.EmailHistoryDAO;
import com.moorkensam.xlra.model.offerte.EmailHistoryRecord;

public class EmailHistoryRecordDAOImpl extends BaseDAO implements
		EmailHistoryDAO {

	@Override
	public void createEmailHistoryRecord(EmailHistoryRecord record) {
		getEntityManager().persist(record);
	}

}
