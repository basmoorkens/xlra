package com.moorkensam.xlra.controller;

import java.util.Arrays;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.RateFileService;

public class ManageRatesControllerTest extends UnitilsJUnit4 {

  @TestedObject
  ManageRatesController controller;

  @Mock
  private RateFileService rfService;

  private RateFile rf;

  /**
   * Exec before test.
   */
  @Before
  public void init() {
    rf = new RateFile();
    rf.setName("TEST1");
    rf.setId(1);
    controller = new ManageRatesController();
    controller.setRateFileService(rfService);
    controller.setRateFiles(Arrays.asList(rf));
    controller.setSelectedRateFile(new RateFile());
  }

  @Test
  public void testSaveRateFile() {
    RateFile newRf = new RateFile();
    controller.setSelectedRateFile(newRf);
    EasyMock.expect(rfService.updateRateFile(newRf)).andReturn(newRf);
    EasyMockUnitils.replay();

    controller.saveRateFile();
  }

  @Test
  public void testGetRateFileByIdNotfound() {
    RateFile result = controller.getRateFileById(-1);
    Assert.assertNull(result);
  }

  @Test
  public void testGetRateFileById() {
    RateFile result = controller.getRateFileById(1);
    Assert.assertNotNull(result);
    Assert.assertEquals("TEST1", result.getName());
  }
}
