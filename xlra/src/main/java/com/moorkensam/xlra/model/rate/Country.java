package com.moorkensam.xlra.model.rate;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "country")
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

}
