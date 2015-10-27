package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.RoleDAO;
import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.model.security.Role;

public class RoleDAOImpl extends BaseDAO implements RoleDAO {

	@Override
	public void createRole(Role role) {
		getEntityManager().persist(role);
	}

	@Override
	public Role updateRole(Role role) {
		return getEntityManager().merge(role);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAllRoles() {
		Query query = getEntityManager().createNamedQuery("Role.findAll");
		List<Role> roles = (List<Role>) query.getResultList();
		for (Role role : roles) {
			lazyLoadRole(role);
			fillInPermissionsString(role);
		}
		return roles;
	}

	private void lazyLoadRole(Role role) {
		role.getPermissions().size();
	}

	private void fillInPermissionsString(Role role) {
		int counter = 0;
		String permString = "";
		for (Permission p : role.getPermissions()) {
			permString += p.getKey() + ",";
			counter++;
			if (counter % 10 == 0) {
				permString += "\n";
			}
		}
		role.setPermissionsString(permString);
	}

	@Override
	public Role getRoleById(long id) {
		Query query = getEntityManager().createNamedQuery("Role.findById");
		query.setParameter("id", id);
		return (Role) query.getSingleResult();
	}
}
