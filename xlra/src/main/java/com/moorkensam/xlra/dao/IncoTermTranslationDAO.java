package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.IncoTermTranslation;
import com.moorkensam.xlra.model.rate.IncoTermType;

public interface IncoTermTranslationDAO {

	public IncoTermTranslation findTranslationByLanguageAndIncoTermType(Language language, IncoTermType incoTermType);
	
	void updateIncoTermTranslation(IncoTermTranslation incoTermTranslation); 
}
