package com.smate.core.base.utils.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.smate.core.base.utils.common.URIencodeUtils;

public class URIencodeUtilsTet {

	public URIencodeUtilsTet() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDecodeURIComponent() {
		String s=URIencodeUtils.decodeURIComponent("%E7%BE%8E%E4%B8%BDDNA%E7%A0%94%E7%A9%B6");
		Assert.assertEquals("美丽DNA研究", s);
	}

	@Test
	public void testEncodeURIComponent() {
		String s=URIencodeUtils.encodeURIComponent("美丽DNA研究");
		String s1="%E7%BE%8E%E4%B8%BDDNA%E7%A0%94%E7%A9%B6";
	    Assert.assertEquals(s1, s);
	}
}
