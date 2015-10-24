package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.RoleDAO;
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
		return (List<Role>) query.getResultList();
	}

}
