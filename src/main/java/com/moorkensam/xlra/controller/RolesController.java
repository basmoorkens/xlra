package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.service.RolePermissionService;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class RolesController {

  @Inject
  private RolePermissionService roleService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private MessageUtil messageUtil;

  private Role selectedRole;

  private List<Role> allRoles;

  private List<Permission> allPermissions;

  private DualListModel<Permission> permissions;

  /**
   * Initialize the controller.
   */
  @PostConstruct
  public void initialize() {
    messageUtil = MessageUtil.getInstance(messageBundle);
    permissions = new DualListModel<Permission>();
    refreshRoles();
    allPermissions = roleService.getAllPermissions();
    setupInternalNewRole();
  }

  private void setupInternalNewRole() {
    internalSetRole(new Role());
  }

  private void refreshRoles() {
    setAllRoles(roleService.getAllRoles());
  }

  /**
   * Creates or updates the selected role.
   */
  public void createOrUpdateRole() {
    selectedRole.setPermissions(permissions.getTarget());
    if (selectedRole.getId() == 0) {
      roleService.createRole(selectedRole);
      messageUtil.addMessage("message.role.created.title", "message.role.created.detail",
          selectedRole.getName());
      setupNewRole();
    } else {
      messageUtil.addMessage("message.role.updated.title", "message.role.updated.detail",
          selectedRole.getName());
      roleService.updateRole(selectedRole);
    }
    refreshRoles();
  }

  /**
   * Select a role for editing.
   * 
   * @param role The role to select.
   */
  public void selectRoleForEdit(Role role) {
    internalSetRole(role);
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addRoleDialog').show();");
  }

  private void internalSetRole(Role role) {
    selectedRole = role;
    getPermissions().setTarget(role.getPermissions());
    getPermissions().setSource(getSourcePermissions(selectedRole));
  }

  public void cancelAddRole() {
    selectedRole = null;
    hideAddDialog();
  }

  private void hideAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addRoleDialog').hide();");
  }

  private List<Permission> getSourcePermissions(Role role) {
    List<Permission> sourcePerm = new ArrayList<Permission>();
    for (Permission p : allPermissions) {
      if (!role.getPermissions().contains(p)) {
        sourcePerm.add(p);
      }
    }

    return sourcePerm;
  }

  public void setupNewRole() {
    selectedRole = new Role();
    selectRoleForEdit(selectedRole);
  }

  public List<Role> getAllRoles() {
    return allRoles;
  }

  public void setAllRoles(List<Role> allRoles) {
    this.allRoles = allRoles;
  }

  public Role getSelectedRole() {
    return selectedRole;
  }

  public void setSelectedRole(Role selectedRole) {
    this.selectedRole = selectedRole;
  }

  public List<Permission> getAllPermissions() {
    return allPermissions;
  }

  public void setAllPermissions(List<Permission> allPermissions) {
    this.allPermissions = allPermissions;
  }

  public DualListModel<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(DualListModel<Permission> permissions) {
    this.permissions = permissions;
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
