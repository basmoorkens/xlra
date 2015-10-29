package com.moorkensam.xlra.model.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "role")
@NamedQueries({
		@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
		@NamedQuery(name = "Role.findById", query = "SELECT r FROM Role r where r.id = :id") })
public class Role extends BaseEntity {

	private static final long serialVersionUID = 3014589161716980040L;

	public Role() {
		permissions = new ArrayList<Permission>();
	}

	@Column(name = "role_name")
	private String name;

	@Column(name = "role_description")
	private String description;

	@ManyToMany
	@JoinTable(name = "role_permissions", joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id", referencedColumnName = "id") })
	private List<Permission> permissions;

	@Transient
	private String permissionsString;

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermissionsString() {
		return permissionsString;
	}

	public void setPermissionsString(String permissionsString) {
		this.permissionsString = permissionsString;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Role))
			return false;
		Role other = (Role) obj;
		return other.getId() == id;
	}

	public void fillInPermissionsString() {
		int counter = 0;
		String permString = "";
		for (Permission p : getPermissions()) {
			permString += p.getKey() + ",";
			counter++;
			if (counter % 5 == 0) {
				permString += "\n";
			}
		}
		setPermissionsString(permString);
	}

}
