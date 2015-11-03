package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.RolePermissionService;
import com.moorkensam.xlra.service.UserService;

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

	@PostConstruct
	public void initialize() {
		roles = new DualListModel<Role>();
		refreshUsers();
		setAllRoles(permissionService.getAllRoles());
	}

	private void refreshUsers() {
		setUsers(userService.getAllUsers());
	}

	public void setupNewUsers() {
		selectedUser = new User();
		selectUserForEdit(selectedUser);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('addUserDialog').show();");
	}

	public void deleteUser(User user) {
		userService.deleteUser(user);
		MessageUtil.addMessage("User deleted", "The user " + user.getName()
				+ " was successfully deleted.");
		refreshUsers();

	}

	public void resetUserPassword() {
		if (getCanResetPassword()) {
			userService.resetUserPassword(selectedUser);
		}
	}

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

	public void createOrUpdateUser() {
		selectedUser.setRoles(roles.getTarget());
		if (selectedUser.getId() == 0) {
			userService.createUser(selectedUser);
			MessageUtil.addMessage("User created",
					"The user " + selectedUser.getName()
							+ " was successfully created.");
			setupNewUsers();
		} else {
			MessageUtil.addMessage("User updated",
					"The user " + selectedUser.getName()
							+ " was successfully updated.");
			userService.updateUser(selectedUser);
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
