package com.smate.batchtask.test.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.batchtask.test.service.JobLaunchSingleThreadService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class JobSingleThreadExecute{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	JobLaunchSingleThreadService jobLaunchSingleThreadService;
	
	public void run() throws BatchTaskException{
		logger.debug("===========================================BatchSingleThreadTask开始运行");
		if(isRun() == false){
			logger.debug("===========================================BatchSingleThreadTask开关关闭");
			return;
		}else{
			try{
				
				doRun();
				
			}catch(BatchTaskException e){
				logger.error("BatchSingleThreadTask运行异常", e);
				throw new BatchTaskException(e);
			}
		}
	}
	
	public void doRun() throws BatchTaskException{
		try{
			jobLaunchSingleThreadService.JobLauchSingleThread();
			
		}catch(Exception e){
			logger.error("BatchSingleThreadTask运行异常", e);
		}
		
	}
	
	public boolean isRun() throws BatchTaskException{
		//TODO 动态控制逻辑
		return true;
	}
}