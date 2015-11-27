package com.moorkensam.xlra.model.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "permissions")
@NamedQueries({
		@NamedQuery(name = "Permission.findAll", query = "SELECT p FROM Permission p"),
		@NamedQuery(name = "Permission.findById", query = "SELECT p FROM Permission p WHERE p.id = :id") })
public class Permission extends BaseEntity {

	private static final long serialVersionUID = 5223990771708418257L;

	@Column(name = "permission_key")
	private String key;

	@Column(name = "permission_description")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Permission))
			return false;
		Permission other = (Permission) obj;
		return other.getId() == id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
