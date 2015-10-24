package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.UserDAO;
import com.moorkensam.xlra.model.security.User;

public class UserDAOImpl extends BaseDAO implements UserDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() {
		Query query = getEntityManager().createNamedQuery("User.findAll");
		return (List<User>) query.getResultList();
	}

	@Override
	public void createUser(User user) {
		getEntityManager().persist(user);
	}

	@Override
	public User updateUser(User user) {
		return getEntityManager().merge(user);
	}

	@Override
	public User resetPassword(User user, String generatedPassword) {
		user.setPassword(generatedPassword);
		return getEntityManager().merge(user);
	}

}
