package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.PermissionDAO;
import com.moorkensam.xlra.dao.RoleDAO;
import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.service.RolePermissionService;

@Stateless
public class RolePermissionServiceImpl implements RolePermissionService {

	@Inject
	private RoleDAO roleDAO;

	@Inject
	private PermissionDAO permissionDAO;

	@Override
	public void createRole(Role role) {
		roleDAO.createRole(role);
	}

	@Override
	public Role updateRole(Role role) {
		return roleDAO.updateRole(role);
	}

	@Override
	public List<Role> getAllRoles() {
		return roleDAO.getAllRoles();
	}

	@Override
	public List<Permission> getAllPermissions() {
		return permissionDAO.getAllPermissions();
	}

	@Override
	public void createPermission(Permission p) {
		permissionDAO.createPermission(p);
	}

	@Override
	public Permission updatePermission(Permission p) {
		return permissionDAO.updatePermission(p);
	}

}
