package com.moorkensam.xlra.service.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.TranslationDAO;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.service.TranslationService;

public class TranslationServiceImplTest extends UnitilsJUnit4 {

	private TranslationServiceImpl translationService;

	@org.unitils.easymock.annotation.Mock
	private TranslationDAO translationDAO;

	private List<Translation> translations;

	@Before
	public void init() {
		translationService = new TranslationServiceImpl();
		translationService.setTranslationDAO(translationDAO);
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
		EasyMock.expect(translationDAO.getAllNonDeletedTranslations())
				.andReturn(translations);
		EasyMockUnitils.replay();
		Translation translation = translationService
				.findTranslationInCacheByTranslationKeyAndLanguage(
						TranslationKey.ADR_MINIMUM, Language.EN);

		Assert.assertEquals(translation.getText(), "OK");
	}

}
