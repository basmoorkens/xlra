package com.moorkensam.xlra.model.configuration;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.Language;

@Entity
@Cacheable
@Table(name = "xlratranslation")
public class Translation extends BaseEntity {

	private static final long serialVersionUID = 2952050080864430058L;

	@Enumerated(EnumType.STRING)
	private TranslationKey translationKey;

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

	public TranslationKey getTranslationKey() {
		return translationKey;
	}

	public void setTranslationKey(TranslationKey translationKey) {
		this.translationKey = translationKey;
	}
}
