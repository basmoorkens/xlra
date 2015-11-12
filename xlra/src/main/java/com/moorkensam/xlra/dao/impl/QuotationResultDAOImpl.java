package com.moorkensam.xlra.dao.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.QuotationResultDAO;
import com.moorkensam.xlra.model.QuotationQuery;
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

	@Override
	public List<QuotationResult> getQuotationResults(int first, int pageSize,
			String sortField, SortOrder sortOrder, Map<String, String> filters) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT q FROM QuotationResult q WHERE 1 = 1 ");
		if (sortField != null && !sortField.isEmpty()) {
			queryBuilder.append("ORDER BY q." + sortField + " ");
			if (sortOrder != null) {
				if (SortOrder.ASCENDING.equals(sortOrder)) {
					queryBuilder.append("ASC");
				} else if (SortOrder.DESCENDING.equals(sortOrder)) {
					queryBuilder.append("DESC");
				}
			}
		}
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		if (first > 0) {
			query.setFirstResult(first);
		}
		if (pageSize > 0) {
			query.setMaxResults(pageSize);
		}

		@SuppressWarnings("unchecked")
		List<QuotationResult> resultList = (List<QuotationResult>) query
				.getResultList();
		for (QuotationResult q : resultList) {
			fullLoad(q);
		}
		return resultList;
	}
}
