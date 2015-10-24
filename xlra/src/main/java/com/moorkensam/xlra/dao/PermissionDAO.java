package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.security.Permission;

public interface PermissionDAO {

	public List<Permission> getAllPermissions();

	public void createPermission(Permission p);

	public Permission updatePermission(Permission p);

}
