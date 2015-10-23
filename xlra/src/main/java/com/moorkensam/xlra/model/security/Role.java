package com.moorkensam.xlra.model.security;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "role")
public class Role extends BaseEntity {

	private static final long serialVersionUID = 3014589161716980040L;

	private String roleName;

	// @ManyToMany
	@Transient
	private List<Permission> permissions;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
