package com.smate.batchtask.test.service;

import java.util.List;

import com.smate.batchtask.test.model.TaskMessageList;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface TestService{
	
	public TaskMessageList getTaskMessageList() throws BatchTaskException;
	
	public void updateStatus(Long msgId) throws BatchTaskException;
	
	public List<TaskMessageList> getAllMsgStatus1() throws BatchTaskException;
}