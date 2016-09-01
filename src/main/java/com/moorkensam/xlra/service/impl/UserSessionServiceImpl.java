package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.inject.Inject;

@Stateful
public class UserSessionServiceImpl implements UserSessionService {

  private User loggedInUser;

  @Resource
  private SessionContext sessionContext;

  @Inject
  private UserService userService;

  @PostConstruct
  public void initialize() {
    refreshUser();
  }

  /**
   * Refresh the user. This can be called if the entity in memory is suspected to be dirty.
   */
  public void refreshUser() {
    Principal callerPrincipal = sessionContext.getCallerPrincipal();
    loggedInUser = userService.getUserByUserName(callerPrincipal.getName());
  }

  @Override
  public User getLoggedInUser() {
    return loggedInUser;
  }

  public void setLoggedInUser(User loggedInUser) {
    this.loggedInUser = loggedInUser;
  }

  public SessionContext getSessionContext() {
    return sessionContext;
  }

  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean isLoggedInUserAdmin() {
    return isLoggedInUserA("Admin");
  }

  @Override
  public boolean isLoggedInUserSystemAdmin() {
    return isLoggedInUserA("SysAdmin");
  }

  /**
   * Check if a user has a role.
   * 
   * @param roleString The role to lookup
   * @return True if it contains the role
   */
  private boolean isLoggedInUserA(String roleString) {
    if (loggedInUser.getRolesAsString().contains(roleString)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean doesLoggedInUserHaveAdminRights() {
    return isLoggedInUserAdmin() || isLoggedInUserSystemAdmin();
  }

}
