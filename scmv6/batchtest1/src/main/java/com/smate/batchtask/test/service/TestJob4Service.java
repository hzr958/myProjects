package com.smate.batchtask.test.service;


import com.smate.batchtask.test.model.TestJob4Info;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface TestJob4Service{
	
	public void saveTaskMessage(TestJob4Info testJob4Info) throws BatchTaskException;
	
	
}