package com.moorkensam.xlra.service.impl;

import java.util.Arrays;

import javax.persistence.NoResultException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateFileSearchFilter;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.service.util.QuotationUtil;

public class RateFileServiceTest extends UnitilsJUnit4 {

	@TestedObject
	private RateFileServiceImpl rateFileService;

	@Mock
	private RateFileDAO rfDao;

	@Mock
	private QuotationUtil quotationUtil;

	@Before
	public void init() {
		rateFileService = new RateFileServiceImpl();
		rateFileService.setRateFileDAO(rfDao);
		rateFileService.setQuotationUtil(quotationUtil);
	}

	@Test
	public void testgetRateFileForQuery() throws RateFileException {
		QuotationQuery query = new QuotationQuery();
		RateFileSearchFilter filter = new RateFileSearchFilter();
		EasyMock.expect(
				quotationUtil.createRateFileSearchFilterForQuery(query, false))
				.andReturn(filter);
		RateFile rf = new RateFile();
		EasyMock.expect(rfDao.getFullRateFileForFilter(filter)).andReturn(rf);
		EasyMockUnitils.replay();

		RateFile result = rateFileService.getRateFileForQuery(query);
	}

	@Test
	public void testgetRateFileQuery() throws RateFileException {
		QuotationQuery query = new QuotationQuery();
		RateFileSearchFilter filter = new RateFileSearchFilter();
		EasyMock.expect(
				quotationUtil.createRateFileSearchFilterForQuery(query, false))
				.andReturn(filter);
		EasyMock.expect(rfDao.getFullRateFileForFilter(filter)).andThrow(
				new NoResultException());
		RateFileSearchFilter backupFilter = new RateFileSearchFilter();
		EasyMock.expect(
				quotationUtil.createRateFileSearchFilterForQuery(query, true))
				.andReturn(backupFilter);
		RateFile rf = new RateFile();
		EasyMock.expect(rfDao.getFullRateFileForFilter(backupFilter))
				.andReturn(rf);
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
		EasyMock.expect(rfDao.getFullRateFile(rf.getId())).andReturn(rf);
		EasyMockUnitils.replay();

		RateFile result = rateFileService.deleteZone(zone);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getZones());
		Assert.assertEquals(1, result.getZones().size());
	}

	@Test
	public void testgetCopyOfRateFileForFilter() {
		RateFile rf1 = new RateFile();
		rf1.setId(1L);
		rf1.setName("test");
		EasyMock.expect(rfDao.getRateFilesForFilter(new RateFileSearchFilter()))
				.andReturn(Arrays.asList(rf1));
		EasyMock.expect(rfDao.getFullRateFile(1L)).andReturn(rf1);
		EasyMockUnitils.replay();

		RateFile rs = rateFileService
				.getCopyOfRateFileForFilter(new RateFileSearchFilter());
		Assert.assertNotNull(rs);
		Assert.assertEquals("test", rs.getName());
	}

	@Test
	public void testGetRateFileWithoutLazyLoda() {
		RateFile rf1 = new RateFile();
		rf1.setId(1L);
		rf1.setName("bla");
		RateFile rf2 = new RateFile();
		rf2.setId(2L);

		EasyMock.expect(rfDao.getAllRateFiles()).andReturn(
				Arrays.asList(rf1, rf2));
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

		EasyMock.expect(rfDao.getAllRateFiles()).andReturn(
				Arrays.asList(rf1, rf2));
		EasyMockUnitils.replay();
		RateFile result = rateFileService.getRateFileWithoutLazyLoad(3L);
		Assert.assertNull(result);
	}
}
