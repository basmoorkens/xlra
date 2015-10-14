package com.moorkensam.xlra.service.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import com.moorkensam.xlra.dao.TranslationDAO;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.service.TranslationService;

public class TranslationServiceImplTest extends UnitilsJUnit4 {

	@TestedObject
	private TranslationService translationService;

	@InjectIntoByType
	private Mock<TranslationDAO> translationDAO;

	private List<Translation> translations;

	@Before
	public void init() {
		translations = new ArrayList<Translation>();
		Translation tr = new Translation();
		tr.setLanguage(Language.EN);
		tr.setTranslationKey(TranslationKey.ADR_MINIMUM);
		tr.setText("OK");
		Translation tr2 = new Translation();
		tr2.setLanguage(Language.FR);
		tr2.setText("NOK");
		tr2.setTranslationKey(TranslationKey.ADR_MINIMUM);
		translations.add(tr);
		translations.add(tr2);
	}

	@Test
	public void testGetCachedEntry() {
		translationDAO.returns(translations).getAllNonDeletedTranslations();

		Translation translation = translationService
				.findTranslationInCacheByTranslationKeyAndLanguage(
						TranslationKey.ADR_MINIMUM, Language.EN);

		Assert.assertEquals(translation.getText(), "OK");
	}

}
