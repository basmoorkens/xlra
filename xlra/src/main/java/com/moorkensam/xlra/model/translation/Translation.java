package com.moorkensam.xlra.model.translation;

import java.beans.Transient;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.configuration.TranslationForLanguage;

@Entity
@Cacheable
@Table(name = "xlratranslation")
@NamedQueries({
		@NamedQuery(name = "Translation.findDistinctKeys", query = "SELECT t FROM Translation t GROUP BY t.translationKey"),
		@NamedQuery(name = "Translation.findAllNonDeleted", query = "SELECT t FROM Translation t WHERE t.deleted = false ORDER BY t.translationKey") })
public class Translation extends BaseEntity implements Comparable<Translation> {

	private static final long serialVersionUID = 2952050080864430058L;

	@Enumerated(EnumType.STRING)
	private TranslationKey translationKey;

	@ElementCollection
	@CollectionTable(name = "translationsForLanguages", joinColumns = @JoinColumn(name = "translation_id"))
	private List<TranslationForLanguage> translations;

	@ElementCollection
	@CollectionTable(name = "translationKeysForLanguages", joinColumns = @JoinColumn(name = "translation_id"))
	private List<TranslationForLanguage> translationKeysTranslations;

	@Transient
	public String getTranslationKeyAsString() {
		if (translationKey == null) {
			return "";
		}
		return translationKey.toString();
	}

	public TranslationKey getTranslationKey() {
		return translationKey;
	}

	public void setTranslationKey(TranslationKey translationKey) {
		this.translationKey = translationKey;
	}

	public TranslationForLanguage getTranslationForLanguage(Language language) {
		for (TranslationForLanguage tl : translations) {
			if (tl.getLanguage().equals(language)) {
				return tl;
			}
		}
		return null;
	}

	public TranslationForLanguage getTranslationKeyForLanguage(Language language) {
		for (TranslationForLanguage tl : translationKeysTranslations) {
			if (tl.getLanguage().equals(language)) {
				return tl;
			}
		}
		return null;
	}

	@Override
	public int compareTo(Translation o) {
		return getTranslationKey().compareTo(o.getTranslationKey());
	}

	public List<TranslationForLanguage> getTranslations() {
		return translations;
	}

	public void setTranslations(List<TranslationForLanguage> translations) {
		this.translations = translations;
	}

	public TranslationForLanguage getEnglishTranslation() {
		return getTranslationForLanguage(Language.EN);
	}

	public TranslationForLanguage getFrenchTranslation() {
		return getTranslationForLanguage(Language.FR);
	}

	public TranslationForLanguage getGermanTranslation() {
		return getTranslationForLanguage(Language.DE);
	}

	public TranslationForLanguage getDutchTranslation() {
		return getTranslationForLanguage(Language.NL);
	}

	public TranslationForLanguage getEnglishTranslationKey() {
		return getTranslationKeyForLanguage(Language.EN);
	}

	public TranslationForLanguage getFrenchTranslationKey() {
		return getTranslationKeyForLanguage(Language.FR);
	}

	public TranslationForLanguage getGermanTranslationKey() {
		return getTranslationKeyForLanguage(Language.DE);
	}

	public TranslationForLanguage getDutchTranslationKey() {
		return getTranslationKeyForLanguage(Language.NL);
	}

	public List<TranslationForLanguage> getTranslationKeysTranslations() {
		return translationKeysTranslations;
	}

	public void setTranslationKeysTranslations(
			List<TranslationForLanguage> translationKeysTranslations) {
		this.translationKeysTranslations = translationKeysTranslations;
	}

	public String getTranslationKeysString() {
		String result = "";
		if (translationKeysTranslations == null) {
			return result;
		}
		for (TranslationForLanguage tl : translationKeysTranslations) {
			result += tl.getLanguage().getDescription() + ", ";
		}
		if (result.isEmpty() || result.length() == 1) {
			return result;
		}
		return result.substring(0, result.length() - 2);
	}

	public String getTranslationsString() {
		String result = "";
		if (translations == null) {
			return result;
		}
		for (TranslationForLanguage tl : translations) {
			result += tl.getLanguage().getDescription() + ", ";
		}
		if (result.isEmpty() || result.length() == 1) {
			return result;
		}
		return result.substring(0, result.length() - 2);
	}
}
