package com.moorkensam.xlra.dao.impl;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.IncoTermTranslationDAO;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.IncoTermTranslation;
import com.moorkensam.xlra.model.rate.IncoTermType;

public class IncoTermTranslationDAOImpl extends BaseDAO implements
		IncoTermTranslationDAO {

	@Override
	public IncoTermTranslation findTranslationByLanguageAndIncoTermType(
			Language language, IncoTermType incoTermType) {
		Query query = getEntityManager().createNamedQuery("IncoTermTranslation.findByLanguageAndIncoTermType");
		query.setParameter("incoTermType", incoTermType);
		query.setParameter("language", language);
		
		return (IncoTermTranslation) query.getSingleResult();
	}

	@Override
	public void updateIncoTermTranslation(
			IncoTermTranslation incoTermTranslation) {
		getEntityManager().merge(incoTermTranslation);
	}

}
