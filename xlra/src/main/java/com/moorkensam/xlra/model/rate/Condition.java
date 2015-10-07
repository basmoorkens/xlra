package com.moorkensam.xlra.model.rate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationKey;

@Entity
@Table(name = "conditions")
public class Condition extends BaseEntity {

	private static final long serialVersionUID = 7814328305384417198L;

	@ManyToOne
	@JoinColumn(name = "translationId")
	private Translation translation;

	private String value;

	@ManyToOne
	@JoinColumn(name = "rateFileId")
	private RateFile rateFile;

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
		c.setTranslation(translation);
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

	public Translation getTranslation() {
		return translation;
	}

	public void setTranslation(Translation translation) {
		this.translation = translation;
	}

}
