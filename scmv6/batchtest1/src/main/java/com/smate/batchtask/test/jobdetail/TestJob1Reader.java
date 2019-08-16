package com.smate.batchtask.test.jobdetail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.smate.core.base.utils.exception.BatchTaskException;

public class TestJob1Reader implements ItemReader<Long>{

	private List<Long> listNull = new ArrayList<Long>();
	protected Logger logger = LoggerFactory.getLogger(getClass());
	

	
	@Override
	public Long read() throws BatchTaskException, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		
		
		if(CollectionUtils.isEmpty(listNull)){
			return null;
		}
		return listNull.remove(0);
	}
	
	
	public TestJob1Reader(){
		listNull.add(99L);
	}
	
}