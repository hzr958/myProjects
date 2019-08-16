package com.smate.core.base.utils.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.smate.core.base.utils.common.LocaleTextUtils;

public class LocaleTextUtilsTest {

	public LocaleTextUtilsTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLocaleText() {
		Assert.assertEquals("中文",LocaleTextUtils.getLocaleText(null, "中文", "enText"));
	}

}
