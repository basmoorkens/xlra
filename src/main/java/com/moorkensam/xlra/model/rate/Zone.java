package com.moorkensam.xlra.model.rate;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Interval;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Cacheable
@Table(name = "zone")
public class Zone extends BaseEntity {

  public Zone() {}

  public Zone(String name) {
    this.name = name;
  }

  private static final long serialVersionUID = 1L;

  private String name;

  @Enumerated(EnumType.STRING)
  private ZoneType zoneType;

  @ElementCollection
  @CollectionTable(name = "alphanumericalpostalcodes", joinColumns = @JoinColumn(name = "zone_id"))
  @Column(name = "postalcode")
  private List<String> alphaNumericalPostalCodes;

  @ElementCollection
  @CollectionTable(name = "numericalpostalcodes", joinColumns = @JoinColumn(name = "zone_id"))
  @Column(name = "postalcode")
  private List<Interval> numericalPostalCodes;

  @ManyToOne
  @JoinColumn(name = "rateFileId")
  private RateFile rateFile;

  @Transient
  private String alphaNumericPostalCodesAsString;

  @Transient
  private String numericalPostalCodesAsString;

  @ManyToOne
  @JoinColumn(name = "countryId")
  private Country country;

  private String extraInfo;

  public List<String> getAlphaNumericalPostalCodes() {
    return alphaNumericalPostalCodes;
  }

  public void setAlphaNumericalPostalCodes(List<String> toSet) {
    this.alphaNumericalPostalCodes = toSet;
  }

  public ZoneType getZoneType() {
    return zoneType;
  }

  public void setZoneType(ZoneType zoneType) {
    this.zoneType = zoneType;
  }

  public Country getCountry() {
    return this.country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public String getAlphaNumericPostalCodesAsString() {
    return alphaNumericPostalCodesAsString;
  }

  public void setAlphaNumericPostalCodesAsString(String alphaNumericPostalCodesAsString) {
    this.alphaNumericPostalCodesAsString = alphaNumericPostalCodesAsString;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumericalPostalCodesAsString() {
    return numericalPostalCodesAsString;
  }

  public void setNumericalPostalCodesAsString(String numericalPostalCodesAsString) {
    this.numericalPostalCodesAsString = numericalPostalCodesAsString;
  }

  public List<Interval> getNumericalPostalCodes() {
    return numericalPostalCodes;
  }

  public void setNumericalPostalCodes(List<Interval> numericalPostalCodes) {
    this.numericalPostalCodes = numericalPostalCodes;
  }

  public RateFile getRateFile() {
    return rateFile;
  }

  public void setRateFile(RateFile rateFile) {
    this.rateFile = rateFile;
  }

  public String getExtraInfo() {
    return extraInfo;
  }

  public void setExtraInfo(String extraInfo) {
    this.extraInfo = extraInfo;
  }

  /**
   * Get the column header for the zone.
   * 
   * @return the header.
   */
  public String getAsColumnHeader() {
    String header = name + " ";
    if (zoneType == ZoneType.ALPHANUMERIC_LIST) {
      header += "( " + getAlphaNumericPostalCodesAsString() + " )";
    } else {
      header += "( " + getNumericalPostalCodesAsString() + " )";
    }

    return header;
  }

  /**
   * deep copy function.
   * 
   * @return the deep copy.
   */
  public Zone deepCopy() {
    Zone copy = new Zone();
    copy.fillInValuesFromZone(this);
    copy.setRateFile(null);
    return copy;
  }

  /**
   * Fill in values from a different zone.
   * 
   * @param zone The zone to fill values in from.
   */
  public void fillInValuesFromZone(Zone zone) {
    this.setName(zone.getName());
    this.setAlphaNumericPostalCodesAsString(zone.getAlphaNumericPostalCodesAsString());
    this.setNumericalPostalCodesAsString(zone.getNumericalPostalCodesAsString());
    this.setAlphaNumericalPostalCodes(zone.getAlphaNumericalPostalCodes());
    this.setNumericalPostalCodes(zone.getNumericalPostalCodes());
    this.setCountry(zone.getCountry());
    this.setExtraInfo(zone.getExtraInfo());
    this.setZoneType(zone.getZoneType());
  }

}
