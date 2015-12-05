package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.security.User;

public interface UserDAO {

	public List<User> getAllUsers();

	public void createUser(User user);

	public User updateUser(User user);

	public User resetPassword(User user, String generatedPassword);

	public User getUserbyId(long id);

	public User getUserByEmail(String email);

	public User getUserByUserName(String userName);

	public void deleteUser(User user);

	public User isValidPasswordRequest(String email, String token);

	public User isValidPasswordResetRequest(String email, String token);
}
