package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Department;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.TransportType;

import java.util.List;
import java.util.ResourceBundle;

public class LocaleUtil {

  /**
   * Fill in the i8nname property of the country.
   * 
   * @param countries The countries to fill in
   * @param language The language to use to fill in.
   */
  public void fillInCountryi8nNameByLanguage(List<Country> countries, Language language) {
    switch (language) {
      case EN:
        fillInEnglishName(countries);
        break;
      case NL:
        fillInDutchName(countries);
        break;
      default:
        fillInEnglishName(countries);
        break;
    }
  }


  private void fillInEnglishName(List<Country> countries) {
    for (Country c : countries) {
      c.setI8nCountryName(c.getEnglishName());
    }
  }

  private void fillInDutchName(List<Country> countries) {
    for (Country c : countries) {
      c.setI8nCountryName(c.getDutchName());
    }
  }

  /**
   * Fill in the translations for the departments.
   * 
   * @param departments The departments to fill in
   * @param messageBundle The messagebundle to use for translations
   */
  public void fillInDepartmentTranslations(List<Department> departments,
      ResourceBundle messageBundle) {
    for (Department dep : departments) {
      dep.setDescription(messageBundle.getString(dep.getI8nKey()));
    }
  }

  /**
   * Fill in the translations for transporttypes based on the given resourcebundle.
   * 
   * @param transportTypes The transporttypes to fill in
   * @param messageBundle The messageboundle to use.
   */
  public void fillInTransportTypeTranslations(List<TransportType> transportTypes,
      ResourceBundle messageBundle) {
    for (TransportType tt : transportTypes) {
      tt.setDescription(messageBundle.getString(tt.getI8nKey()));
    }
  }

  /**
   * Fill in the translations for kinds based on the given resourcebundle.
   * 
   * @param kinds List of rate kinds
   * @param messageBundle Messagebundle to use
   */
  public void fillInRateKindTranslations(List<Kind> kinds, ResourceBundle messageBundle) {
    for (Kind kind : kinds) {
      kind.setDescription(messageBundle.getString(kind.getI8nKey()));
    }
  }

  /**
   * Translate the measurements based on given resourcebundle
   * 
   * @param measurements The measurements to translate
   * @param messageBundle The messagebundle to use
   */
  public void fillInMeasurementTranslations(List<Measurement> measurements,
      ResourceBundle messageBundle) {
    for (Measurement measurement : measurements) {
      measurement.setDescription(messageBundle.getString(measurement.getI8nKey()));
    }
  }

  /**
   * Translate the languages based on given resourcebundle.
   * 
   * @param supportedLanguages The supported languages
   * @param messageBundle The resourcebundle.
   */
  public void fillInLanguageTranslations(List<Language> supportedLanguages,
      ResourceBundle messageBundle) {
    for (Language l : supportedLanguages) {
      l.setDescription(messageBundle.getString(l.getI8nKey()));
    }
  }

}
