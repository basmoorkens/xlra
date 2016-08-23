package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.configuration.Language;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

public class UserUtilTest extends UnitilsJUnit4 {

  private ResourceBundle resourceBundle;

  /**
   * init a dummy impl of resourcebundle for the test.
   */
  @Before
  public void init() {
    resourceBundle = new ResourceBundle() {

      @Override
      protected Object handleGetObject(String key) {
        return "dummy impl";
      }

      @Override
      public Enumeration<String> getKeys() {
        return Collections.emptyEnumeration();
      }
    };
  }

  @Test
  public void testGetSupportedLocales() {
    List<Language> languages = UserUtil.getInstance(resourceBundle).getSupportedLanguages();
    Assert.assertNotNull(languages);
    Assert.assertEquals(2, languages.size());
    Assert.assertEquals(Language.EN, languages.get(0));
    Assert.assertEquals(Language.NL, languages.get(1));
  }

  @Test
  public void testGetDefaultUserLocale() {
    Language result = UserUtil.getInstance(resourceBundle).getDefaultUserLanguage();
    Assert.assertEquals(Language.EN, result);
  }
}
