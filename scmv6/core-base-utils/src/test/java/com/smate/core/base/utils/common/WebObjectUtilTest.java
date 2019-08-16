package com.smate.core.base.utils.common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.smate.core.base.utils.common.WebObjectUtil;

public class WebObjectUtilTest {

	public WebObjectUtilTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsStrNvl() {
		String s="   ";
		boolean b=WebObjectUtil.isStrNvl(s);
		Assert.assertEquals(true, b);
	}

	@Test
	public void testCovertListToJson() {
		A a=new A("a","a");
		A b=new A("b","b");
		List<A> list=new ArrayList<A>();
		list.add(a);
		list.add(b);
		//System.out.println(WebObjectUtil.covertListToJson(list));
		
	}

	@Test
	public void testCovertMapToJson() {
		Map m=new HashMap();
		m.put("key", "value");
		m.put("name","zjh");
		Assert.assertEquals("{'name':'zjh','key':'value'}",WebObjectUtil.covertMapToJson(m));
	}

	@Test
	public void testGetYYYYMMDDDir() {
		Assert.assertEquals("2015/06/03/", WebObjectUtil.getYYYYMMDDDir());
	}

	@Test
	public void testCovertDateToYMD() {
		Date date=new Date();
		Assert.assertEquals("2015/06/03", WebObjectUtil.covertDateToYMD(date));
		
		
	}

	@Test
	public void testGetFileNameWithoutExt() {
		String s="file.doc";
		Assert.assertEquals("file",WebObjectUtil.getFileNameWithoutExt(s));
		
	}

	@Test
	public void testGetFileNameExt() {
		String s="file.txt";
		System.out.println(WebObjectUtil.getFileNameExt(s));
	}

	@Test
	public void testFormateFileName() {
		
	}

	@Test
	public void testProcessFileName() {
		
	}

	@Test
	public void testReplaceJsonStr() {
		
	}

	@Test
	public void testGetFileType() {
		
	}

}
