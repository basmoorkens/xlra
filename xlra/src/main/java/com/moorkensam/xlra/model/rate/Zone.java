package com.moorkensam.xlra.model.rate;

import java.util.Arrays;
import java.util.List;

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

import com.moorkensam.xlra.model.BaseEntity;

@Entity
@Table(name = "zone")
public class Zone extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;

	@Enumerated(EnumType.STRING)
	private ZoneType zoneType;

	@ElementCollection
	@CollectionTable(name = "alphanumericalpostalcodes", joinColumns = @JoinColumn(name = "zone_id"))
	@Column(name = "postalcode")
	private List<String> alphaNumericalPostalCodes;

	private int startCode, stopCode;

	@Transient
	private String alphaNumericPostalCodesAsString;

	@ManyToOne
	@JoinColumn(name = "countryId")
	private Country country;

	public int getStopCode() {
		return stopCode;
	}

	public void setStopCode(int stopCode) {
		this.stopCode = stopCode;
	}

	public int getStartCode() {
		return startCode;
	}

	public void setStartCode(int startCode) {
		this.startCode = startCode;
	}

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

	public void setAlphaNumericPostalCodesAsString(
			String alphaNumericPostalCodesAsString) {
		this.alphaNumericPostalCodesAsString = alphaNumericPostalCodesAsString;
	}

	public void convertAlphaNumericPostalCodeListToString() {
		if (alphaNumericalPostalCodes != null
				&& !alphaNumericalPostalCodes.isEmpty()) {
			String alphaCodes = "";
			for (String s : alphaNumericalPostalCodes) {
				alphaCodes += (s + ",");
			}

			alphaCodes = alphaCodes.substring(0, alphaCodes.length() - 1);
			setAlphaNumericPostalCodesAsString(alphaCodes);

		}
	}

	public void convertAlphaNumericPostalCodeStringToList() {
		String[] alphaArray = alphaNumericPostalCodesAsString.split(",");
		setAlphaNumericalPostalCodes(Arrays.asList(alphaArray));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
