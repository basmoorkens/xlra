package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.QuotationResultDAO;
import com.moorkensam.xlra.model.rate.QuotationResult;

public class QuotationResultDAOImpl extends BaseDAO implements
		QuotationResultDAO {

	@Override
	public void createQuotationResult(QuotationResult result) {
		getEntityManager().persist(result);
	}

	@Override
	public QuotationResult updateQuotationResult(QuotationResult result) {
		QuotationResult resultMerged = getEntityManager().merge(result);
		fullLoad(resultMerged);
		return resultMerged;
	}

	private void fullLoad(QuotationResult result) {
		result.getQuery();
		result.getEmailResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QuotationResult> getAllQuotationResults() {
		Query q = getEntityManager()
				.createNamedQuery("QuotationResult.findAll");
		return (List<QuotationResult>) q.getResultList();
	}

}
