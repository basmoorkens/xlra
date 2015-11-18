package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.TranslationDAO;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.service.TranslationService;

/**
 * Contains methods for updating and fetching translations.
 * 
 * @author bas
 *
 */
@Stateless
public class TranslationServiceImpl implements TranslationService {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private TranslationDAO translationDAO;

	@Override
	public void updateTranslation(Translation translation) {
		logger.info("Updating translation for key "
				+ translation.getTranslationKey());
		getTranslationDAO().updateTranslation(translation);
	}

	@Override
	public List<Translation> getAllNonDeletedTranslations() {
		return getTranslationDAO().getAllNonDeletedTranslations();
	}

	@Override
	public void createTranslation(Translation translation)
			throws XlraValidationException {
		if (validateTranslation(translation)) {
			getTranslationDAO().createTranslation(translation);
		}
	}

	private boolean validateTranslation(Translation translation)
			throws XlraValidationException {
		if (translation.getTranslationKey() == null) {
			throw new XlraValidationException(
					"You can not create a translation without a key");
		}
		return true;
	}

	@Override
	public Translation findTranslationInCacheByTranslationKey(TranslationKey key) {
		if (logger.isDebugEnabled()) {
			logger.debug("Fetching translations in memory for " + key);
		}
		List<Translation> translations = getAllNonDeletedTranslations();
		for (Translation translation : translations) {
			if (translation.getTranslationKey() == key) {
				return translation;
			}
		}
		return null;
	}

	public TranslationDAO getTranslationDAO() {
		return translationDAO;
	}

	public void setTranslationDAO(TranslationDAO translationDAO) {
		this.translationDAO = translationDAO;
	}
}
