package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.ConditionType;
import com.moorkensam.xlra.model.translation.TranslationKey;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

public class ConditionFactoryTest extends UnitilsJUnit4 {

  private ConditionFactory conditionFactory;

  @Mock
  private TranslationUtil translationUtil;

  @Before
  public void init() {
    conditionFactory = new ConditionFactory();
    conditionFactory.setTranslationUtil(translationUtil);
  }

  @Test
  public void testCreateCondition() {
    translationUtil.fillInTranslation(EasyMock.isA(Condition.class));
    EasyMock.expectLastCall();
    EasyMockUnitils.replay();

    Condition cond = conditionFactory.createCondition(TranslationKey.ADR_MINIMUM, "100");
    Assert.assertNotNull(cond);
    Assert.assertEquals(ConditionType.CALCULATION, cond.getConditionType());
    Assert.assertEquals(TranslationKey.ADR_MINIMUM, cond.getConditionKey());
    Assert.assertNotNull(cond.getI8nKey());
  }

}
