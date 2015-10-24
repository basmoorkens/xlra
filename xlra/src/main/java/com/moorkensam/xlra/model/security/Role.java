package com.moorkensam.xlra.model.security;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "role")
@NamedQueries(@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"))
public class Role extends BaseEntity {

	private static final long serialVersionUID = 3014589161716980040L;

	private String roleName;

	@ManyToMany
	@JoinTable(name = "role_permissions", joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id", referencedColumnName = "id") })
	private List<Permission> permissions;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
