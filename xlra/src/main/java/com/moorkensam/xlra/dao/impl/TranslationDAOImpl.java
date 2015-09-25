package com.moorkensam.xlra.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.TranslationDAO;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationKey;

public class TranslationDAOImpl extends BaseDAO implements TranslationDAO {

	@Override
	public List<TranslationKey> getDistinctTranslationKeys() {
		Query query = getEntityManager().createNamedQuery(
				"Translation.findDistinctKeys");
		@SuppressWarnings("unchecked")
		List<Translation> translations = (List<Translation>) query
				.getResultList();
		List<TranslationKey> keys = new ArrayList<TranslationKey>();
		for (Translation tr : translations) {
			if (!(keys.contains(tr.getTranslationKey()))) {
				keys.add(tr.getTranslationKey());
			}
		}
		return keys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Translation> getAllNonDeletedTranslations() {
		Query query = getEntityManager().createNamedQuery(
				"Translation.findAllNonDeleted");
		return (List<Translation>) query.getResultList();
	}

	@Override
	public void updateTranslation(Translation translation) {
		getEntityManager().merge(translation);
	}

	@Override
	public void createTranslation(Translation translation) {
		getEntityManager().persist(translation);
	}
}
