package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationKey;

public interface TranslationService {
	void updateTranslation(Translation translation);

	List<Translation> getAllNonDeletedTranslations();

	void createTranslation(Translation translation);

	/**
	 * Fetches a translation from memory.
	 * 
	 * @param key
	 * @param language
	 * @return
	 */
	Translation findTranslationInCacheByTranslationKey(TranslationKey key);
}
