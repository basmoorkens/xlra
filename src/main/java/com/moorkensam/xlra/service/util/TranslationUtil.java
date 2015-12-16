package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.translation.Translatable;
import com.moorkensam.xlra.model.translation.TranslationKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class TranslationUtil {

  /**
   * Fill in the translations.
   * 
   * @param translatables The list of translations to fill in.
   */
  public void fillInTranslations(List<? extends Translatable> translatables) {
    for (Translatable trans : translatables) {
      fillInTranslation(trans);
    }
  }

  /**
   * Fill in a single translation.
   * 
   * @param trans The translation to fill in.
   */
  public void fillInTranslation(Translatable trans) {
    ResourceBundle bundle =
        ResourceBundle.getBundle("translations", FacesContext.getCurrentInstance().getViewRoot()
            .getLocale());
    trans.setTranslatedKey(bundle.getString(trans.getI8nKey()));
  }

  /**
   * Fetches all translation keys that are not yet in the provided ratefile.
   * 
   * @param rf The ratefile that was porvided.
   * @return the list of available translation keys.
   */
  public List<TranslationKey> getAvailableTranslationKeysForSelectedRateFile(RateFile rf) {
    List<TranslationKey> allKeys = TranslationUtil.getTranslationKeyKeys();
    if (rf != null && rf.getConditions() != null && !rf.getConditions().isEmpty()) {
      List<TranslationKey> usedKeys = new ArrayList<TranslationKey>();
      for (Condition c : rf.getConditions()) {
        usedKeys.add(c.getConditionKey());
      }
      List<TranslationKey> result = new ArrayList<TranslationKey>();
      for (TranslationKey key : allKeys) {
        if (!usedKeys.contains(key)) {
          result.add(key);
        }
      }
      return result;
    }
    return allKeys;
  }

  public static List<TranslationKey> getTranslationKeyKeys() {
    return Arrays.asList(TranslationKey.values());

  }

}
