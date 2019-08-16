package com.smate.batchtask.test.jobdetail;

import java.util.Date;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.batchtask.test.model.TestJob1Info;
import com.smate.batchtask.test.model.TestJob5Info;
import com.smate.batchtask.test.service.TestJob1Service;
import com.smate.batchtask.test.service.TestJob5Service;
import com.smate.batchtask.test.service.TestService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class TestJob5Writer implements ItemWriter<Long>{

	@Autowired
	TestJob5Service testJob5Service;
	
	@Autowired
	TestService testService;
	
	private JobExecution jobExecution;
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution){
		
		jobExecution = stepExecution.getJobExecution();
	}
	
	@Override
	public void write(List<? extends Long> items) throws BatchTaskException {
		Thread current = Thread.currentThread();
		Long msgId = items.get(0);
		msgId = jobExecution.getJobParameters().getLong("msgId");
		
		System.out.println("任务msgId = "+msgId+ ", Thread Name: " +current.getName()+", Thread Id: "+current.getId()+", Thread Group: "+current.getThreadGroup()+", Thread priority: "+current.getPriority());
		TestJob5Info single = new TestJob5Info();
		String threadInfo = "Thread Name: " +current.getName()+", Thread Id: "+current.getId()+", Thread Group: "+current.getThreadGroup()+", Thread priority: "+current.getPriority();
		
		single.setMsgId(msgId);
		single.setThreadInfo(threadInfo);
		single.setCreateTime(new Date());
		
		testJob5Service.saveTaskMessage(single);
		
		//状态置为4，已经完成处理，需要在以后直接删除
		testService.updateStatus(msgId);
	}
	
}