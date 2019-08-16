package com.smate.web.management.model.grp;

public class SearchPubShowInfo {

	private Long pubId;// 基准库成果id.
	private String des3PubId;// 基准库成果加密id.
	private Integer dbid;
	private String title;
	private String authorNames;
	private String briefDesc;
	private Long publishYear; // 发表年份
	private Long pubType; // 成果类型
	private String patentOpenNu; // 专利号
	private String language; // 详情显示页面语言版本优先
	private String doi;
	private String doiUrl;
	private String fundingInfo; // 基金标注信息
	private String pubUrl;

	public Long getPubId() {
		return pubId;
	}

	public void setPubId(Long pubId) {
		this.pubId = pubId;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthorNames() {
		return authorNames;
	}

	public void setAuthorNames(String authorNames) {
		this.authorNames = authorNames;
	}

	public String getBriefDesc() {
		return briefDesc;
	}

	public void setBriefDesc(String briefDesc) {
		this.briefDesc = briefDesc;
	}

	public Long getPublishYear() {
		return publishYear;
	}

	public void setPublishYear(Long publishYear) {
		this.publishYear = publishYear;
	}

	public Long getPubType() {
		return pubType;
	}

	public void setPubType(Long pubType) {
		this.pubType = pubType;
	}

	public String getDes3PubId() {
		return des3PubId;
	}

	public void setDes3PubId(String des3PubId) {
		this.des3PubId = des3PubId;
	}

	public String getPatentOpenNu() {
		return patentOpenNu;
	}

	public void setPatentOpenNu(String patentOpenNu) {
		this.patentOpenNu = patentOpenNu;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getDoiUrl() {
		return doiUrl;
	}

	public void setDoiUrl(String doiUrl) {
		this.doiUrl = doiUrl;
	}

	public String getFundingInfo() {
		return fundingInfo;
	}

	public void setFundingInfo(String fundingInfo) {
		this.fundingInfo = fundingInfo;
	}

	public String getPubUrl() {
		return pubUrl;
	}

	public void setPubUrl(String pubUrl) {
		this.pubUrl = pubUrl;
	}

}
