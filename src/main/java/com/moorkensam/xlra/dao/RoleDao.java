package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.security.Role;

import java.util.List;

public interface RoleDao {

  public void createRole(Role role);

  public Role updateRole(Role role);

  public List<Role> getAllRoles();

  public Role getRoleById(long id);

}
