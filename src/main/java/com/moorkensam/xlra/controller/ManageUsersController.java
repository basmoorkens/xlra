package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.error.UserException;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.RolePermissionService;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.UserStatusUtil;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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

  @Inject
  private UserSessionService userSessionService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private MessageUtil messageUtil;

  private List<User> users;

  private List<Role> allRoles;

  private User selectedUser;

  private DualListModel<Role> roles;

  private boolean editMode;

  private String crudPopupTitle;

  /**
   * Initializes the controller.
   */
  @PostConstruct
  public void initialize() {
    messageUtil = MessageUtil.getInstance(messageBundle);
    setCreateTitle();
    roles = new DualListModel<Role>();
    refreshUsers();
    fillInAvailableRoles();
  }

  private void setUpdateTitle() {
    crudPopupTitle =
        messageBundle.getString("usermanagement.popup.title.edit") + selectedUser.getUserName();
  }

  private void setCreateTitle() {
    crudPopupTitle = messageBundle.getString("usermanagement.popup.title.create");
  }

  private void fillInAvailableRoles() {
    List<Role> allRoles = permissionService.getAllRoles();
    if (!userSessionService.isLoggedInUserSystemAdmin()) {
      removeSysAdminRole(allRoles);
    }
    setAllRoles(allRoles);
  }

  private void removeSysAdminRole(List<Role> allRoles) {
    Iterator<Role> roleIterator = allRoles.iterator();
    while (roleIterator.hasNext()) {
      Role next = roleIterator.next();
      if (next.getName().equalsIgnoreCase("sysadmin")) {
        roleIterator.remove();
      }
    }
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
    messageUtil.addMessage("User deleted", "The user " + user.getName()
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
        messageUtil.addMessage("Password reset",
            "Password reset email was sent to " + selectedUser.getEmail());
        refreshUsers();
      } catch (MessagingException e) {
        messageUtil.addErrorMessage(
            "Failed to send email to user",
            "There was a problem sending out the password reset email to "
                + selectedUser.getUserName()
                + ". Please try again or of this errors persists contact the system "
                + "administrator.");
      } catch (XlraValidationException e2) {
        messageUtil.addErrorMessage("Error resetting user password", e2.getBusinessException());
      }
    }
  }

  /**
   * Enable the user.
   * 
   * @param user The user to enable.
   */
  public void enableUser(User user) {
    try {
      userService.enableUser(user);
      messageUtil.addMessage("User enabled", "Enabled user " + user.getUserName());
      refreshUsers();
    } catch (XlraValidationException e) {
      messageUtil.addErrorMessage("Can not enable user", e.getBusinessException());
    }
  }

  /**
   * Disable a user.
   * 
   * @param user The user to disable.
   */
  public void disableUser(User user) {
    try {
      userService.disableUser(user);
      messageUtil.addMessage("User disabled", "Disabled user " + user.getUserName());
      refreshUsers();
    } catch (XlraValidationException e) {
      messageUtil.addErrorMessage("Can not disable user", e.getBusinessException());
    }
  }

  /**
   * Sets up a new user.
   */
  public void setupNewUser() {
    selectedUser = new User();
    selectUserForEdit(selectedUser);
    editMode = false;
    showAddDialog();
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
    if (user.getId() > 0) {
      setUpdateTitle();
    } else {
      setCreateTitle();
    }
    editMode = true;
    showAddDialog();
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
   * Cancel the add of a new user.
   */
  public void cancelAddNewUser() {
    selectedUser = null;
    hideAddDialog();
    roles = new DualListModel<Role>();
  }

  private void hideAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addUserDialog').hide();");
  }

  private void showAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addUserDialog').show();");
  }

  /**
   * Creates or updates a user.
   */
  public void createOrUpdateUser() {
    selectedUser.setRoles(roles.getTarget());
    if (selectedUser.getId() == 0) {
      createUser();
    } else {
      updateUser();
    }
  }

  private void createUser() {
    try {
      userService.createUser(selectedUser);
      messageUtil.addMessage("User created", "The user " + selectedUser.getUserName()
          + " was successfully created.");
      refreshUsers();
      hideAddDialog();
    } catch (UserException e) {
      showAddDialog();
      messageUtil.addErrorMessage("Error creating user", e.getBusinessException());
    }
  }

  private void updateUser() {
    messageUtil.addMessage("User updated", "The user " + selectedUser.getUserName()
        + " was successfully updated.");
    userService.updateUser(selectedUser, false);
    refreshUsers();
    hideAddDialog();
  }

  public boolean canUserBeEnabled(User user) {
    return !UserStatusUtil.canEnableUser(user);
  }

  public boolean canUserResetPassword() {
    return !UserStatusUtil.canResetPassword(selectedUser);
  }

  public boolean canUserBeDisabled(User user) {
    return !UserStatusUtil.canDisableUser(user);
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

  /**
   * Check if the user canbe put in password reset state. only makes sense for a user that is
   * already persisted.
   * 
   * @return the result of the check
   */
  public boolean getCanResetPassword() {
    if (selectedUser.getId() > 0) {
      return UserStatusUtil.canResetPassword(selectedUser);
    }
    return false;
  }

  public boolean isEditMode() {
    return editMode;
  }

  public void setEditMode(boolean editMode) {
    this.editMode = editMode;
  }


  public String getCrudPopupTitle() {
    return crudPopupTitle;
  }

  public void setCrudPopupTitle(String crudPopupTitle) {
    this.crudPopupTitle = crudPopupTitle;
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }

  public MessageUtil getMessageUtil() {
    return messageUtil;
  }

  public void setMessageUtil(MessageUtil messageUtil) {
    this.messageUtil = messageUtil;
  }
}
