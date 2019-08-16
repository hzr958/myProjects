package com.smate.batchtask.test.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.batchtask.test.model.TaskMessageList;

@Service("JobLaunchMultiThreadService")
public class JobLaunchMultiThreadServiceImpl implements JobLaunchMultiThreadService{
	
	@Autowired
	private JobLauncher jobLauncherMultiThread;
	
	@Autowired
	TaskMarkerService taskMarkerService;
	
	@Autowired
	TestService testService;
	
	@Autowired
	private Job testJobStep2;
	
	public void JobLauchMultiThread(){

	try{	
		
		List<TaskMessageList> list =new ArrayList<TaskMessageList>();
		list = testService.getAllMsgStatus1();
		
		

			//为jobInstance添加创建时间属性
		for(TaskMessageList itemGet : list){
			
			
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
			
			
			Date time = new Date();
			JobParametersBuilder builder = new JobParametersBuilder();
			JobParameters jobParameters = new JobParameters();
			builder.addDate("job_create_time", time);
			builder.addString("jobCategory", jobCategory);
			builder.addLong("msgId", msgId);
			jobParameters = builder.toJobParameters();
			
			JobExecution result = jobLauncherMultiThread.run(testJobStep2, jobParameters);//.run(job, new JobParameters());
			System.out.println(result.toString());
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}