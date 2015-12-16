package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.security.TokenInfo;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class TokenUtilTest extends UnitilsJUnit4 {

  @Test
  public void testGetNextToken() {
    TokenInfo ti = TokenUtil.getNextToken();
    Assert.assertNotNull(ti);
    Assert.assertNotNull(ti.getVerificationToken());
    Assert.assertNotNull(ti.getValidTo());
  }
}
