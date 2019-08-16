package com.smate.batchtask.test.service;


import com.smate.batchtask.test.model.TestJob5Info;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface TestJob5Service{
	
	public void saveTaskMessage(TestJob5Info testJob5Info) throws BatchTaskException;
	
	
}