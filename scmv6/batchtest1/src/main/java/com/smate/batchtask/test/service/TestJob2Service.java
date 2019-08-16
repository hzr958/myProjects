package com.smate.batchtask.test.service;


import com.smate.batchtask.test.model.TestJob2Info;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface TestJob2Service{
	
	public void saveTaskMessage(TestJob2Info testJob2Info) throws BatchTaskException;
	
	
}