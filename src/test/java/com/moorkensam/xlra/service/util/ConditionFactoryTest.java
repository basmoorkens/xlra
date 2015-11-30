package com.moorkensam.xlra.service.util;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.ConditionType;
import com.moorkensam.xlra.model.translation.TranslationKey;

public class ConditionFactoryTest extends UnitilsJUnit4 {

	private ConditionFactory conditionFactory;

	@Before
	public void init() {
		conditionFactory = new ConditionFactory();
	}

	@Test
	public void testCreateCondition() {
		Condition cond = conditionFactory.createCondition(
				TranslationKey.ADR_MINIMUM, "100");
		Assert.assertNotNull(cond);
		Assert.assertEquals(ConditionType.CALCULATION, cond.getConditionType());
		Assert.assertEquals(TranslationKey.ADR_MINIMUM, cond.getConditionKey());
	}

}
