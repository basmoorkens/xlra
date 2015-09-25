package com.moorkensam.xlra.model.configuration;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.Language;

@Entity
@Cacheable
@Table(name = "xlratranslation")
@NamedQueries({
		@NamedQuery(name = "Translation.findDistinctKeys", query = "SELECT t FROM Translation t GROUP BY t.translationKey"),
		@NamedQuery(name = "Translation.findAllNonDeleted", query = "SELECT t FROM Translation t WHERE t.deleted = false ORDER BY t.translationKey") })
public class Translation extends BaseEntity implements Comparable<Translation> {

	private static final long serialVersionUID = 2952050080864430058L;

	@ManyToOne
	@JoinColumn(name = "configurtionId")
	private Configuration configuration;

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

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public int compareTo(Translation o) {
		return getTranslationKey().compareTo(o.getTranslationKey());
	}
}
