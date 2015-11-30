package com.moorkensam.xlra.dao.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.QuotationQueryDAO;
import com.moorkensam.xlra.model.offerte.QuotationQuery;

public class QuotationQueryDAOImpl extends BaseDAO implements QuotationQueryDAO {

	@Override
	public QuotationQuery createQuotationQuery(QuotationQuery quotation) {
		return getEntityManager().merge(quotation);
	}

	@Override
	public QuotationQuery updateQuotationQuery(QuotationQuery quotation) {
		return getEntityManager().merge(quotation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QuotationQuery> getAllQuotationQueries() {
		Query q = getEntityManager().createNamedQuery("QuotationQuery.findAll");
		return (List<QuotationQuery>) q.getResultList();
	}

}
