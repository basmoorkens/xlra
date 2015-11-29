package com.moorkensam.xlra.model.offerte;

import java.io.File;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.mail.EmailResult;
import com.moorkensam.xlra.model.rate.RateFile;

@Entity
@Cacheable
@Table(name = "quotationresult")
@NamedQueries(@NamedQuery(name = "QuotationResult.findAll", query = "SELECT q FROM QuotationResult q where q.deleted = false"))
public class QuotationResult extends BaseEntity {

	private static final long serialVersionUID = -8105357874994501600L;

	@OneToOne
	@JoinColumn(name = "calculation_id")
	private PriceCalculation calculation;

	@Column(name = "pdf_file_name")
	private String pdfFileName;

	@Embedded
	private EmailResult emailResult;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quotation_query_id")
	private QuotationQuery query;

	/**
	 * There is no guarantee that the ratefile that was used to generate this
	 * quotation result is still the same. So its just here for a future
	 * reference for reporting.
	 */
	@ManyToOne
	@JoinColumn(name = "ratefileid")
	private RateFile rateFile;

	private String offerteUniqueIdentifier;

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
}
