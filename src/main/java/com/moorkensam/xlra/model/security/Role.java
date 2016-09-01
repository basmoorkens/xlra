package com.moorkensam.xlra.model.security;

import com.moorkensam.xlra.model.BaseEntity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "role")
@NamedQueries({@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
    @NamedQuery(name = "Role.findById", query = "SELECT r FROM Role r where r.id = :id")})
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
  @JoinTable(name = "role_permissions", joinColumns = {@JoinColumn(name = "role_id",
      referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id",
      referencedColumnName = "id")})
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
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Role)) {
      return false;
    }
    Role other = (Role) obj;
    return new EqualsBuilder().append(id, other.getId()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).append(getId()).toHashCode();
  }

  /**
   * Fill in the permissions from the list into a string representation.
   */
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
