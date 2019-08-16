package com.smate.center.searcher.service;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.constant.EmailConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;


/**
 * solr 更新索引 任务服务类  
 * @author hp
 *
 */
@Service("solrTaskService")
@Transactional(rollbackFor = Exception.class)
public class SolrTaskServiceImpl implements SolrTaskService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	 
    @Value("${solr.server.url}")
    String solrServerUrl;
   
    @Value("${solr.admin.email}")
    String solrAdminEmail;
    
    @Value("${initEmail.restful.url}")
    private String initEmailUrl;
    @Resource(name="restTemplate")
	private RestTemplate restTemplate;

    @Override
	public void updateIndex()   {   	 
		   executeUrl(solrServerUrl+"/dataimport?command=full-import&commit=y&clean=true");	    	 	
	}

	@Override
	public String getStatus(){
		String status="";
		String xmlString =  executeUrl(solrServerUrl+"/dataimport?command=status");
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			status = document.selectSingleNode("/response/str[@name='status']").getText();
			if(StringUtils.equals("idle", status)){ //idle or busy
				//发送邮件到管理员邮箱
				sendEmail(1);
            }
		} catch (DocumentException e) {
			//发送邮件到管理员邮箱
			sendEmail(0);
			logger.error("====================================SolrTask===姓名索引更新-解析返回的XML信息错误");
		}  	
		
	
		return status;
	}
	
	private String executeUrl(String urlStr){
		StringBuffer bs = new StringBuffer();
		try{
			URL url = new URL(urlStr);
	        HttpURLConnection urlcon = (HttpURLConnection)url.openConnection();
	        urlcon.connect();         //获取连接
	        InputStream is = urlcon.getInputStream();
	        BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
	       
	        String l = null;
	        while((l=buffer.readLine())!=null){
	            bs.append(l).append("\r\n");
	        }
	              
	     }catch(IOException e){
	    	//send email  update index error
	    	 sendEmail(0);
	    	 logger.error("====================================SolrTask===姓名索引更新-连接URL:"+urlStr+" 错误");
	     }
		 return bs.toString(); 
	}
	 
	/****
	 * @param status  0 -error  1 -success
	 * @throws Exception 
	 * 
	 * ***/
	private void sendEmail(int status) {
		Map<String, Object> mailMap = new HashMap<String, Object>();
		if(status==0){
			mailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, "姓名索引更新失败邮件通知");
			mailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, "email_solr_error.ftl");	
		}else{
			mailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, "姓名索引更新成功邮件通知");
			mailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, "email_solr_success.ftl");	
		}		 
		String[] emailArrayStrings =  StringUtils.split(solrAdminEmail,",");
		for(int i=0;i<emailArrayStrings.length;i++){
			mailMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, emailArrayStrings[i]);
			if(restTemplate.postForObject(initEmailUrl,mailMap,Integer.class)==-1){
				logger.error("====================================SolrTask===姓名索引更新邮件通知-数据到邮件服务时出错");				
			}
		}
		
	}
}
