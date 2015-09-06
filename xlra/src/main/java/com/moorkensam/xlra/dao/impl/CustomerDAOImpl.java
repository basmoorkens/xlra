package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.CustomerDAO;
import com.moorkensam.xlra.model.FullCustomer;

@Stateless
@NamedQueries({
	@NamedQuery(name="Customer.findAll", query="SELECT c FROM Customer c")
})
public class CustomerDAOImpl extends BaseDAO implements CustomerDAO {

	public void createCustomer(FullCustomer customer) {
		getEntityManager().persist(customer);
	}
	
	public void updateCustomer(FullCustomer customer) {
		getEntityManager().merge(customer);
	}
	
	public List<FullCustomer> getAllFullCustomers() {
		Query query = getEntityManager().createNamedQuery("FullCustomer.findAll");
		return (List<FullCustomer>) query.getResultList();
	}
	
	
}
