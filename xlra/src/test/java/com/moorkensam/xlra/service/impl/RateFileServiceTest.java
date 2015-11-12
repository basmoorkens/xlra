package com.moorkensam.xlra.service.impl;

import java.util.Arrays;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.RateFileDAO;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.searchfilter.RateFileSearchFilter;

public class RateFileServiceTest extends UnitilsJUnit4 {

	@TestedObject
	private RateFileServiceImpl rateFileService;

	@Mock
	private RateFileDAO rfDao;

	@Before
	public void init() {
		rateFileService = new RateFileServiceImpl();
		rateFileService.setRateFileDAO(rfDao);
	}

	@Test
	public void testgetCopyOfRateFileForFilter() {
		RateFile rf1 = new RateFile();
		rf1.setId(1l);
		rf1.setName("test");
		EasyMock.expect(rfDao.getRateFilesForFilter(new RateFileSearchFilter()))
				.andReturn(Arrays.asList(rf1));
		EasyMock.expect(rfDao.getFullRateFile(1l)).andReturn(rf1);
		EasyMockUnitils.replay();

		RateFile rs = rateFileService
				.getCopyOfRateFileForFilter(new RateFileSearchFilter());
		Assert.assertNotNull(rs);
		Assert.assertEquals("test", rs.getName());
	}
}
