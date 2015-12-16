package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.RolePermissionService;
import com.moorkensam.xlra.service.UserService;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;

@ManagedBean
@ViewScoped
public class ManageUsersController {

  @Inject
  private UserService userService;

  @Inject
  private RolePermissionService permissionService;

  private List<User> users;

  private List<Role> allRoles;

  private User selectedUser;

  private DualListModel<Role> roles;

  /**
   * Initializes the controller.
   */
  @PostConstruct
  public void initialize() {
    roles = new DualListModel<Role>();
    refreshUsers();
    setAllRoles(permissionService.getAllRoles());
  }

  private void refreshUsers() {
    setUsers(userService.getAllUsers());
  }

  /**
   * Delete a user.
   * 
   * @param user The user to delete.
   */
  public void deleteUser(User user) {
    userService.deleteUser(user);
    MessageUtil.addMessage("User deleted", "The user " + user.getName()
        + " was successfully deleted.");
    refreshUsers();

  }

  /**
   * Resets the users password.
   */
  public void resetUserPassword() {
    if (getCanResetPassword()) {
      try {
        userService.resetUserPassword(selectedUser);
        MessageUtil.addMessage("Password reset",
            "Password reset email was sent to " + selectedUser.getEmail());
      } catch (MessagingException e) {
        MessageUtil.addErrorMessage(
            "Failed to send email to user",
            "There was a problem sending out the password reset email to "
                + selectedUser.getUserName()
                + ". Please try again or of this errors persists contact the system "
                + "administrator.");
      }
    }
  }

  /**
   * Enable the user.
   * 
   * @param user The user to enable.
   */
  public void enableUser(User user) {
    userService.enableUser(user);
    MessageUtil.addMessage("User enabled", "Enabled user " + user.getUserName());
    refreshUsers();
  }

  /**
   * Disable a user.
   * 
   * @param user The user to disable.
   */
  public void disableUser(User user) {
    userService.disableUser(user);
    MessageUtil.addMessage("User disabled", "Disabled user " + user.getUserName());
    refreshUsers();
  }

  /**
   * Sets up a new user.
   */
  public void setupNewUser() {
    selectedUser = new User();
    selectUserForEdit(selectedUser);
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addUserDialog').show();");
  }

  /**
   * Setup the page for editing a user.
   * 
   * @param user The user to edit.
   */
  public void selectUserForEdit(User user) {
    if (user.getId() > 0) {
      user = userService.getUserById(user.getId());
    }
    selectedUser = user;
    roles.setTarget(user.getRoles());
    roles.setSource(getSourceRoles(user));
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addUserDialog').show();");
  }

  private List<Role> getSourceRoles(User user) {
    List<Role> sourceRole = new ArrayList<Role>();
    for (Role r : allRoles) {
      if (!user.getRoles().contains(r)) {
        sourceRole.add(r);
      }
    }

    return sourceRole;
  }

  /**
   * Creates or updates a user.
   */
  public void createOrUpdateUser() {
    selectedUser.setRoles(roles.getTarget());
    if (selectedUser.getId() == 0) {
      userService.createUser(selectedUser);
      MessageUtil.addMessage("User created", "The user " + selectedUser.getUserName()
          + " was successfully created.");
    } else {
      MessageUtil.addMessage("User updated", "The user " + selectedUser.getUserName()
          + " was successfully updated.");
      userService.updateUser(selectedUser, false);
    }
    refreshUsers();
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public User getSelectedUser() {
    return selectedUser;
  }

  public void setSelectedUser(User selectedUser) {
    this.selectedUser = selectedUser;
  }

  public DualListModel<Role> getRoles() {
    return roles;
  }

  public void setRoles(DualListModel<Role> roles) {
    this.roles = roles;
  }

  public RolePermissionService getPermissionService() {
    return permissionService;
  }

  public void setPermissionService(RolePermissionService permissionService) {
    this.permissionService = permissionService;
  }

  public List<Role> getAllRoles() {
    return allRoles;
  }

  public void setAllRoles(List<Role> allRoles) {
    this.allRoles = allRoles;
  }

  public boolean getCanResetPassword() {
    return selectedUser != null && selectedUser.getId() > 0;
  }

}
