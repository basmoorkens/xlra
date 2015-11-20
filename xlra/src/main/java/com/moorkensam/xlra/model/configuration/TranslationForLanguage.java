package com.moorkensam.xlra.model.configuration;

import javax.persistence.Embeddable;

import com.moorkensam.xlra.model.Language;

@Embeddable
public class TranslationForLanguage {

	private String translation;

	private Language language;

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public TranslationForLanguage deepCopy() {
		TranslationForLanguage copy = new TranslationForLanguage();
		copy.setLanguage(this.language);
		copy.setTranslation(this.translation);
		return copy;
	}

}
