package com.smate.core.base.utils.string;

import com.smate.core.base.utils.string.JnlFormateUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

public class JnlFormateUtilsTest extends TestCase {
	public JnlFormateUtilsTest(String method){
		super(method);
	}
	JnlFormateUtils jnlformateutils=new JnlFormateUtils();
	public void testgetStrAlias(){
		String s1=jnlformateutils.getStrAlias("aΓΔ\n");
		System.out.println("s"+s1);
		Assert.assertEquals("agammadelta", s1);
	}
	public void testisChinesechar(){
		boolean s2=jnlformateutils.isChinese('?');
		Assert.assertEquals(false, s2);
	}
	public void testisChinesestring(){
		boolean s3=jnlformateutils.isChinese("你好吗？");
		Assert.assertEquals(true, s3);		
	}
   public void testgetLocalStr(){
	   String  s4=jnlformateutils.getLocalStr("a真的B你是", false);
	   Assert.assertEquals("ab", s4);
   }
   public void testgetSubStr(){
	   String s5=jnlformateutils.getSubStr("y a(BΩ)");
	   System.out.println("i am"+s5);
	   Assert.assertEquals("ya%", s5);
   }	   
}
