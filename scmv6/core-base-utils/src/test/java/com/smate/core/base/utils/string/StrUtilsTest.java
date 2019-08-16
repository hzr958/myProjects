package com.smate.core.base.utils.string;

import com.smate.core.base.utils.string.StrUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

public class StrUtilsTest extends TestCase {
	public StrUtilsTest(String method){
	   super(method);
	}
	StrUtils strutils=new StrUtils();
	public void testgetAllFirstLetter()
	{
		String str=strutils.getAllFirstLetter("你好吗种子");
		System.out.println(str);
		Assert.assertEquals("nhmzz", str);
	}
	public void testgetFirstLetter(){
		String str1=strutils.getFirstLetter("你是骗子吗");
		System.out.println(str1);
		Assert.assertEquals("n", str1);
	}
	public  void testtoXmlChar(){
		String str1=strutils.getFirstLetter("&amp;amp;you are a&");
		System.out.println(str1);
		Assert.assertEquals("&", str1);
	}
	

}
