package com.smate.core.base.utils.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.smate.core.base.utils.common.HashUtils;

public class HashUtilsTest {

	public HashUtilsTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStrHashCode() {
		String s1="1234";
		Assert.assertNotNull((HashUtils.getStrHashCode(s1)));;
	}

	@Test
	public void testGetSrUnitHashCode() {
		String s2="345";
		Assert.assertNotNull(HashUtils.getSrUnitHashCode(s2));
	}

	@Test
	public void testMain() {
		long[] b = new long[] { 1L, 2L };
		long[] c = new long[] { 1L, 2L };
		Assert.assertFalse(b.equals(c));
	}

}
