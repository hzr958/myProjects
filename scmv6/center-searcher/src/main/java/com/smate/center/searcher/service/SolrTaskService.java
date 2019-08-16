package com.smate.center.searcher.service;

 

/**
 * solr 更新索引 任务服务类  
 * @author hp
 *
 */
public interface SolrTaskService {
	
	/**
	 * 更新索引
	 * @throws Exception 
	 */
	public void updateIndex() ;
    /**
     * 查看更新索引的
     * @throws Exception 
     * **/
	public String getStatus();
	
 
}
