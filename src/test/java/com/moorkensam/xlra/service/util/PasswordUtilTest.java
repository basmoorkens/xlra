package com.moorkensam.xlra.service.util;

import org.junit.Assert;
import org.junit.Test;

public class PasswordUtilTest {

  private static final String xlraHash =
      "c05e198412a3608e7a626e473180472d170f0f9c95c158eb0a43583e286799f3";

  @Test
  public void testGetPasswordHash() {
    String result = PasswordUtil.makePasswordHash("xlra");
    Assert.assertEquals(xlraHash, result);
  }

}
