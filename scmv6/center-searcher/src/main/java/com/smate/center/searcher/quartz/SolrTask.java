package com.smate.center.searcher.quartz;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

 

import com.smate.center.searcher.service.SolrTaskService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 更新solr姓名、单位检索索引任务
 * 
 * @author zk
 *
 */
public class SolrTask{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
		
	@Autowired
	private SolrTaskService solrTaskService;
	
	
	 
	public void run() throws BatchTaskException{
		logger.debug("====================================SolrTask===开始运行");
		if(isRun() == false){
			logger.debug("====================================SolrTask===开关关闭");
			return;
		}else{
			try{		
				doRun();				
			}catch(BatchTaskException e){
				logger.error("SolrTask,运行异常", e);
				throw new BatchTaskException(e);
			}
		}
	}
	
	public void doRun() throws BatchTaskException{
		 try{
			//执行 更新索引
			solrTaskService.updateIndex();
			
			//1分钟 执行一次 直到  更新索引完成   ，  更新索引完成发送邮件到 
			boolean flag=true;
	        while(flag){
	            String status = solrTaskService.getStatus();
	            if(StringUtils.equals("idle", status)){ //idle or busy
	            	flag=false;
	            }
	            Thread.sleep(1000*60*60);				            
	        }			
		}catch(Exception e){
			logger.error("SolrTask,运行异常", e);
		}
		 
	}
	
	public boolean isRun() throws BatchTaskException{
		//任务开关控制逻辑
		return true;
	}
}