package com.moorkensam.xlra.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;

import com.moorkensam.xlra.dao.TranslationDAO;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationForLanguage;
import com.moorkensam.xlra.model.configuration.TranslationKey;

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
		TranslationForLanguage tfl = new TranslationForLanguage();
		tfl.setLanguage(Language.EN);
		tr.setTranslationKey(TranslationKey.ADR_MINIMUM);
		tfl.setTranslation("OK");
		Translation tr2 = new Translation();
		TranslationForLanguage tfl2 = new TranslationForLanguage();
		tfl2.setLanguage(Language.FR);
		tfl2.setTranslation("NOK");
		tr.setTranslations(Arrays.asList(tfl, tfl2));
		translations.add(tr);
		translations.add(tr2);
	}

	@Test
	public void testGetCachedEntry() {
		EasyMock.expect(translationDAO.getAllNonDeletedTranslations())
				.andReturn(translations);
		EasyMockUnitils.replay();
		Translation translation = translationService
				.findTranslationInCacheByTranslationKey(TranslationKey.ADR_MINIMUM);

		Assert.assertEquals(translation.getTranslationForLanguage(Language.EN)
				.getTranslation(), "OK");
		Assert.assertEquals(translation.getTranslationForLanguage(Language.FR)
				.getTranslation(), "NOK");
	}

}
