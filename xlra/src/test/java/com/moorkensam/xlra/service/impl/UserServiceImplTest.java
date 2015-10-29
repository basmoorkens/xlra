package com.moorkensam.xlra.service.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

public class UserServiceImplTest extends UnitilsJUnit4 {

	@TestedObject
	private UserServiceImpl service;

	@Before
	public void init() {
		service = new UserServiceImpl();
	}

	@Test
	public void testGetPasswordHash() {
		String xlraHash = "c05e198412a3608e7a626e473180472d170f0f9c95c158eb0a43583e286799f3";
		String result = service.makePasswordHash("xlra");
		Assert.assertEquals(xlraHash, result);
	}
}
