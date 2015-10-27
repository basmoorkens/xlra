package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.security.User;

public interface UserService {

	public List<User> getAllUsers();

	public void createUser(User user);

	public User updateUser(User user);

	public void deleteUser(User user);

	public void resetUserPassword(User user);

	public User getUserById(long id);

	public User getUserByEmail(String email);

}
