package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 批量抓取crossref数据临时存储表.
 * 
 * @author zll
 * 
 */
@Entity
@Table(name = "DBCACHE_CFETCH")
public class DbCacheCfetch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1911052680437455107L;

	/**
	 * XML_ID NUMBER(18) N 主键 NUMBER(18) N 单位ID CLOB N 文件数据 NUMBER(1) N 0
	 * 0未处理，1已处理，9异常 VARCHAR2(1000 CHAR) Y 异常消息
	 */

	private Long crossrefId;
	// 文件数据
	private String jsonData;
	// XML导入文件名
	private String fileName;
	// 成果年份
	private Integer pubYear;
	// 0未处理，1已处理，9异常
	private Integer status = 0;
	// 异常消息
	private String errorMsg;

	public DbCacheCfetch() {
		super();
	}

	public DbCacheCfetch(String jsonData, String fileName, Integer pubYear) {
		super();
		this.jsonData = jsonData;
		this.fileName = fileName;
		this.pubYear = pubYear;
		this.status = 0;
	}

	public DbCacheCfetch(String jsonData, String fileName, Integer pubYear, Integer status) {
		super();
		this.fileName = fileName;
		this.pubYear = pubYear;
		this.status = status;
	}

	@Id
	@Column(name = "CROSSREF_ID")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DBCACHE_CFETCH", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	public Long getCrossrefId() {
		return crossrefId;
	}

	public void setCrossrefId(Long crossrefId) {
		this.crossrefId = crossrefId;
	}

	@Column(name = "JSON_DATA")
	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	@Column(name = "ERROR_MSG")
	public String getErrorMsg() {
		return errorMsg;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	@Column(name = "PUB_YEAR")
	public Integer getPubYear() {
		return pubYear;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setPubYear(Integer pubYear) {
		this.pubYear = pubYear;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
