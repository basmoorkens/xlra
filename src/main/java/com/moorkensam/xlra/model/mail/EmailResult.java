package com.moorkensam.xlra.model.mail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;

@Embeddable
public class EmailResult {

  @Lob
  @Column(name = "email")
  private String email;

  private String subject;

  @ElementCollection
  @CollectionTable(name = "offerte_recipients", joinColumns = @JoinColumn(name = "offerte_id"))
  private List<String> recipients;

  private boolean send;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isSend() {
    return send;
  }

  public void setSend(boolean send) {
    this.send = send;
  }

  public List<String> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<String> recipients) {
    this.recipients = recipients;
  }

  /**
   * Add a recipient to the list of recipients.
   * 
   * @param recipient the recipient to add
   */
  public void addRecipient(String recipient) {
    if (recipients == null) {
      recipients = new ArrayList<String>();
    }
    recipients.add(recipient);
  }

  /**
   * Returns the recipients as a String
   * 
   * @return The list of recipients as a string.
   */
  public String getRecipientsAsString() {
    if (recipients == null || recipients.size() == 0) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (String recipient : recipients) {
      builder.append(recipient + ", ");
    }
    return builder.toString();
  }
}
