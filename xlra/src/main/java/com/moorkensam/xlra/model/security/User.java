package com.moorkensam.xlra.model.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "user")
@NamedQueries({
		@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
		@NamedQuery(name = "User.findById", query = "SELECT u FROM User u where u.id = :id"),
		@NamedQuery(name = "User.findByEmailAndToken", query = "SELECT u FROM User u WHERE u.email = :email and u.tokenInfo.verificationToken = :token"
				+ " and u.userStatus = :userStatus"),
		@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u LEFT JOIN FETCH u.roles where u.email = :email"),
		@NamedQuery(name = "User.findAllNonDeleted", query = "SELECT u FROM User u where u.enabled = true") })
public class User extends BaseEntity {

	private static final long serialVersionUID = -8058602514887367935L;

	private String email;

	private String password;

	private String name;

	private String firstName;

	private boolean enabled;

	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

	@Embedded
	private TokenInfo tokenInfo;

	@Transient
	public String getFullName() {
		return firstName + " " + name;
	}

	public User() {
		tokenInfo = new TokenInfo();
		enabled = true;
		roles = new ArrayList<Role>();
	}

	@ManyToMany
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	@BatchSize(size = 2)
	private List<Role> roles;

	public String getRolesAsString() {
		String rolesString = "";
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {
				rolesString += role.getName() + ", ";
			}
		}
		return rolesString;
	}

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Permission> getAllPermissions() {
		List<Permission> permissions = new ArrayList<Permission>();
		for (Role r : roles) {
			permissions.addAll(r.getPermissions());
		}
		return permissions;
	}

	public TokenInfo getTokenInfo() {
		return tokenInfo;
	}

	public void setTokenInfo(TokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}
}
