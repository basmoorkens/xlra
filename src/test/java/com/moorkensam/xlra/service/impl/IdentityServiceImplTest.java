package com.moorkensam.xlra.service.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class IdentityServiceImplTest extends UnitilsJUnit4 {

	private IdentityService service;

	@Before
	public void init() {
		service = IdentityService.getInstance();
	}

	@Test
	public void testGetIdentifierUnique() {
		String id = service.getNextIdentifier();
		String id2 = service.getNextIdentifier();
		Assert.assertFalse(id.equals(id2));
	}

}
