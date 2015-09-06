package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.CustomerDAO;
import com.moorkensam.xlra.model.Customer;

@Stateless
@NamedQueries({
	@NamedQuery(name="Customer.findAll", query="SELECT c FROM Customer c")
})
public class CustomerDAOImpl extends BaseDAO implements CustomerDAO {

	public void createCustomer(Customer customer) {
		getEntityManager().persist(customer);
	}
	
	public void updateCustomer(Customer customer) {
		getEntityManager().merge(customer);
	}
	
	public List<Customer> getAllCustomers() {
		Query query = getEntityManager().createNamedQuery("Customer.findAll");
		return (List<Customer>) query.getResultList();
	}
	
	
}
