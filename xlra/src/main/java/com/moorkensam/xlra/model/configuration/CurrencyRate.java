package com.moorkensam.xlra.model.configuration;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "currencyrate")
@NamedQueries({
		@NamedQuery(name = "CurrencyRate.findAll", query = "SELECT c FROM CurrencyRate c where c.deleted = false"),
		@NamedQuery(name = "CurrencyRate.findAllChf", query = "SELECT c FROM CurrencyRate c WHERE c.deleted = false and c.currencyType = com.moorkensam.xlra.model.configuration.XLRACurrency.CHF") })
public class CurrencyRate extends AbstractRate {

	private static final long serialVersionUID = -7687370103653996637L;

	@Enumerated(EnumType.STRING)
	@NotNull
	private XLRACurrency currencyType;

	public XLRACurrency getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(XLRACurrency currencyType) {
		this.currencyType = currencyType;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			CurrencyRate cr = (CurrencyRate) obj;
			if (this.getCurrencyType().equals(cr.getCurrencyType())) {
				return true;
			}
		}
		return false;
	}

}
