package com.smate.center.searcher.entity;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Solr 基本实体类，所有Document集成此基本类
 * 
 * @author 斌少
 * 
 */
public class SolrEntity implements Serializable {

	private static final long serialVersionUID = -1119308514712063004L;
    
	/****
	 * 执行环境
	 * ****/
	@Field
	private String env;
    
	/***
	 * 业务类型
	 * **/
	@Field
	private String businessType;	//name_search(full_date中定义)
    
	@Field
	private String _version_;	//索引更新版本（自动生成）

	public SolrEntity() {
		this.env = System.getProperty("spring.profiles.active", "run");
	}

	public String getType() {
		return env;
	}

	public void setType(String env) {
	}

	public String getBusinessType() {
		return businessType;
	}

	/**
	 * 系统自动赋值
	 * 
	 * @param businessType
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	 

	public String get_version_() {
		return _version_;
	}

	public void set_version_(String _version_) {
		this._version_ = _version_;
	}
}
