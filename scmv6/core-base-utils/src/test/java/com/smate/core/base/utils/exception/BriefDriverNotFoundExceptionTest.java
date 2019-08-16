package com.smate.core.base.utils.exception;



import com.smate.core.base.utils.exception.BriefDriverNotFoundException;

import junit.framework.TestCase;
import junit.framework.Assert;   

public class BriefDriverNotFoundExceptionTest extends TestCase {
	  public BriefDriverNotFoundExceptionTest(String method){
		  super(method);
	  }
	    public void testBriefDriverNotFoundException()   
	    {   
	        
	            
	       try {
			throw new BriefDriverNotFoundException("generateBriefFromImportXml找不到类型typeId={}对应的BriefDriver", 1);
		  } catch (BriefDriverNotFoundException e) {
			
		  }
	      fail("expected IllegalArgumentException for non +ve age");
	      
	    }   
}
