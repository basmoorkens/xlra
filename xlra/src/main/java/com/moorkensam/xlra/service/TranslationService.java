package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.configuration.Translation;

public interface TranslationService {
	void updateTranslation(Translation translation);

	List<Translation> getAllNonDeletedTranslations();
	
	void createTranslations(Translation[] translations);
}
