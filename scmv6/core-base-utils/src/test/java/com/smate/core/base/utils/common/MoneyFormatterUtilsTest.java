package com.smate.core.base.utils.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.smate.core.base.utils.common.MoneyFormatterUtils;

public class MoneyFormatterUtilsTest {

	public MoneyFormatterUtilsTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormatObject() {
		String s=new String("123,4677,000");
		String s1="1,234,677,000.00";
		Assert.assertEquals(s1,MoneyFormatterUtils.format(s));
	}

	@Test
	public void testFormatObjectString() {
		String s="  ";
		Assert.assertEquals("it's true", MoneyFormatterUtils.format(s, "it's true"));
		
	}

	@Test
	public void testFormatValueObject() {
		String s=new String("1283497345.5");
		Assert.assertEquals("1283497345.50",MoneyFormatterUtils.formatValue(s));
		
	}

	@Test
	public void testFormatValueObjectString() {
		String s=new String("123.0000");
		Assert.assertEquals("123.00",MoneyFormatterUtils.formatValue(s, "1234"));
		
		
	}

}
