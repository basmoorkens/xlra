package com.moorkensam.xlra.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.CustomerDAO;
import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;

public class CustomerDAOImpl extends BaseDAO implements CustomerDAO {

	public BaseCustomer createCustomer(BaseCustomer customer) {
		return getEntityManager().merge(customer);
	}

	public BaseCustomer updateCustomer(BaseCustomer customer) {
		return getEntityManager().merge(customer);
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
	public BaseCustomer getCustomerById(long id) {
		return getEntityManager().find(BaseCustomer.class, id);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BaseCustomer> getAllCustomers() {
		Query query = getEntityManager().createNamedQuery(
				"BaseCustomer.findAll");
		return (List<BaseCustomer>) query.getResultList();
	}

}
