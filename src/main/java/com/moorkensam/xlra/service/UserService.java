package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.error.UserException;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.model.security.User;

import java.util.List;

import javax.mail.MessagingException;

public interface UserService {

  public List<User> getAllUsers();

  public void createUser(User user) throws UserException;

  public User updateUser(User user, boolean updatePw);

  public void deleteUser(User user);

  public void resetUserPassword(User user) throws MessagingException, XlraValidationException;

  public User getUserById(long id);

  public User getUserByEmail(String email);

  public User getUserByUserName(String username);

  public void resendAccountCreatedEmail(User user);

  public User isValidPasswordRequest(String email, String token);

  public User isValidPasswordResetRequest(String email, String token);

  public void setPasswordAndActivateUser(User user, String password);

  public void disableUser(User user) throws XlraValidationException;

  public void enableUser(User user) throws XlraValidationException;

}
