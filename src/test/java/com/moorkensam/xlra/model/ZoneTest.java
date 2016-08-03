package com.moorkensam.xlra.model;

import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.util.ArrayList;

public class ZoneTest extends UnitilsJUnit4 {

  @TestedObject
  private Zone original;


  @Test
  public void testDeepCopy() {
    original = new Zone();
    original.setCountry(new Country());
    original.getCountry().setShortName("BE");
    original.setName("test");
    original.setZoneType(ZoneType.ALPHANUMERIC_LIST);
    original.setAlphaNumericalPostalCodes(new ArrayList<String>());
    original.getAlphaNumericalPostalCodes().add("oki");
    Zone copy = original.deepCopy();
    Assert.assertEquals("BE", copy.getCountry().getShortName());
    Assert.assertEquals("test", copy.getName());
    Assert.assertEquals(ZoneType.ALPHANUMERIC_LIST, copy.getZoneType());
    Assert.assertEquals(1, copy.getAlphaNumericalPostalCodes().size());
  }

}
