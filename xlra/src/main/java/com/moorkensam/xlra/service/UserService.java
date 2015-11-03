package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.security.User;

public interface UserService {

	public List<User> getAllUsers();

	public void createUser(User user);

	public User updateUser(User user, boolean updatePw);

	public void deleteUser(User user);

	public void resetUserPassword(User user);

	public User getUserById(long id);

	public User getUserByEmail(String email);

	public void resendAccountCreatedEmail(User user);

	public User isValidPasswordRequest(String email, String token);

	public void setPasswordAndActivateUser(User user, String password);

}
