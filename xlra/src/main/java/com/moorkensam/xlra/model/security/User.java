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
@Table(name = "user")
@NamedQueries({
		@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
		@NamedQuery(name = "User.findAllNonDeleted", query = "SELECT u FROM User u where u.enabled = true") })
public class User extends BaseEntity {

	private static final long serialVersionUID = -8058602514887367935L;

	private String email;

	private String password;

	private boolean enabled;

	@ManyToMany
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	private List<Role> roles;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
