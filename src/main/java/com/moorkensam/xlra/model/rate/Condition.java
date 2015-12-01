package com.moorkensam.xlra.model.rate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.translation.Translatable;
import com.moorkensam.xlra.model.translation.TranslationKey;

@Entity
@Table(name = "conditions")
public class Condition extends BaseEntity implements Translatable {

	private static final long serialVersionUID = 7814328305384417198L;

	private String value;

	@Enumerated(EnumType.STRING)
	private TranslationKey conditionKey;

	@Column(name = "offerte_standard_selected")
	private boolean standardSelected;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private ConditionType conditionType;

	@ManyToOne
	@JoinColumn(name = "rateFileId")
	private RateFile rateFile;

	@Transient
	private String i8nKey;

	@Transient
	private String translatedValue;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public RateFile getRateFile() {
		return rateFile;
	}

	public void setRateFile(RateFile rateFile) {
		this.rateFile = rateFile;
	}

	public Condition deepCopy() {
		Condition c = new Condition();
		c.setValue(this.value);
		c.setRateFile(this.rateFile);
		c.setConditionKey(conditionKey);
		c.setStandardSelected(standardSelected);
		c.setI8nKey(i8nKey);
		return c;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Condition))
			return false;
		Condition other = (Condition) obj;
		if (other.getId() == id) {
			return true;
		}
		return false;
	}

	public TranslationKey getConditionKey() {
		return conditionKey;
	}

	public void setConditionKey(TranslationKey conditionKey) {
		this.conditionKey = conditionKey;
	}

	public boolean isStandardSelected() {
		return standardSelected;
	}

	public void setStandardSelected(boolean standardSelected) {
		this.standardSelected = standardSelected;
	}

	public ConditionType getConditionType() {
		return conditionType;
	}

	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}

	public String getI8nKey() {
		return i8nKey;
	}

	public void setI8nKey(String i8nKey) {
		this.i8nKey = i8nKey;
	}

	public String getTranslatedValue() {
		return translatedValue;
	}

	public void setTranslatedValue(String translatedValue) {
		this.translatedValue = translatedValue;
	}
}
