package com.moorkensam.xlra.model.rate;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Cacheable
@Table(name = "country")
@NamedQueries(@NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c WHERE c.deleted = false"))
public class Country extends BaseEntity {

	private static final long serialVersionUID = -5766329224119072846L;

	private String name;

	private String shortName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Country)) {
			return false;
		}
		Country c = (Country) obj;
		if (c.getId() == id && c.getName().equals(name)
				&& c.getShortName().equals(shortName)) {
			return true;
		}
		return false;
	}

}
