package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.EmailHistoryDao;
import com.moorkensam.xlra.model.mail.EmailHistoryRecord;

public class EmailHistoryRecordDaoImpl extends BaseDao implements EmailHistoryDao {

  @Override
  public void createEmailHistoryRecord(EmailHistoryRecord record) {
    getEntityManager().persist(record);
  }

}
