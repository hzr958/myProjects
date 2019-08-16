package com.smate.core.base.utils.common;

import java.lang.reflect.InvocationTargetException;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BeanUtilsTest extends TestCase {
	public BeanUtilsTest(String name) {
		super(name);
	}

	BeanUtils utils = new BeanUtils();
	A a = new A("test1", "test2");
	B b = new B();

	public void testcopyProperties() throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(a, b);
		Assert.assertNotNull("copyProperties", a);
	}

	public void testcopyPropertiesForObject() {

	}

	public void testcopyProperty() {

	}

	public void testregistryBean() {

	}

	public void testcompareListObject() {

	}

	public void testDateConvert() {

	}

}
