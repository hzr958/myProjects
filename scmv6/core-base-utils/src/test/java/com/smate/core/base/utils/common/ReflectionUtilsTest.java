package com.smate.core.base.utils.common;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.smate.core.base.utils.common.ReflectionUtils;


public class ReflectionUtilsTest {

	public ReflectionUtilsTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetFieldValue() {
		A a=new A("t1","t2");
		Object s=ReflectionUtils.getFieldValue(a, "test1");
		Assert.assertEquals("t1",s);
	}

	@Test
	public void testSetFieldValue() {
		A a = new A("t1","t2");
		ReflectionUtils.setFieldValue(a, "test1", "hello");
		Assert.assertEquals("hello", ReflectionUtils.getFieldValue(a, "test1"));
	}

	@Test
	public void testInvokeMethod() {
		A a=new A("hi","hi");
	//怎么写？
		
	}



	@Test
	public void testGetDeclaredMethod() {
		//不会写
		
	}

	@Test
	public void testGetSuperClassGenricTypeClass() {
		//不会写
		
	}

	@Test
	public void testGetSuperClassGenricTypeClassInt() {
		//不会写
		
	}

	@Test
	public void testConvertElementPropertyToList() {
		
	}

	@Test
	public void testConvertElementPropertyToString() {
		
	}

	@Test
	public void testConvertValue() {
		
	}

	@Test
	public void testConvertReflectionExceptionToUnchecked() {
		
	}

}
