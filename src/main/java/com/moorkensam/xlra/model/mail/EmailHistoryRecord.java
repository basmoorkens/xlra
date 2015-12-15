package com.moorkensam.xlra.model.mail;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.offerte.QuotationResult;

@Entity
@Table(name = "emailhistoryrecord")
public class EmailHistoryRecord extends BaseEntity {

  private static final long serialVersionUID = 4167800588284752022L;

  private EmailSentStatus status;

  @Temporal(TemporalType.TIMESTAMP)
  private Date dateTime;

  private String username;

  @ManyToOne
  @JoinColumn(name = "offerte_id")
  private QuotationResult offerte;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }

  public EmailSentStatus getStatus() {
    return status;
  }

  public void setStatus(EmailSentStatus status) {
    this.status = status;
  }

  public QuotationResult getOfferte() {
    return offerte;
  }

  public void setOfferte(QuotationResult offerte) {
    this.offerte = offerte;
  }

}
