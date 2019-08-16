package com.smate.core.base.utils.string;

import com.smate.core.base.utils.string.IrisStringUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

public class IrisStringUtilsTest extends TestCase {
	public IrisStringUtilsTest(String method){
		super(method);
	}
	IrisStringUtils irisstringutils=new IrisStringUtils();
	public void testsubstring() throws Exception{
		String s=irisstringutils.bSubstring("<美12345丽@89%>", 5);
	    Assert.assertEquals("测试substring取字符", "<美12",s);
	}
	public void testfilterSupplementaryChars(){
		String s1=irisstringutils.filterSupplementaryChars("123455&煤炉");
		Assert.assertEquals(getName(), "123455&煤炉", s1);
	}
	public void testfilterSupplementaryCharsChina(){
		String s2=irisstringutils.filterSupplementaryCharsChina("美丽&的西双版纳");
		Assert.assertEquals("美丽&amp;的西双版纳", s2);
	}
	public void testfilterIllegalXmlChar(){
		String s3=irisstringutils.filterIllegalXmlChar("n <\t \b 1  \n  yo urehi @fheui/>");
		System.out.println(s3);
		Assert.assertEquals("n <  1    yo urehi @fheui/>", s3);
	}
	public void testfilterXmlStr(){
		String s4=irisstringutils.filterXmlStr("<12 \t 3 4 5>");
		Assert.assertEquals("<12  3 4 5>", s4);
		
	}
	public void testfull2Half(){
		String s5=irisstringutils.full2Half("ａｆｄｆｄｆｄ美丽得南非");
		Assert.assertEquals("afdfdfd美丽得南非", s5);
	}
	public void testhasChineseWord(){
		boolean s6=irisstringutils.hasChineseWord("mei五分li符号efh");
		Assert.assertEquals(true, s6);
	}
	public void testisChineseChar(){
	boolean s7=irisstringutils.isChineseChar('。');
	Assert.assertEquals(true, s7);
	
	
	}
	
	

}
