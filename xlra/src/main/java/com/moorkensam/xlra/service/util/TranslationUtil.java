package com.moorkensam.xlra.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.moorkensam.xlra.model.configuration.TranslationKey;

public class TranslationUtil {

	public static List<TranslationKey> getTranslationsNotKey() {
		List<TranslationKey> keys = Arrays.asList(TranslationKey.values());
		List<TranslationKey> result = new ArrayList<TranslationKey>();
		for (TranslationKey key : keys) {
			if (!key.isKey()) {
				result.add(key);
			}
		}
		return keys;
	}

	public static List<TranslationKey> getTranslationKeys() {
		List<TranslationKey> keys = Arrays.asList(TranslationKey.values());
		List<TranslationKey> result = new ArrayList<TranslationKey>();
		for (TranslationKey key : keys) {
			if (key.isKey()) {
				result.add(key);
			}
		}
		return keys;
	}

}
