package com.smate.center.searcher.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.AnalysisPhase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.TokenInfo;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;


/**
 * solr分词工具类
 * 
 * @author zk
 *
 */
public class SolrServiceUtils {
	private static Logger log = Logger.getLogger(SolrServiceUtils.class);

	private static HttpSolrServer solrServer;

	public SolrServiceUtils (String solrServerUrl){
		solrServer = new HttpSolrServer(solrServerUrl);
		solrServer.setConnectionTimeout(5000);			 
	}

	 
	/**
	 *  
	 * @param  analysisType:text_cn_ca_simple text_cn_simple等
	 * @param  sentence 搜索的句子     
	 * @return 分词结果
	 */
	public  List<String> getAnalysis(String analysisType,String sentence) {
	
		FieldAnalysisRequest request = new FieldAnalysisRequest("/analysis/field");
		request.addFieldType(analysisType);
		request.setFieldValue("");
		request.setQuery(sentence);		
	 
		List<String> results = new ArrayList<String>();
		FieldAnalysisResponse response = null; 
		try { 
		    response = request.process(solrServer); 
		   
		  } catch (Exception e) {
		      log.error("连接solrServer response失败", e);			    
		 }		
		  Iterator<AnalysisPhase> it =response.getFieldTypeAnalysis(analysisType).getQueryPhases().iterator(); 
		  while(it.hasNext()) {
			  AnalysisPhase pharse = (AnalysisPhase) it.next(); 			 
			  List<TokenInfo> list = pharse.getTokens();
		  for (TokenInfo info : list) {
			  results.add(info.getText()); 
		   }		  
		 }		 
		  return results;		
	}

}
