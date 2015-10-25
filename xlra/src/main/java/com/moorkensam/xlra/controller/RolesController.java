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
import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.service.RolePermissionService;

@ManagedBean
@ViewScoped
public class RolesController {

	@Inject
	private RolePermissionService roleService;

	private Role selectedRole;

	private List<Role> allRoles;

	private List<Permission> allPermissions;

	private DualListModel<Permission> permissions;

	@PostConstruct
	public void initialize() {
		permissions = new DualListModel<Permission>();
		refreshRoles();
		allPermissions = roleService.getAllPermissions();
		setupNewRole();
	}

	private void refreshRoles() {
		setAllRoles(roleService.getAllRoles());
	}

	public void createOrUpdateRole() {
		selectedRole.setPermissions(permissions.getTarget());
		if (selectedRole.getId() == 0) {
			roleService.createRole(selectedRole);
			MessageUtil.addMessage("Role created",
					"The role " + selectedRole.getName()
							+ " was successfully created.");
			setupNewRole();
		} else {
			MessageUtil.addMessage("Role updated",
					"The role " + selectedRole.getName()
							+ " was successfully updated.");
			roleService.updateRole(selectedRole);
		}
		refreshRoles();
	}

	public void selectRoleForEdit(Role role) {
		selectedRole = role;
		getPermissions().setTarget(role.getPermissions());
		getPermissions().setSource(getSourcePermissions(selectedRole));
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('addRoleDialog').show();");
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
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('addRoleDialog').show();");
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

}
