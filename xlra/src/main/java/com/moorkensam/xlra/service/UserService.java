package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.model.security.User;

public interface UserService {

	public List<User> getAllUsers();

	public void createUser(User user);

	public User updateUser(User user);

	public User resetPassword(User user, String generatedPassword);
}
