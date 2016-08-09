package com.moorkensam.xlra.model.offerte;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.mail.EmailHistoryRecord;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.rate.Country;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Cacheable
@Table(name = "quotationresult")
@NamedQueries({
    @NamedQuery(name = "QuotationResult.findAll",
        query = "SELECT q FROM QuotationResult q where q.deleted = false"),
    @NamedQuery(name = "QuotationResult.findByKey",
        query = "SELECT q FROM QuotationResult q where q.offerteUniqueIdentifier = :key"),
    @NamedQuery(name = "QuotationResult.countOffertes",
        query = "SELECT count(q.id) FROM QuotationResult q ")})
public class QuotationResult extends BaseEntity {

  private static final long serialVersionUID = -8105357874994501600L;

  @OneToOne
  @JoinColumn(name = "calculation_id")
  private PriceCalculation calculation;

  @Column(name = "pdf_file_name")
  private String pdfFileName;

  @Embedded
  private EmailResult emailResult;

  @Column(name = "created_user_full_name")
  private String createdUserFullName;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "quotation_query_id")
  private QuotationQuery query;

  @OneToMany(mappedBy = "offerte")
  private List<EmailHistoryRecord> emailHistory;

  private String offerteUniqueIdentifier;

  @Column(name = "used_ratefilename")
  private String usedRateFileName;

  @Column(name = "created_username")
  private String createdUserName;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private QuotationResultStatus quotationResultStatus;

  @Transient
  private List<OfferteOptionDto> selectableOptions;

  @Transient
  private List<String> availableEmailRecipients;

  public QuotationResult() {
    this.selectableOptions = new ArrayList<OfferteOptionDto>();
    this.emailResult = new EmailResult();
  }

  public EmailResult getEmailResult() {
    return emailResult;
  }

  public void setEmailResult(EmailResult emailResult) {
    this.emailResult = emailResult;
  }

  public QuotationQuery getQuery() {
    return query;
  }

  public void setQuery(QuotationQuery query) {
    this.query = query;
  }

  public String getOfferteUniqueIdentifier() {
    return offerteUniqueIdentifier;
  }

  public void setOfferteUniqueIdentifier(String offerteUniqueIdentifier) {
    this.offerteUniqueIdentifier = offerteUniqueIdentifier;
  }

  public PriceCalculation getCalculation() {
    return calculation;
  }

  public void setCalculation(PriceCalculation calculation) {
    this.calculation = calculation;
  }

  public String getPdfFileName() {
    return pdfFileName;
  }

  public void setPdfFileName(String pdfFileName) {
    this.pdfFileName = pdfFileName;
  }

  public String getCreatedUserFullName() {
    return createdUserFullName;
  }

  public void setCreatedUserFullName(String createdUserFullName) {
    this.createdUserFullName = createdUserFullName;
  }

  public List<OfferteOptionDto> getSelectableOptions() {
    return selectableOptions;
  }

  public void setSelectableOptions(List<OfferteOptionDto> selectableOptions) {
    this.selectableOptions = selectableOptions;
  }

  /**
   * get the country.
   * 
   * @return the country
   */
  public Country getCountry() {
    if (getQuery() == null) {
      return null;
    }
    return getQuery().getCountry();
  }

  public List<EmailHistoryRecord> getEmailHistory() {
    return emailHistory;
  }

  public void setEmailHistory(List<EmailHistoryRecord> emailHistory) {
    this.emailHistory = emailHistory;
  }

  /**
   * add an email history record.
   * 
   * @param record the record to add.
   */
  public void addEmailHistoryRecord(EmailHistoryRecord record) {
    if (emailHistory == null) {
      emailHistory = new ArrayList<EmailHistoryRecord>();
    }
    emailHistory.add(record);
  }

  /**
   * Fetches the offerte country of the quotation query if present.
   * 
   * @return the offerte country.
   */
  @Transient
  public Country getOfferteCountry() {
    if (query != null) {
      return query.getCountry();
    }
    return null;
  }

  public String getUsedRateFileName() {
    return usedRateFileName;
  }

  public void setUsedRateFileName(String usedRateFileName) {
    this.usedRateFileName = usedRateFileName;
  }

  public List<String> getAvailableEmailRecipients() {
    return availableEmailRecipients;
  }

  public void setAvailableEmailRecipients(List<String> availableEmailRecipients) {
    this.availableEmailRecipients = availableEmailRecipients;
  }

  /**
   * Convenience method to add recipients.
   * 
   * @param recipient The recipient to add.
   */
  public void addAvailableEmailRecipients(String recipient) {
    if (availableEmailRecipients == null) {
      availableEmailRecipients = new ArrayList<String>();
    }
    availableEmailRecipients.add(recipient);
  }

  public String getCreatedUserName() {
    return createdUserName;
  }

  public void setCreatedUserName(String createdUserName) {
    this.createdUserName = createdUserName;
  }

  public QuotationResultStatus getQuotationResultStatus() {
    return quotationResultStatus;
  }

  public void setQuotationResultStatus(QuotationResultStatus quotationResultStatus) {
    this.quotationResultStatus = quotationResultStatus;
  }

}
