package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.security.User;

public interface UserDAO {

	public List<User> getAllUsers();

	public void createUser(User user);

	public User updateUser(User user);

	public User resetPassword(User user, String generatedPassword);

	public User getUserbyId(long id);
}
