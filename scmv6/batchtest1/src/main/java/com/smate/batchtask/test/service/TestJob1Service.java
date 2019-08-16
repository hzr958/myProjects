package com.smate.batchtask.test.service;


import com.smate.batchtask.test.model.TestJob1Info;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface TestJob1Service{
	
	public void saveTaskMessage(TestJob1Info testJob1Info) throws BatchTaskException;
	
}