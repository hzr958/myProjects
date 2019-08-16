package com.smate.batchtask.test.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.batchtask.test.service.JobLaunchMultiThreadService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class JobMultiThreadExecute{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	JobLaunchMultiThreadService jobLaunchMultiThreadService;
	
	public void run() throws BatchTaskException{
		logger.debug("===========================================BatchMultiTask开始运行动态刷新任务");
		if(isRun() == false){
			logger.debug("===========================================BatchMultiTask开关关闭");
			return;
		}else{
			try{
				
				doRun();
				
			}catch(BatchTaskException e){
				logger.error("BatchMultiTask运行异常", e);
				throw new BatchTaskException(e);
			}
		}
	}
	
	public void doRun() throws BatchTaskException{
		try{
			
			jobLaunchMultiThreadService.JobLauchMultiThread();
			
		}catch(Exception e){
			logger.error("BatchMultiTask运行异常", e);
		}
		
	}
	
	public boolean isRun() throws BatchTaskException{
		//TODO 动态控制逻辑
		return true;
	}
}