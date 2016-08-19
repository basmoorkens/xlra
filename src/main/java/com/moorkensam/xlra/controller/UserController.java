package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class UserController {

  @Inject
  private UserService userService;

  private User user;

  private String newPassword;

  private String retypedPassword;

  @Inject
  private UserSessionService userSessionService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private MessageUtil messageUtil;

  @PostConstruct
  public void initialize() {
    messageUtil = MessageUtil.getInstance(messageBundle);
    user = userSessionService.getLoggedInUser();
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getRetypedPassword() {
    return retypedPassword;
  }

  public void setRetypedPassword(String retypedPassword) {
    this.retypedPassword = retypedPassword;
  }

  /**
   * Save the user.
   */
  public void saveUser() {
    boolean skippws = false;
    if (newPassword == null || newPassword.isEmpty() || retypedPassword == null
        || retypedPassword.isEmpty()) {
      skippws = true;
    } else {
      if (newPassword.equals(retypedPassword) && !skippws) {
        user.setPassword(newPassword);
      } else {
        if (!skippws) {
          messageUtil.addErrorMessage("Passwords do not match",
              "The new password and the retyped new password should be the same!");
          return;
        }
      }
    }
    user = userService.updateUser(user, !skippws);
    messageUtil.addMessage("Profile updated", "Your profile was successfully updated.");
  }

  public MessageUtil getMessageUtil() {
    return messageUtil;
  }

  public void setMessageUtil(MessageUtil messageUtil) {
    this.messageUtil = messageUtil;
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }

}
