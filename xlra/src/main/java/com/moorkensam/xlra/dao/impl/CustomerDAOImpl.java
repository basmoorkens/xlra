package com.moorkensam.xlra.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.CustomerDAO;
import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;

public class CustomerDAOImpl extends BaseDAO implements CustomerDAO {

	public void createCustomer(FullCustomer customer) {
		getEntityManager().persist(customer);
	}

	public void updateCustomer(FullCustomer customer) {
		getEntityManager().merge(customer);
	}

	@SuppressWarnings("unchecked")
	public List<FullCustomer> getAllFullCustomers() {
		Query query = getEntityManager().createNamedQuery(
				"FullCustomer.findAll");
		return (List<FullCustomer>) query.getResultList();
	}

	@Override
	public void deleteCustomer(BaseCustomer customer) {
		customer.setDeleted(true);
		customer.setDeletedDateTime(new Date());
		getEntityManager().merge(customer);
	}

	@Override
	public FullCustomer getCustomerById(long id) {
		return (FullCustomer) getEntityManager().find(FullCustomer.class, id);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BaseCustomer> getAllCustomers() {
		Query query = getEntityManager().createNamedQuery(
				"BaseCustomer.findAll");
		return (List<BaseCustomer>) query.getResultList();
	}

}
