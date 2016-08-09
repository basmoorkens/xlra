package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.security.User;

public interface UserSessionService {

  User getLoggedInUser();

  boolean isLoggedInUserAdmin();

  boolean isLoggedInUserSystemAdmin();

  boolean doesLoggedInUserHaveAdminRights();
}
