package com.smate.core.base.xml.utils;

import com.smate.core.base.utils.data.XmlUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

public class XmlUtilisTest extends TestCase {
    public XmlUtilisTest(String name) {
        super(name);
    }

    public void testgetLanguageSpecificText() {
        String s = XmlUtil.getLanguageSpecificText("zh", "你是好人", " good person");
        Assert.assertEquals("你是好人", s);
    }

    public void testisEmpty() {
        String text = "  ";
        boolean b = XmlUtil.isEmpty(text);
        Assert.assertEquals(true, b);
    }

    public void testfilterNull() {
        String s = XmlUtil.filterNull(null);
        Assert.assertEquals("", s);

    }

    public void testisSCString() {
        boolean b = XmlUtil.isAttributePath("争议12mei");
        Assert.assertEquals(false, b);
    }

    public void testisNumeric() {
        String str = "12345";
        boolean b = XmlUtil.isNumeric(str);
        Assert.assertEquals(true, b);
    }

    public void testgetCleanAuthorName() {
        String authorname = " ,zhongzi;";
        String a = XmlUtil.getCleanAuthorName(authorname);
        Assert.assertEquals("zhongzi", a);
    }

}
