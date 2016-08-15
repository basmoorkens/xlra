package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.service.UserSessionService;

import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class TemplateController {

  @Inject
  private UserSessionService userSessionService;

  /**
   * Check if the permission is owned by the user.
   * 
   * @param permission The permission to check.
   * @return True when the user has the permission.
   */
  public boolean hasPermission(String permission) {
    for (Permission p : userSessionService.getLoggedInUser().getAllPermissions()) {
      if (p.getKey().equalsIgnoreCase(permission)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if the user has none of the permissions.
   * 
   * @param permissions The permissions to check.
   * @return Returns true when none of the permissions are owned by the user.
   */
  public boolean hasNonePermissions(List<String> permissions) {
    for (String p : permissions) {
      for (Permission perm : userSessionService.getLoggedInUser().getAllPermissions()) {
        if (perm.getKey().equalsIgnoreCase(p)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks if the user is an admin.
   * 
   * @return True if the user is an admin, false otherwise.
   */
  public boolean isAdmin() {
    return userSessionService.isLoggedInUserAdmin();
  }

  public boolean isSysAdmin() {
    return userSessionService.isLoggedInUserSystemAdmin();
  }


  public UserSessionService getUserSessionService() {
    return userSessionService;
  }

  public void setUserSessionService(UserSessionService userSessionService) {
    this.userSessionService = userSessionService;
  }

  public String getLoggedInUserName() {
    return userSessionService.getLoggedInUser().getUserName();
  }

}
