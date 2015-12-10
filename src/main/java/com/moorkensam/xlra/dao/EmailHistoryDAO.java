package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.mail.EmailHistoryRecord;

public interface EmailHistoryDAO {

	void createEmailHistoryRecord(EmailHistoryRecord record);
}
