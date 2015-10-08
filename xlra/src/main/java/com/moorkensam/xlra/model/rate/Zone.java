package com.moorkensam.xlra.model.rate;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Interval;

@Entity
@Cacheable
@Table(name = "zone")
public class Zone extends BaseEntity {

	public Zone() {

	}

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
	@CollectionTable(name = "numericalPostalcodes", joinColumns = @JoinColumn(name = "zone_id"))
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

	public void convertNumericalPostalCodeListToString() {
		if (numericalPostalCodes != null && !numericalPostalCodes.isEmpty()) {
			String numericalCodes = "";
			for (Interval interval : numericalPostalCodes) {
				numericalCodes += interval.toIntString() + ",";
			}
			numericalCodes.substring(0, numericalCodes.length() - 1);
			setNumericalPostalCodesAsString(numericalCodes);
		}
	}

	public void convertNumericalPostalCodeStringToList() {
		if (numericalPostalCodesAsString != null
				&& !numericalPostalCodesAsString.isEmpty()) {
			List<Interval> intervals = new ArrayList<Interval>();
			String[] numericArray = numericalPostalCodesAsString.split(",");
			numericArray = trimSpaces(numericArray);
			for (String s : numericArray) {
				String[] ints = s.split("-");
				ints = trimSpaces(ints);
				Interval interval = new Interval(ints);
				intervals.add(interval);
			}
			setNumericalPostalCodes(intervals);
		}
	}

	public String[] trimSpaces(String[] input) {
		String[] result = new String[input.length];
		for (int i = 0; i < input.length; i++) {
			result[i] = input[i].trim();
		}
		return result;
	}

	public void convertAlphaNumericPostalCodeStringToList() {
		if (alphaNumericPostalCodesAsString != null
				&& !alphaNumericPostalCodesAsString.isEmpty()) {
			String[] alphaArray = alphaNumericPostalCodesAsString.split(",");
			alphaArray = trimSpaces(alphaArray);
			setAlphaNumericalPostalCodes(Arrays.asList(alphaArray));
		}
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

	public void setNumericalPostalCodesAsString(
			String numericalPostalCodesAsString) {
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

	public String getAsColumnHeader() {
		String header = name + " ";
		if (zoneType == ZoneType.ALPHANUMERIC_LIST) {
			header += "( " + getAlphaNumericPostalCodesAsString() + " )";
		} else {
			header += "( " + getNumericalPostalCodesAsString() + " )";
		}

		return header;
	}

	public Zone deepCopy() {
		Zone copy = new Zone();
		copy.setName(name);
		copy.setAlphaNumericPostalCodesAsString(alphaNumericPostalCodesAsString);
		copy.setNumericalPostalCodesAsString(numericalPostalCodesAsString);
		copy.setCountry(country);
		copy.setExtraInfo(extraInfo);
		copy.setZoneType(zoneType);
		return copy;
	}

}
