package com.smate.core.base.utils.common;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.smate.core.base.utils.common.HqlUtils;

public class HqlUtilsTest {

	public HqlUtilsTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetHqlparams() {
		List<Long> idList=new ArrayList<Long>();
		Long[] lon={1l,2l,3l,4l,5l,6l,7l};
		for(int i=0;i<lon.length;i++){
		idList.add(lon[i]);
		}
		String str1=HqlUtils.getHqlparams(idList);
		Assert.assertEquals("?,?,?,?,?,?,?", str1);
	}

	@Test
	public void testGetHqlparamsById() {
		List<Long> idList=new ArrayList<Long>();
		Long[] lon={1l,2l,3l,4l,5l,6l,7l};
		for(int i=0;i<lon.length;i++){
		idList.add(lon[i]);
		}
		String str1=HqlUtils.getHqlparamsById(idList);
		Assert.assertEquals("1,2,3,4,5,6,7", str1);
		
	}

	@Test
	public void testInsIdsFormat() {
		String str="12,34,56,45,23";
		Assert.assertEquals("12,23,34,45,56",HqlUtils.insIdsFormat(str));
	}

	@Test
	public void testRemoveDuplicateWithOrder() {
		List list=new ArrayList();
		list.add("me");
		list.add("she");
		list.add("me");
		list.add("upi");
		list.add("kit");
		list.add("upi");
		List l=new ArrayList();
		l.add("me");
		l.add("she");
		l.add("upi");
		l.add("kit");
		Assert.assertEquals(l,HqlUtils.removeDuplicateWithOrder(list));
		
	}

	@Test
	public void testGetRandomMax100() {
		int a=HqlUtils.getRandomMax100();
		boolean b=(a>0&&a<100);
		Assert.assertTrue(b);
	}

	@Test
	public void testGetRandom() {
		long a=HqlUtils.getRandom(1, 50);
		boolean b=(a>1&&a<50);
		Assert.assertTrue(b);
	}
}
