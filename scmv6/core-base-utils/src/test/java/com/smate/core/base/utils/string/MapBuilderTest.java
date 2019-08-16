package com.smate.core.base.utils.string;

import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

public class MapBuilderTest extends TestCase {
    public MapBuilderTest(String method){
    	super(method);
    }
    MapBuilder mapbuild= MapBuilder.getInstance();
    public void testgetInstance(){
    	MapBuilder mapbuild1= MapBuilder.getInstance();
    	System.out.println(mapbuild1);
		Assert.assertNotNull(mapbuild1);
    }
    public void testput(){
    	mapbuild.put("key", "value");
    	System.out.println(mapbuild.get("key"));
    	Assert.assertSame("value",mapbuild.get("key"));	
    }
    public void testget(){
    	mapbuild.put("key1", "value1");
    	System.out.println(mapbuild.get("key1"));
    	Assert.assertSame("value1",mapbuild.get("key1"));
    }
    public void testremove(){
    	mapbuild.put("key2","value2");
    	mapbuild.remove("key2");
    	Assert.assertEquals(null, mapbuild.get("key2"));
    }
    public void testgetMap(){
    	mapbuild.put("hello", "hello");
    	Map m=mapbuild.getMap();
    	System.out.println(m);
    	Assert.assertEquals("{hello=hello}", m);//为什么是这样子的呢？
    }
    public void testgetjson(){
    	mapbuild.put("124" , "345");
    	String s=mapbuild.getJson();
    	System.out.println(s);//为什么？
    	Assert.assertEquals("{\"124\":\"345\"}", s);
    }
    public void  testclear(){
    	mapbuild.put("you", "sun of beache");
    	mapbuild.clear();
    	System.out.println(mapbuild);
    	Assert.assertNull(mapbuild);//为红色呢么啊？
    	
    }
    
}
