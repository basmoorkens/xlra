package com.moorkensam.xlra.service.util;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class FileUtilTest extends UnitilsJUnit4 {

	@Test
	public void testParseFileNameFromPath() {
		String result = FileUtil.getFileNameFromPath("../test/test1.txt");
		Assert.assertEquals("test1.txt", result);
	}

	@Test
	public void testParseFileNameFromPathWithNull() {
		String result = FileUtil.getFileNameFromPath(null);
		Assert.assertEquals("", result);
	}

	@Test
	public void testParseFileNameWithoutSlashInIt() {
		String result = FileUtil.getFileNameFromPath("test.txt");
		Assert.assertEquals("test.txt", result);
	}

}
