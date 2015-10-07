package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationKey;

public interface TranslationService {
	void updateTranslation(Translation translation);

	List<Translation> getAllNonDeletedTranslations();

	void createTranslations(Translation[] translations);

	Translation findTranslationInCacheByTranslationKeyAndLanguage(
			TranslationKey key, Language language);
}
