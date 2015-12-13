package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.mail.EmailHistoryRecord;

public interface EmailHistoryDao {

  void createEmailHistoryRecord(EmailHistoryRecord record);
}
