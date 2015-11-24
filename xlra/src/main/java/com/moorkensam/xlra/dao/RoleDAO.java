package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.security.Role;

public interface RoleDAO {

	public void createRole(Role role);

	public Role updateRole(Role role);

	public List<Role> getAllRoles();

	public Role getRoleById(long id);

}
