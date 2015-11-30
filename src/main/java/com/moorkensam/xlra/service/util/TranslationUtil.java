package com.moorkensam.xlra.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.translation.TranslationKey;

public class TranslationUtil {

	public static List<TranslationKey> getAvailableTranslationKeysForSelectedRateFile(
			RateFile rf) {
		List<TranslationKey> allKeys = TranslationUtil.getTranslationKeyKeys();
		if (rf != null && rf.getConditions() != null
				&& !rf.getConditions().isEmpty()) {
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
