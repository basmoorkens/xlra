package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserStatus;

/**
 * Util class for manipulating user objects.
 * 
 * @author bas
 *
 */
public class UserUtil {


  /**
   * Check if the user can be disabled
   * 
   * @param user The user to check
   * @return The result of the check.
   */
  public static boolean canDisableUser(User user) {
    if (user != null && user.getUserStatus() != null) {
      return user.getUserStatus().getAllowedStatuses().contains(UserStatus.DISABLED);
    }
    return false;
  }

  /**
   * Check if the user can be enabled
   * 
   * @param user The user to check
   * @return The result of the check.
   */
  public static boolean canEnableUser(User user) {
    if (user != null && user.getUserStatus() != null) {
      return user.getUserStatus().getAllowedStatuses().contains(UserStatus.IN_OPERATION);
    }
    return false;
  }

  /**
   * Check if a password reset state can be set for the user.
   * 
   * @param user The user to check
   * @return The result of the check.
   */
  public static boolean canResetPassword(User user) {
    if (user != null && user.getUserStatus() != null) {
      return user.getUserStatus().getAllowedStatuses().contains(UserStatus.PASSWORD_RESET);
    }
    return false;
  }

}
