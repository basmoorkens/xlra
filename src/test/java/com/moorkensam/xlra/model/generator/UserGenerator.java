package com.moorkensam.xlra.model.generator;

import com.moorkensam.xlra.model.security.User;

public class UserGenerator {

  public static final String FIRST_NAME = "bas";

  public static final String STANDARD_LAST_NAME = "moorkens";

  public static final String STANDARD_USERNAME = "bmoork";


  /**
   * Creates a standard user.
   * 
   * @return The created user object.
   */
  public static User getStandardUser() {
    User user = new User();
    user.setUserName(STANDARD_USERNAME);
    user.setFirstName(FIRST_NAME);
    user.setName(STANDARD_LAST_NAME);
    return user;
  }

}
