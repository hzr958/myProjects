package com.smate.batchtask.test.jobdetail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.batchtask.test.model.TaskMessageList;
import com.smate.batchtask.test.service.TestService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class TestReader implements ItemReader<String>{

	//@Autowired
	//TestService testService;
	//synchronized
	@Override
	public String read() throws BatchTaskException, UnexpectedInputException,
			ParseException, NonTransientResourceException {
/*		TaskMessageList resultList = new TaskMessageList();
		
		resultList = testService.getTaskMessageList();*/
		String item = null;
		
		return item;
	}
	
	
}
		
