package com.smate.batchtask.test.jobdetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.scope.context.JobContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.smate.batchtask.test.model.TaskMessageList;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

public class TestWriter implements ItemWriter<String>{
	//private static ThreadLocal<JobExecution> jobExecution = new ThreadLocal<JobExecution>();
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/*@BeforeStep
	public void beforeStep(StepExecution stepExecution){
		
		JobExecution jobExecutionCurrent = stepExecution.getJobExecution();
		jobExecution.set(jobExecutionCurrent);
	}*/
	
	@Override
	public void write(List<? extends String> items)
			throws BatchTaskException, JSONException {
		
		logger.error("batchtask，准备进入decider");
		/*
		TaskMessageList itemGet = new TaskMessageList();
		if(CollectionUtils.isNotEmpty(items)){
			itemGet = items.get(0);
		}
		
		ExecutionContext jobContext = jobExecution.get().getExecutionContext();
		
		String jobCategory = null; 
		
		Map toMap = new HashMap();
		
		Long msgId = itemGet.getMessageId();
		String msgContent = itemGet.getMessageDetails();
		JSONObject jsonObject = new JSONObject(msgContent);
			
			
		Iterator iterator = jsonObject.keys();
			
		while(iterator.hasNext()){
			String key = (String) iterator.next();
			String value = jsonObject.getString(key);
			toMap.put(key, value);			
		    toMap.put("MessageId", msgId);
		}
		
		// 获取job类型写入jobContext中，供decider使用.如果job任务种类为空，则打印T表中任务id
		
		jobCategory = (String) toMap.get("jobCategory");
		if(StringUtils.isNotBlank(jobCategory)){
			jobContext.putString("jobCategory", jobCategory);
			jobContext.putLong("msgId", msgId);
		}else{
			logger.error("SpringBatch 获取任务名为空！任务消息8位Id = ", msgId);
			return;
		}
	*/}
		
}
