package com.moorkensam.xlra.model.log;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "quotationLogRecord")
@NamedQueries({ @NamedQuery(name = "QuotationLogRecord.getQuotationLogRecordsByDate", query = "SELECT q FROM QuotationLogRecord q WHERE q.logDate > :startDate AND q.logDate < :endDate") })
public class QuotationLogRecord extends LogRecord {

	private static final long serialVersionUID = 3740455597206707650L;

	private String offerteKey;

	private String createdUserName;

	private String customerName;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public String getOfferteKey() {
		return offerteKey;
	}

	public void setOfferteKey(String offerteKey) {
		this.offerteKey = offerteKey;
	}

}
