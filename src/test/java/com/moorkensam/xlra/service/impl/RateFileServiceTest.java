package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.RateFileDao;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.QuotationUtil;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import java.util.Arrays;
import java.util.HashMap;

import javax.persistence.NoResultException;

public class RateFileServiceTest extends UnitilsJUnit4 {

  @TestedObject
  private RateFileServiceImpl rateFileService;

  @Mock
  private UserService userService;

  @Mock
  private RateFileDao rfDao;

  @Mock
  private QuotationUtil quotationUtil;

  /**
   * init the test.
   */
  @Before
  public void init() {
    rateFileService = new RateFileServiceImpl();
    rateFileService.setRateFileDao(rfDao);
    rateFileService.setQuotationUtil(quotationUtil);
    rateFileService.setUserService(userService);
  }

  @Test
  public void testgetRateFileForQuery() throws RateFileException {
    QuotationQuery query = new QuotationQuery();
    RateFileSearchFilter filter = new RateFileSearchFilter();
    EasyMock.expect(quotationUtil.createRateFileSearchFilterForQuery(query, false)).andReturn(
        filter);
    RateFile rf = new RateFile();
    EasyMock.expect(rfDao.getFullRateFileForFilter(filter)).andReturn(rf);
    EasyMockUnitils.replay();

    RateFile result = rateFileService.getRateFileForQuery(query);
  }

  @Test
  public void testgetRateFileQueryFullCustomerRateFileNotFound() throws RateFileException {
    QuotationQuery query = new QuotationQuery();
    RateFileSearchFilter filter = new RateFileSearchFilter();
    EasyMock.expect(quotationUtil.createRateFileSearchFilterForQuery(query, false)).andReturn(
        filter);
    EasyMock.expect(rfDao.getFullRateFileForFilter(filter)).andThrow(new NoResultException());
    RateFileSearchFilter backupFilter = new RateFileSearchFilter();
    EasyMock.expect(quotationUtil.createRateFileSearchFilterForQuery(query, true)).andReturn(
        backupFilter);
    RateFile rf = new RateFile();
    EasyMock.expect(rfDao.getFullRateFileForFilter(backupFilter)).andReturn(rf);
    EasyMockUnitils.replay();

    RateFile result = rateFileService.getRateFileForQuery(query);
  }

  @Test
  public void deleteZone() {
    Zone zone = new Zone();
    Zone zone2 = new Zone();
    zone.setId(1L);
    zone2.setId(2L);
    RateFile rf = new RateFile();
    rf.setId(10L);
    zone.setRateFile(rf);
    zone2.setRateFile(rf);
    rf.addZone(zone);
    rf.addZone(zone2);
    EasyMock.expect(rfDao.updateRateFile(rf)).andReturn(rf);
    String username = "bmoork";
    EasyMock.expect(userService.getCurrentUsername()).andReturn(username);
    EasyMock.expect(rfDao.getFullRateFile(rf.getId())).andReturn(rf);
    EasyMockUnitils.replay();

    RateFile result = rateFileService.deleteZone(zone);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getZones());
    Assert.assertEquals(1, result.getZones().size());
    Assert.assertEquals(result.getLastEditedBy(), username);
  }

  @Test
  public void testgetCopyOfRateFileForFilter() throws RateFileException {
    RateFile rf1 = new RateFile();
    Customer customer = new Customer();
    customer.setName("testje");
    rf1.setId(1L);
    rf1.setName("test");
    EasyMock.expect(rfDao.getRateFilesForFilter(new RateFileSearchFilter())).andReturn(
        Arrays.asList(rf1));
    EasyMock.expect(rfDao.getFullRateFile(1L)).andReturn(rf1);
    EasyMockUnitils.replay();
    RateFileSearchFilter filter = new RateFileSearchFilter();
    filter.setCountry(new Country());
    filter.getCountry().setNames(new HashMap<Language, String>());
    filter.getCountry().setEnglishName("be");
    filter.setMeasurement(Measurement.KILO);
    RateFile rs = rateFileService.generateCustomerRateFileForFilterAndCustomer(filter, customer);
    Assert.assertNotNull(rs);
    Assert.assertEquals("testje - be - Kilo - null - null", rs.getName());
  }

  @Test
  public void testGetRateFileWithoutLazyLoda() {
    RateFile rf1 = new RateFile();
    rf1.setId(1L);
    rf1.setName("bla");
    RateFile rf2 = new RateFile();
    rf2.setId(2L);

    EasyMock.expect(rfDao.getAllRateFiles()).andReturn(Arrays.asList(rf1, rf2));
    EasyMockUnitils.replay();
    RateFile result = rateFileService.getRateFileWithoutLazyLoad(1L);
    Assert.assertNotNull(result);
    Assert.assertEquals("bla", result.getName());
  }

  @Test
  public void testGetRateFileWithoutLazyLodaNotFound() {
    RateFile rf1 = new RateFile();
    rf1.setId(1L);
    RateFile rf2 = new RateFile();
    rf2.setId(2L);

    EasyMock.expect(rfDao.getAllRateFiles()).andReturn(Arrays.asList(rf1, rf2));
    EasyMockUnitils.replay();
    RateFile result = rateFileService.getRateFileWithoutLazyLoad(3L);
    Assert.assertNull(result);
  }
}
