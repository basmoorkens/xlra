package com.moorkensam.xlra.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.moorkensam.xlra.model.Customer;

@Stateless
public class TestEJB {

	@PersistenceContext(unitName="xlraPU")
	private EntityManager em;
	
	public void insertCustomer(Customer c) {
		em.persist(c);
	}
	
	public List<Customer> getCustomers() {
		String hql = "SELECT c FROM Customer c"; 
		Query q = em.createQuery(hql);
		return q.getResultList();
	}
	
}
