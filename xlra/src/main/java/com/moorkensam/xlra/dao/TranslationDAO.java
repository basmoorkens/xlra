package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.translation.Translation;
import com.moorkensam.xlra.model.translation.TranslationKey;

public interface TranslationDAO {

	List<TranslationKey> getDistinctTranslationKeys();

	List<Translation> getAllNonDeletedTranslations();
	
	void updateTranslation(Translation translation);
	
	void createTranslation(Translation translation);
	
}
