package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.TranslationDAO;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.service.TranslationService;

@Stateless
public class TranslationServiceImpl implements TranslationService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private TranslationDAO translationDAO;

	@Override
	public void updateTranslation(Translation translation) {
		logger.info("Updating translation for key "
				+ translation.getTranslationKey() + " and language "
				+ translation.getLanguage());
		;
		translationDAO.updateTranslation(translation);
	}

	@Override
	public List<Translation> getAllNonDeletedTranslations() {
		return translationDAO.getAllNonDeletedTranslations();
	}

	@Override
	public void createTranslations(Translation[] translations) {
		for (Translation translation : translations) {
			translationDAO.createTranslation(translation);
		}
	}
}
