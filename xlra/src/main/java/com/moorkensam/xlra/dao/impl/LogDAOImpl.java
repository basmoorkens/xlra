package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.LogDAO;
import com.moorkensam.xlra.model.configuration.LogRecord;

public class LogDAOImpl extends BaseDAO implements LogDAO {

	@Override
	public void createLogRecord(LogRecord record) {
		getEntityManager().persist(record);
	}
}
