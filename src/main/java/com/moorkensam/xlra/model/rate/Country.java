package com.moorkensam.xlra.model.rate;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Language;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "country")
@NamedQueries(@NamedQuery(name = "Country.findAll",
    query = "SELECT c FROM Country c  WHERE c.deleted = false"))
public class Country extends BaseEntity {

  private static final long serialVersionUID = -5766329224119072846L;

  @ElementCollection(fetch = FetchType.EAGER)
  @MapKeyColumn(name = "language")
  @MapKeyEnumerated(EnumType.STRING)
  @Column(name = "name")
  @CollectionTable(name = "countrynames", joinColumns = @JoinColumn(name = "country_id"))
  private Map<Language, String> names;

  private String shortName;

  @Enumerated(EnumType.STRING)
  private ZoneType zoneType;

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public ZoneType getZoneType() {
    return this.zoneType;
  }

  public void setZoneType(ZoneType zoneType) {
    this.zoneType = zoneType;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Country)) {
      return false;
    }
    Country country = (Country) obj;
    return new EqualsBuilder().append(id, country.getId())
        .append(shortName, country.getShortName()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).append(id).append(shortName).toHashCode();
  }

  @Override
  public String toString() {
    return " (" + shortName + ")";
  }

  public Map<Language, String> getNames() {
    return names;
  }

  public void setNames(Map<Language, String> names) {
    this.names = names;
  }

  /**
   * builds an empty language map.
   */
  public void buildEmptyLanguageMap() {
    if (names == null) {
      names = new HashMap<Language, String>();
    }
    for (Language l : Language.values()) {
      names.put(l, "");
    }
  }

  public String getEnglishName() {
    return names.get(Language.EN);
  }

  public String getDutchName() {
    return names.get(Language.NL);
  }

  public String getGermanName() {
    return names.get(Language.DE);
  }

  public String getFrenchName() {
    return names.get(Language.FR);
  }

  public void setDutchName(String value) {
    names.put(Language.NL, value);
  }

  public void setGermanName(String value) {
    names.put(Language.DE, value);
  }

  public void setFrenchName(String value) {
    names.put(Language.FR, value);
  }

  public void setEnglishName(String value) {
    names.put(Language.EN, value);
  }

  public String getNameForLanguage(Language language) {
    return names.get(language);
  }

}
