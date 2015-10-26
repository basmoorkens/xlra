package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.PermissionDAO;
import com.moorkensam.xlra.dao.RoleDAO;
import com.moorkensam.xlra.dao.UserDAO;
import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.UserService;

@Stateless
public class UserServiceImpl implements UserService {

	@Inject
	private UserDAO userDAO;

	@Override
	public List<User> getAllUsers() {
		return userDAO.getAllUsers();
	}

	@Override
	public void createUser(User user) {
		userDAO.createUser(user);
	}

	@Override
	public User updateUser(User user) {
		return userDAO.updateUser(user);
	}

	@Override
	public User resetPassword(User user, String generatedPassword) {
		return userDAO.resetPassword(user, generatedPassword);
	}

	@Override
	public User getUserById(long id) {
		return userDAO.getUserbyId(id);
	}

}
