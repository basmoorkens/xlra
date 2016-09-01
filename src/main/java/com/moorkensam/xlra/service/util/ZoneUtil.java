package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.configuration.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZoneUtil {

  /**
   * convert the alphanumerical postalcodes string to a list.
   */
  public List<String> convertAlphaNumericPostalCodeStringToList(String alphaNumericalSTring) {
    if (alphaNumericalSTring != null && !alphaNumericalSTring.isEmpty()) {
      String[] alphaArray = alphaNumericalSTring.split(",");
      alphaArray = trimSpaces(alphaArray);
      return Arrays.asList(alphaArray);
    }
    return new ArrayList<String>();
  }

  /**
   * convert the numericalpostalcodes string to a list.
   */
  public List<Interval> convertNumericalPostalCodeStringToList(String numericalString) {
    if (numericalString != null && !numericalString.isEmpty()) {
      List<Interval> intervals = new ArrayList<Interval>();
      String[] numericArray = numericalString.split(",");
      numericArray = trimSpaces(numericArray);
      for (String s : numericArray) {
        String[] ints = s.split("-");
        ints = trimSpaces(ints);
        Interval interval = new Interval(ints);
        intervals.add(interval);
      }
      return intervals;
    }
    return new ArrayList<Interval>();
  }

  /**
   * Convert the alphanumeric postal codes to a string list.
   */
  public String convertAlphaNumericPostalCodeListToString(List<String> alphaNumericZones) {
    if (alphaNumericZones != null && !alphaNumericZones.isEmpty()) {
      String alphaCodes = "";
      for (String s : alphaNumericZones) {
        alphaCodes += (s + ",");
      }

      alphaCodes = alphaCodes.substring(0, alphaCodes.length() - 1);
      return alphaCodes;
    }
    return "";
  }

  /**
   * fill in the numericalpostal codes from the list.
   */
  public String convertNumericalPostalCodeListToString(List<Interval> numericPostalCodes) {
    if (numericPostalCodes != null && !numericPostalCodes.isEmpty()) {
      String numericalCodes = "";
      for (Interval interval : numericPostalCodes) {
        numericalCodes += interval.toIntString() + ",";
      }
      numericalCodes = numericalCodes.substring(0, numericalCodes.length() - 1);
      return numericalCodes;
    }
    return "";
  }

  /**
   * Trim the spaces from an input array.
   * 
   * @param input the array.
   * @return the trimmed array.
   */
  public String[] trimSpaces(String[] input) {
    String[] result = new String[input.length];
    for (int i = 0; i < input.length; i++) {
      result[i] = input[i].trim();
    }
    return result;
  }

}
