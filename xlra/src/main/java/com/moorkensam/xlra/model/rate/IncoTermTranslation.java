package com.moorkensam.xlra.model.rate;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Language;

@Entity
@Cacheable
@Table(name = "incotermtranslation")
@NamedQueries(
		@NamedQuery(name = "IncoTermTranslation.findByLanguageAndIncoTermType", 
					query = "SELECT i FROM IncoTermTranslation i WHERE i.language = :language and i.incoTermType = :incoTermType"))
public class IncoTermTranslation extends BaseEntity {

	private static final long serialVersionUID = -3231060510615082478L;

	@Enumerated(EnumType.STRING)
	private IncoTermType incoTermType;

	private String text;

	@Enumerated(EnumType.STRING)
	private Language language;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public IncoTermType getIncoTermType() {
		return incoTermType;
	}

	public void setIncoTermType(IncoTermType incoTermType) {
		this.incoTermType = incoTermType;
	}

}
