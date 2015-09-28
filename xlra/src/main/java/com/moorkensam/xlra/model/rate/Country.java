package com.moorkensam.xlra.model.rate;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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

	@Enumerated(EnumType.STRING)
	private ZoneType zoneType;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "country", cascade = CascadeType.ALL)
	private List<Zone> zones;

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

	public List<Zone> getZones() {
		return zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

	public ZoneType getZoneType() {
		return this.zoneType;
	}

	public void setZoneType(ZoneType zoneType) {
		this.zoneType = zoneType;
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

	@Override
	public String toString() {
		return name + " (" + shortName + ")";
	}

}
