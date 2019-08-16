package com.smate.core.base.utils.security;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.smate.core.base.utils.security.Des3Utils;

public class Des3UtilsTest {

	public Des3UtilsTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEncodeToDes3String() {
		Assert.assertEquals("4lG5QDMo71SkchDw%2BomPYA%3D%3D", Des3Utils.encodeToDes3("www.baidu.com"));
	}

	@Test
	public void testDecodeFromDes3String() {
		Assert.assertEquals("www.baidu.com",Des3Utils.decodeFromDes3("4lG5QDMo71SkchDw%2BomPYA%3D%3D"));
	}

	@Test
	public void testSpecEncodeToDes3() {
		Assert.assertEquals("qVOj%2F%2FiGcyyRprvClsrlLNik75gpVTyYPaonAHTuBt0%3D", Des3Utils.specEncodeToDes3("www.baidu.com:getuserinfo"));
	}

	@Test
	public void testSpecDecodeFromDes3() {
		Assert.assertEquals("www.baidu.com:getuserinfo",Des3Utils.specDecodeFromDes3("qVOj%2F%2FiGcyyRprvClsrlLNik75gpVTyYPaonAHTuBt0%3D"));
	}


}
