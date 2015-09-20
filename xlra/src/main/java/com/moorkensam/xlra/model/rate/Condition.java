package com.moorkensam.xlra.model.rate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "conditions")
public class Condition extends BaseEntity {

	private static final long serialVersionUID = 7814328305384417198L;

	private double importFormality;

	private double exportFormality;

	private double eur1;

	private double adrSurcharge;

	private double adrSurchargeMinimum;

	private String supplementaryTarifNumbers;

	@Enumerated(EnumType.STRING)
	private IncoTermType incoTermType;

	private String waitHourTarif;

	private String paymentTerm;

	private String transportInsurance;

	private String expirationDate;

	public double getImportFormality() {
		return importFormality;
	}

	public void setImportFormality(double importFormality) {
		this.importFormality = importFormality;
	}

	public double getExportFormality() {
		return exportFormality;
	}

	public void setExportFormality(double exportFormality) {
		this.exportFormality = exportFormality;
	}

	public double getEur1() {
		return eur1;
	}

	public void setEur1(double eur1) {
		this.eur1 = eur1;
	}

	public double getAdrSurcharge() {
		return adrSurcharge;
	}

	public void setAdrSurcharge(double adrSurcharge) {
		this.adrSurcharge = adrSurcharge;
	}

	public double getAdrSurchargeMinimum() {
		return adrSurchargeMinimum;
	}

	public void setAdrSurchargeMinimum(double adrSurchargeMinimum) {
		this.adrSurchargeMinimum = adrSurchargeMinimum;
	}

	public String getSupplementaryTarifNumbers() {
		return supplementaryTarifNumbers;
	}

	public void setSupplementaryTarifNumbers(String supplementaryTarifNumbers) {
		this.supplementaryTarifNumbers = supplementaryTarifNumbers;
	}

	public IncoTermType getIncoTermType() {
		return incoTermType;
	}

	public void setIncoTermType(IncoTermType incoTermType) {
		this.incoTermType = incoTermType;
	}

	public String getWaitHourTarif() {
		return waitHourTarif;
	}

	public void setWaitHourTarif(String waitHourTarif) {
		this.waitHourTarif = waitHourTarif;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getTransportInsurance() {
		return transportInsurance;
	}

	public void setTransportInsurance(String transportInsurance) {
		this.transportInsurance = transportInsurance;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Condition deepCopy() {
		Condition c = new Condition();
		c.setAdrSurcharge(getAdrSurcharge());
		c.setAdrSurchargeMinimum(getAdrSurchargeMinimum());
		c.setEur1(getEur1());
		c.setExpirationDate(getExpirationDate());
		c.setExportFormality(getExportFormality());
		c.setImportFormality(getImportFormality());
		c.setIncoTermType(getIncoTermType());
		c.setPaymentTerm(getPaymentTerm());
		c.setSupplementaryTarifNumbers(getSupplementaryTarifNumbers());
		c.setTransportInsurance(getTransportInsurance());
		c.setWaitHourTarif(getWaitHourTarif());
		return c;
	}

}
