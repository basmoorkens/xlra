package com.moorkensam.xlra.mapper;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.model.EmailResult;

public class OfferteMailToEmailResultMapperTest extends UnitilsJUnit4 {

	@TestedObject
	private OfferteEmailToEmailResultMapper mapper;

	private OfferteMailDTO dto;

	@Test
	public void testMapper() {
		dto = new OfferteMailDTO();
		dto.setSubject("subject");
		dto.setContent("content");
		dto.setAddress("bas");

		mapper = new OfferteEmailToEmailResultMapper();
		EmailResult map = mapper.map(dto);
		Assert.assertEquals(dto.getContent(), map.getEmail());
		Assert.assertEquals(dto.getSubject(), map.getSubject());
		Assert.assertEquals(dto.getAddress(), map.getToAddress());
	}

}
