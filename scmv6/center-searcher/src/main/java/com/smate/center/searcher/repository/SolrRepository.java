package com.smate.center.searcher.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;
import org.springframework.stereotype.Repository;

import com.smate.center.searcher.entity.SolrPersonEntity;
import com.smate.center.searcher.service.SolrServiceUtils;
import com.smate.center.searcher.util.SolrPage;


/**
 * Solr 对接公共类(数据层)
 * 
 * @author 斌少
 * 
 */
@Repository
public class SolrRepository<T, ID extends Serializable> extends SimpleSolrRepository<T, ID> {
	 
	private String solrServerUrl;
	
	@Value("${runenv}")
    private String runenv;
	
	public static enum LinkType {
		AND, OR;
	}
	public String getSolrServerUrl() {
		return solrServerUrl;
	}

	public void setSolrServerUrl(String solrServerUrl) {
		this.solrServerUrl = solrServerUrl;
	}
	
	public void deleteIndexs(Collection<String> ids) {
		getSolrOperations().deleteById(ids);
	}

	public Sort getSortDesc(String field) {
		return new Sort(Sort.Direction.DESC, field);
	}

	public Sort getSortAsc(String field) {
		return new Sort(Sort.Direction.ASC, field);
	}

	public List<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, LinkType type,
			boolean highlight) {
		this.setEntityClass(entityClass);
		SolrResultPage<T> sp = (SolrResultPage<T>) findBusinessAll(businessType, conditions, null, null, type,
				highlight);
		return sp.getContent();
	}

	public List<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Sort sort,
			LinkType type, boolean highlight) {
		this.setEntityClass(entityClass);
		SolrResultPage<T> sp = (SolrResultPage<T>) findBusinessAll(businessType, conditions, null, sort, type,
				highlight);
		return sp.getContent();
	}

	public SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass,
			SolrPage<T> page, LinkType type, boolean highlight) {
		this.setEntityClass(entityClass);
		SolrResultPage<T> sp = (SolrResultPage<T>) findBusinessAll(businessType, conditions, page, null, type,
				highlight);
		page.setResult(sp);
		return page;
	}

	public SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Sort sort,
			SolrPage<T> page, LinkType type, boolean highlight) {
		this.setEntityClass(entityClass);
		SolrResultPage<T> sp = (SolrResultPage<T>) findBusinessAll(businessType, conditions, page, sort, type,
				highlight);
		page.setResult(sp);
		return page;
	}

	public SolrPage<T> findPerson(Map<String, Object> conditions, SolrPage<T> page) {
		@SuppressWarnings("unchecked")
		Class<T> entityClass = (Class<T>) SolrPersonEntity.class;
		this.setEntityClass(entityClass);
		SolrResultPage<T> sp = (SolrResultPage<T>) findPersonAll(conditions, page);
		page.setResult(sp);
		return page;
	}
	
	private Page<T> findBusinessAll(String businessType, Map<String, Object> conditions, SolrPage<T> page, Sort sort,
			LinkType type, boolean highlight) {
		    Pageable pageable = null;		
		    int pageCount = 0;		 
			int pageNo = page.getPageNo() - 1;
			pageNo = (pageNo < 0) ? 0 : pageNo;
			pageable = new PageRequest(pageNo, page.getPageSize());
			Query query = createQuery(businessType, conditions, pageable, null, type);							
			Page<T> pageImpl = new SolrResultPage<T>(Collections.<T> emptyList());
			pageImpl = getSolrOperations().queryForPage(query, getEntityClass());		
            page.setPageCount(pageCount);		
			return pageImpl;			
	}
	private Page<T> findPersonAll(Map<String, Object> conditions, SolrPage<T> page) {
		    Pageable pageable = null;		
		    int pageCount = 0;		 
			int pageNo = page.getPageNo() - 1;
			pageNo = (pageNo < 0) ? 0 : pageNo;
			pageable = new PageRequest(pageNo, page.getPageSize());
			Query query = createPersonQuery(conditions, pageable);							
			Page<T> pageImpl = new SolrResultPage<T>(Collections.<T> emptyList());
			pageImpl = getSolrOperations().queryForPage(query, getEntityClass());		
            page.setPageCount(pageCount);
			return pageImpl;			
	}

	private Query createQuery(String businessType, Map<String, Object> c, Pageable pageable, Sort sort, LinkType type) {
		Criteria conditions = null;	 
		conditions =  createConditions(c, type);		 
		Query query = new SimpleQuery(conditions);
		Criteria fq = new Criteria("env").expression(ObjectUtils.toString(runenv)).and(
					new Criteria("businessType").expression(businessType));
	    query.addFilterQuery(new SimpleQuery(fq));		
		if (pageable != null) {
			query.setPageRequest(pageable);
		}
		if (sort != null) {
			query.addSort(sort);
		}
		return query;
	}
	private Query createPersonQuery( Map<String, Object> c, Pageable pageable) {
		Criteria conditions = null;		 
		conditions =  createNSConditions(c);	 
		Query query = new SimpleQuery(conditions);
		query.setTimeAllowed(10000);				
	    // query.addFilterQuery(new SimpleQuery(fq));		
		if (pageable != null) {
			query.setPageRequest(pageable);
		}		 
		return query;
	}
	private Criteria createConditions(Map<String, Object> c, LinkType type) {
		if (type == null) {
			type = LinkType.OR;
		}
		Criteria conditions = new Criteria();		
		if (MapUtils.isNotEmpty(c)) {			
			for (Map.Entry<String, Object> entry : c.entrySet()) {
				   if (type.equals(LinkType.AND)) {
					   conditions = conditions.and(new Criteria(entry.getKey()).contains(ObjectUtils.toString(entry
							.getValue())));
				   } else if (type.equals(LinkType.OR)) {
					   conditions = conditions.or(new Criteria(entry.getKey()).expression(ObjectUtils.toString(entry
								.getValue())));
				   }
		         
			}
		}
		return conditions;
	}
	
	/**
	 * name search查询条件设置
	 * @param c
	 * @return
	 */
	private Criteria createNSConditions(Map<String, Object> c) {
		SolrServiceUtils  solrServiceUtils  = new SolrServiceUtils(solrServerUrl);  
		//设置
		Criteria conditions = new Criteria("env").expression(ObjectUtils.toString(runenv)).and(
				new Criteria("businessType").expression("name_search"));
		if (MapUtils.isNotEmpty(c)) {			
			for (Map.Entry<String, Object> entry : c.entrySet()) {
				   //字符串过滤
				   String searchKey = stringFilter(ObjectUtils.toString(entry.getValue()));
			       if(StringUtils.isBlank(searchKey)){
			    	   return conditions; 
			       }
			       if(StringUtils.equals("ns_name", entry.getKey())){
			    	   //1完全匹配，使用关键词分词器
			    	   conditions = conditions.or(new Criteria("ns_zh_name").expression(
			    			   StringUtils.replace(searchKey, " ", "")
			    			   )).boost(100.0f);
			    	   conditions = conditions.or(new Criteria("ns_en_name1").expression(
			    			   StringUtils.replace(searchKey, " ", "")
			    			   )).boost(100.0f);
			    	   conditions = conditions.or(new Criteria("ns_en_name2").expression(
			    			   StringUtils.replace(searchKey, " ", "")
			    			   )).boost(100.0f);
			    	   //2使用text_oneword_simple 一元分词
			    	   List<String> strList = solrServiceUtils.getAnalysis("text_oneword_simple",searchKey);
					   for(String str:strList){
							conditions = conditions.or(new Criteria("ns_name_field").expression(str)).boost(4f);
					   }
			       }else if(StringUtils.equals("ns_ins_name", entry.getKey())){
			    	  //1完全匹配，使用关键词分词器
			    	   conditions = conditions.or(new Criteria("ns_ins_name").expression(
			    			   StringUtils.replace(searchKey, " ", "")
			    			   )).boost(60.0f);
			    	   //2将搜索字符串按中文分词器分词
			    	   List<String> strList = solrServiceUtils.getAnalysis("text_cn_simple",searchKey);
					   for(String str:strList){
							conditions = conditions.or(new Criteria("ns_ins_name_field").expression(str)).boost(2f);
					   }
			       }
		  }
		}
		return conditions;
	}
    
	/**
	 * 特殊字符过滤
	 * @param searchStr
	 * @return
	 */
	private String stringFilter(String searchStr){
		 String[] searchList = {"`",",",".","/","'",";","[","]","-","=","*","+","-","~","!","@","#","$","%","^","&"
				 ,"(",")","?",">","<","{","}",":","'","|","\\","，","。","？","｛","｝","【","】","；","‘","“","”","·","～",
				 "！","@","#","￥","%","&","（","）","\"","《","》"};
	     String[] reList =  {"","","","","","","","","","","","","","","","","","","","",""
				 ,"","","","","","","","","","","","","","","","","","","","","","","","",
				 "","","","","","","","","","",""};
	     return  StringUtils.replaceEach(searchStr, searchList, reList);		
	}

}
