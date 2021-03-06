package com.moorkensam.xlra.service.util.excel;

import com.moorkensam.xlra.model.ExcelUploadUtilData;
import com.moorkensam.xlra.model.RateLineExcelImportDto;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.util.ConditionFactory;
import com.moorkensam.xlra.service.util.TranslationUtil;
import com.moorkensam.xlra.service.util.excel.ExcelToModelMapper;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelToModelMapperTest extends UnitilsJUnit4 {

  @TestedObject
  private ExcelToModelMapper mapper;

  private ConditionFactory conditionFactory;

  private ExcelUploadUtilData data;

  @Mock
  private TranslationUtil translationUtil;

  private RateFile rf;

  /**
   * Inits the test.
   */
  @Before
  public void init() {
    mapper = new ExcelToModelMapper();
    data = new ExcelUploadUtilData();
    conditionFactory = new ConditionFactory();
    conditionFactory.setTranslationUtil(translationUtil);
    mapper.setConditionFactory(conditionFactory);
    rf = new RateFile();
    List<RateLineExcelImportDto> dtoList = new ArrayList<RateLineExcelImportDto>();
    RateLineExcelImportDto dto = new RateLineExcelImportDto();
    dto.setValue(100d);
    dto.setZone("Zone 1");
    dtoList.add(dto);
    RateLineExcelImportDto dto2 = new RateLineExcelImportDto();
    dto2.setValue(150d);
    dto2.setZone("Zone 2");
    dtoList.add(dto2);
    Map<Double, List<RateLineExcelImportDto>> ratesMap =
        new HashMap<Double, List<RateLineExcelImportDto>>();
    ratesMap.put(1d, dtoList);
    data.setRatesMap(ratesMap);
    Map<String, List<String>> termsMap = new HashMap<String, List<String>>();
    data.setTermsMap(termsMap);
    termsMap.put("IMPORT_FORM", Arrays.asList("blabla1"));
    termsMap.put("EXPORT_FORM", Arrays.asList("blabla2"));
    Map<Integer, String> zoneMap = new HashMap<Integer, String>();
    Map<Integer, List<String>> zoneValuesMap = new HashMap<Integer, List<String>>();
    data.setZoneMap(zoneMap);
    data.setZoneValuesMap(zoneValuesMap);
    zoneMap.put(1, "Zone 1");
    zoneMap.put(2, "Zone 2");
    zoneValuesMap.put(1, Arrays.asList("blabla"));
    zoneValuesMap.put(2, Arrays.asList("boemboem"));
    rf.setCountry(new Country());
    rf.getCountry().setZoneType(ZoneType.ALPHANUMERIC_LIST);

  }

  @Test
  public void testCreateZones() {
    List<Zone> zones = mapper.createZones(rf, data);
    Assert.assertNotNull(zones);
    Assert.assertTrue(zones.size() > 0);
  }

  @Test
  public void testConcatTermValues() {
    List<String> input = new ArrayList<String>();
    input.add("test");
    input.add("bas");
    String result = mapper.concatTermValues(input);
    Assert.assertEquals("testbas", result);
  }

  @Test
  public void testCreateConditions() {
    translationUtil.fillInTranslation(EasyMock.isA(Condition.class));
    EasyMock.expectLastCall();
    translationUtil.fillInTranslation(EasyMock.isA(Condition.class));
    EasyMock.expectLastCall();
    EasyMockUnitils.replay();

    List<Condition> conditions = mapper.createConditions(data, rf);
    Assert.assertNotNull(conditions);
    Assert.assertTrue(conditions.size() > 0);
    for (Condition c : conditions) {
      if (c.getConditionKey() == TranslationKey.IMPORT_FORM) {
        Assert.assertEquals("blabla1", c.getValue());
      }
      if (c.getConditionKey() == TranslationKey.EXPORT_FORM) {
        Assert.assertEquals("blabla2", c.getValue());
      }
    }
  }

  @Test
  public void testCreateRatelines() {
    rf.setZones(new ArrayList<Zone>());
    Zone zone = new Zone();
    zone.setName("Zone 1");
    Zone zone2 = new Zone();
    zone2.setName("Zone 2");
    rf.getZones().add(zone);
    rf.getZones().add(zone2);

    List<RateLine> lines = mapper.createRateLines(data, rf);
    Assert.assertEquals(2, lines.size());
    Assert.assertEquals(new BigDecimal(100d), lines.get(0).getValue());
    Assert.assertEquals("Zone 1", lines.get(0).getZone().getName());
    Assert.assertEquals(1d, lines.get(0).getMeasurement());
    Assert.assertEquals(new BigDecimal(150d), lines.get(1).getValue());
    Assert.assertEquals("Zone 2", lines.get(1).getZone().getName());
    Assert.assertEquals(1d, lines.get(1).getMeasurement());
  }
}
