package com.smate.core.base.utils.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.smate.core.base.utils.common.HtmlUtils;

public class HtmlUtilsTest {

	public HtmlUtilsTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToHtml() {
		String str="<head>you are a girl?<head/>/br";
		Assert.assertEquals("&lt;head&gt;you&nbsp;are&nbsp;a&nbsp;girl?&lt;head/&gt;/br", HtmlUtils.toHtml(str));
		
		
	}

	@Test
	public void testReplaceBlank() {
		String str="you are\t a girl\t?";
		Assert.assertEquals("you are  a girl ?",str);
		
	}

	@Test
	public void testHtml2Text() {
		String str="<script> var a='my'</script>sorry for this";
		Assert.assertEquals("sorry for this", HtmlUtils.Html2Text(str));	
	}

	@Test
	public void testSubString() {
		Assert.assertEquals("if you weso delicious",HtmlUtils.subString("if you were a girl 是 吧", 3, "so delicious"));
	    
	}

}
