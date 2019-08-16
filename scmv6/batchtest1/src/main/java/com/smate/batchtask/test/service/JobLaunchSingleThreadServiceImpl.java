package com.smate.batchtask.test.service;

import java.util.Date;

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

@Service("jobLaunchSingleThreadService")
public class JobLaunchSingleThreadServiceImpl implements JobLaunchSingleThreadService{
	@Autowired
	JobLauncher jobLauncherSingleThread;
	
	@Autowired
	Job testJobStep1;
	
	public void JobLauchSingleThread(){

	try{
			//为jobInstance添加创建时间属性
			Date time = new Date();
			JobParametersBuilder builder = new JobParametersBuilder();
			JobParameters jobParameters = new JobParameters();
			builder.addDate("job_create_time", time);
			jobParameters = builder.toJobParameters();
			
			JobExecution result = jobLauncherSingleThread.run(testJobStep1, jobParameters);
			System.out.println(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}