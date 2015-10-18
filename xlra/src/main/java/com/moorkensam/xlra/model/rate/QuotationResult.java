package com.moorkensam.xlra.model.rate;

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

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.EmailResult;
import com.moorkensam.xlra.model.QuotationQuery;

@Entity
@Cacheable
@Table(name = "quotationresult")
@NamedQueries(@NamedQuery(name = "QuotationResult.findAll", query = "SELECT q FROM QuotationResult q where q.deleted = false"))
public class QuotationResult extends BaseEntity {

	private static final long serialVersionUID = -8105357874994501600L;

	@Lob
	@Column(name = "pdf")
	private byte[] generatedPdf;

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

	public RateFile getRateFile() {
		return rateFile;
	}

	public void setRateFile(RateFile rateFile) {
		this.rateFile = rateFile;
	}

	public byte[] getGeneratedPdf() {
		return generatedPdf;
	}

	public void setGeneratedPdf(byte[] generatedPdf) {
		this.generatedPdf = generatedPdf;
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
}
