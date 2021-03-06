package com.moorkensam.xlra.model.security;

import com.moorkensam.xlra.model.log.LogRecord;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "userlogrecord")
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
