package com.smate.batchtask.test.service;


import com.smate.batchtask.test.model.TestJob3Info;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface TestJob3Service{
	
	public void saveTaskMessage(TestJob3Info testJob3Info) throws BatchTaskException;
	
	
}