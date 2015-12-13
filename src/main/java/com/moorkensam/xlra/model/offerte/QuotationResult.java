package com.moorkensam.xlra.model.offerte;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.mail.EmailHistoryRecord;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.RateFile;

@Entity
@Cacheable
@Table(name = "quotationresult")
@NamedQueries({
    @NamedQuery(name = "QuotationResult.findAll",
        query = "SELECT q FROM QuotationResult q where q.deleted = false"),
    @NamedQuery(name = "QuotationResult.findByKey",
        query = "SELECT q FROM QuotationResult q where q.offerteUniqueIdentifier = :key")})
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

  /**
   * There is no guarantee that the ratefile that was used to generate this quotation result is
   * still the same. So its just here for a future reference for reporting.
   */
  @ManyToOne
  @JoinColumn(name = "ratefileid")
  private RateFile rateFile;

  private String offerteUniqueIdentifier;

  @Transient
  private List<OfferteOptionDto> selectableOptions;

  public QuotationResult() {
    this.selectableOptions = new ArrayList<OfferteOptionDto>();
  }

  public RateFile getRateFile() {
    return rateFile;
  }

  public void setRateFile(RateFile rateFile) {
    this.rateFile = rateFile;
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
}
