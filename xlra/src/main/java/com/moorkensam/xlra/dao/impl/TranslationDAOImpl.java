package com.moorkensam.xlra.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.TranslationDAO;
import com.moorkensam.xlra.model.translation.Translation;
import com.moorkensam.xlra.model.translation.TranslationKey;

public class TranslationDAOImpl extends BaseDAO implements TranslationDAO {

	@Override
	@SuppressWarnings("unchecked")
	public List<TranslationKey> getDistinctTranslationKeys() {
		Query query = getEntityManager().createNamedQuery(
				"Translation.findDistinctKeys");
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
		List<Translation> resultList = (List<Translation>) query
				.getResultList();
		lazyLoadTranslations(resultList);
		return resultList;
	}

	private void lazyLoadTranslations(List<Translation> resultList) {
		for (Translation t : resultList) {
			t.getTranslationKeysTranslations().size();
			t.getTranslations().size();
		}

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
