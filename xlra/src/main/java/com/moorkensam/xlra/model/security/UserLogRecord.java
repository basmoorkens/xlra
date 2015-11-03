package com.moorkensam.xlra.model.security;

import com.moorkensam.xlra.model.configuration.LogRecord;

public class UserLogRecord extends LogRecord {

	private static final long serialVersionUID = 2081940571427192204L;

	private String affectedAccount;

	public String getAffectedAccount() {
		return affectedAccount;
	}

	public void setAffectedAccount(String affectedAccount) {
		this.affectedAccount = affectedAccount;
	}

}
