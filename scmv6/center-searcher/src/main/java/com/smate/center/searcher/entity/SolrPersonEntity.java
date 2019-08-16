package com.smate.center.searcher.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

import com.smate.center.searcher.entity.SolrEntity;

/**
 * 人员实体类(姓名、单位)
 * 
 * @author zk
 *
 */
public final class SolrPersonEntity extends SolrEntity{

	/**  field mean
     *   psn_id ns_psn_id ,
     *   lower(replace(name,' ','')) ns_zh_name,
     *   lower(replace(first_name||last_name,' ','')) ns_en_name1,
     *   lower(replace(last_name||first_name,' ','')) ns_en_name2,
     *   lower(replace(ins_name,' ','')) ns_ins_name,
     *   lower(name||' '||first_name||' '||last_name) ns_name_field,
     *   lower(ins_name) ns_ins_name_field,
     *   'test' env,'ns_test_'||psn_id id,
     *   'name_search' businessType
	 */
	private static final long serialVersionUID = 8864501879599449349L;
	@Id
	@Field
	private String id;			 	//记录主键(需要在查询语句中指定值)
	
	@Field
	private String ns_psn_id;		//人员id
	@Field
	private String ns_zh_name;		//中文姓名 lower(replace(name,' ',''))
	@Field	
	private String ns_en_name1;		//英文姓名 lower(replace(first_name||last_name,' ',''))
	@Field
	private String ns_en_name2;		//英文姓名 lower(replace(last_name||first_name,' ',''))
	@Field
	private String ns_ins_name;		//单位名称 lower(replace(ins_name,' ',''))
	@Field
	private String ns_name_field;	//姓名  lower(name||' '||first_name||' '||last_name)
	@Field
	private String ns_ins_name_field; //lower(ins_name)
	  
    public SolrPersonEntity(){
	   
    }



	public String getNs_psn_id() {
		return ns_psn_id;
	}

	public void setNs_psn_id(String ns_psn_id) {
		this.ns_psn_id = ns_psn_id;
	}

	 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
 
	 
	public String getNs_zh_name() {
		return ns_zh_name;
	}

	public void setNs_zh_name(String ns_zh_name) {
		this.ns_zh_name = ns_zh_name;
	}

 

	public String getNs_ins_name() {
		return ns_ins_name;
	}

	public void setNs_ins_name(String ns_ins_name) {
		this.ns_ins_name = ns_ins_name;
	}

	public String getNs_en_name1() {
		return ns_en_name1;
	}

	public void setNs_en_name1(String ns_en_name1) {
		this.ns_en_name1 = ns_en_name1;
	}

	public String getNs_en_name2() {
		return ns_en_name2;
	}

	public void setNs_en_name2(String ns_en_name2) {
		this.ns_en_name2 = ns_en_name2;
	}

	public String getNs_name_field() {
		return ns_name_field;
	}

	public void setNs_name_field(String ns_name_field) {
		this.ns_name_field = ns_name_field;
	}

	public String getNs_ins_name_field() {
		return ns_ins_name_field;
	}

	public void setNs_ins_name_field(String ns_ins_name_field) {
		this.ns_ins_name_field = ns_ins_name_field;
	}
	 
}
