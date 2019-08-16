package com.smate.core.base.utils.string;

import java.math.BigDecimal;

import com.smate.core.base.utils.string.IrisNumberUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

public class IrisNumberUtilsTest extends TestCase {
	  public IrisNumberUtilsTest(String method){
          super(method);
       }
	IrisNumberUtils irisnumberutils=new IrisNumberUtils();
	public void testcreateInteger(){
		Integer result=IrisNumberUtils.createInteger("3");
		 Assert.assertSame(3, result);
	}
	public void testcreateDouble(){
		Double result1=IrisNumberUtils.createDouble("0.123");
		Assert.assertEquals(0.123, result1);
	}
	public void testcreateFloat(){
		Float result2=IrisNumberUtils.createFloat("0.23");	
		Assert.assertEquals(0.23, result2, 1.0);
	}
	public void testcreateBigDecimal(){
		BigDecimal result3=IrisNumberUtils.createBigDecimal("1345678900");
		Assert.assertEquals(new BigDecimal(1345678900), result3);
	}
	public void testcreateLong(){
		Long result4=IrisNumberUtils.createLong("123");	
		Assert.assertEquals(123,123 , result4);
	}
	public void testmonthDayToInteger(){
		Integer result5= IrisNumberUtils.monthDayToInteger("3");
		Assert.assertEquals((Integer)3, result5);
	}	
	

}
