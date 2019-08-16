package com.smate.center.job.business.pdwhpub.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 基准库成果
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUBLICATION")
public class PdwhPublication implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3161667418999146744L;
	private Long pubId;
	private Integer dbId;
	private String zhTitle;
	private String enTitle;
	private String authorName;
	private String authorNameSpec;
	private String zhBriefDesc;
	private String enBriefDesc;
	private String zhKeywords;
	private String enKeywords;
	private Long jnlId;
	private Integer pubType;
	private Integer pubYear;
	private String organization;
	private String isbn;
	private String issn;
	private String issue;
	private String volume;
	private String startPage;
	private String endPage;
	private String articleNo;
	private String patentNo;
	private String patentOpenNo;
	private String confName;
	private Long createPsnId;
	private Long updatePsnId;
	private Date createDate;
	private Date updateDate;
	private String doi;
	private String sourceId;
	private Long unionHashValue; // 中英文，出版年份，成果类型的hash值； 用于插入数据表时作为临时唯一索引
	private Long titleHashValue; // 中英文标题hash值
	private Long enTitleHash;
	private Long zhTitleHash;
	private Long doiHash;
	private Long sourceIdHash;
	private Long unitHash;
	private Long patentNoHash;
	private Long patentOpenNoHash;
	private String xmlString; // 导入xml文件
	private String enAbstract;
	private String zhAbstract;
	private Integer patentCategory;
	private String fundInfo;// 成果项目资助信息
	@Column(name = "UPDATE_MARK")
	private Integer updateMark;// 成果添加方式 1=在线导入未修改；2=在线导入 已修改；3=手工导入

	public PdwhPublication() {
		super();
	}

	public PdwhPublication(Long pubId, Integer dbid) {
		super();
		this.pubId = pubId;
		this.dbId = dbid;

	}

	public PdwhPublication(Long pubId, String authorName, String authorNameSpec, String zhKeywords, String enKeywords) {
		super();
		this.pubId = pubId;
		this.authorName = authorName;
		this.authorNameSpec = authorNameSpec;
		this.zhKeywords = zhKeywords;
		this.enKeywords = enKeywords;
	}

	public PdwhPublication(Long pubId, String zhTitle, String enTitle, Integer pubType, Integer pubYear) {
		super();
		this.pubId = pubId;
		this.zhTitle = zhTitle;
		this.enTitle = enTitle;
		this.pubYear = pubYear;
		this.pubType = pubType;
	}

	public PdwhPublication(Long pubId, String authorName, String authorNameSpec, String organization) {
		super();
		this.pubId = pubId;
		this.authorName = authorName;
		this.authorNameSpec = authorNameSpec;
		this.organization = organization;
	}

	@Id
	@Column(name = "PUB_ID")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUBLICATION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	public Long getPubId() {
		return pubId;
	}

	public void setPubId(Long pubId) {
		this.pubId = pubId;
	}

	@Column(name = "DB_ID")
	public Integer getDbId() {
		return dbId;
	}

	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}

	@Column(name = "ZH_TITLE")
	public String getZhTitle() {
		return zhTitle;
	}

	public void setZhTitle(String zhTitle) {
		this.zhTitle = zhTitle;
	}

	@Column(name = "EN_TITLE")
	public String getEnTitle() {
		return enTitle;
	}

	public void setEnTitle(String enTitle) {
		this.enTitle = enTitle;
	}

	@Column(name = "AUTHOR_NAME")
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	@Column(name = "AUTHOR_NAME_SPEC")
	public String getAuthorNameSpec() {
		return authorNameSpec;
	}

	public void setAuthorNameSpec(String authorNameSpec) {
		this.authorNameSpec = authorNameSpec;
	}

	@Column(name = "ZH_BRIEF_DESC")
	public String getZhBriefDesc() {
		return zhBriefDesc;
	}

	public void setZhBriefDesc(String zhBriefDesc) {
		this.zhBriefDesc = zhBriefDesc;
	}

	@Column(name = "EN_BRIEF_DESC")
	public String getEnBriefDesc() {
		return enBriefDesc;
	}

	public void setEnBriefDesc(String enBriefDesc) {
		this.enBriefDesc = enBriefDesc;
	}

	@Column(name = "ZH_KEYWORDS")
	public String getZhKeywords() {
		return zhKeywords;
	}

	public void setZhKeywords(String zhKeywords) {
		this.zhKeywords = zhKeywords;
	}

	@Column(name = "EN_KEYWORDS")
	public String getEnKeywords() {
		return enKeywords;
	}

	public void setEnKeywords(String enKeywords) {
		this.enKeywords = enKeywords;
	}

	@Column(name = "JNL_ID")
	public Long getJnlId() {
		return jnlId;
	}

	public void setJnlId(Long jnlId) {
		this.jnlId = jnlId;
	}

	@Column(name = "PUB_TYPE")
	public Integer getPubType() {
		return pubType;
	}

	public void setPubType(Integer pubType) {
		this.pubType = pubType;
	}

	@Column(name = "PUB_YEAR")
	public Integer getPubYear() {
		return pubYear;
	}

	public void setPubYear(Integer pubYear) {
		this.pubYear = pubYear;
	}

	@Column(name = "ISBN")
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@Column(name = "ISSN")
	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	@Column(name = "ISSUE")
	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	@Column(name = "VOLUME")
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	@Column(name = "START_PAGE")
	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	@Column(name = "END_PAGE")
	public String getEndPage() {
		return endPage;
	}

	public void setEndPage(String endPage) {
		this.endPage = endPage;
	}

	@Column(name = "ARTICLE_NO")
	public String getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(String articleNo) {
		this.articleNo = articleNo;
	}

	@Column(name = "PATENT_NO")
	public String getPatentNo() {
		return patentNo;
	}

	public void setPatentNo(String patentNo) {
		this.patentNo = patentNo;
	}

	@Column(name = "PATENT_OPEN_NO")
	public String getPatentOpenNo() {
		return patentOpenNo;
	}

	public void setPatentOpenNo(String patentOpenNo) {
		this.patentOpenNo = patentOpenNo;
	}

	@Column(name = "CONF_NAME")
	public String getConfName() {
		return confName;
	}

	public void setConfName(String confName) {
		this.confName = confName;
	}

	@Column(name = "CREATE_PSN_ID")
	public Long getCreatePsnId() {
		return createPsnId;
	}

	public void setCreatePsnId(Long createPsnId) {
		this.createPsnId = createPsnId;
	}

	@Column(name = "UPDATE_PSN_ID")
	public Long getUpdatePsnId() {
		return updatePsnId;
	}

	public void setUpdatePsnId(Long updatePsnId) {
		this.updatePsnId = updatePsnId;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Transient
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Transient
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Transient
	public Long getUnionHashValue() {
		return unionHashValue;
	}

	public void setUnionHashValue(Long unionHashValue) {
		this.unionHashValue = unionHashValue;
	}

	@Transient
	public Long getTitleHashValue() {
		return titleHashValue;
	}

	public void setTitleHashValue(Long titleHashValue) {
		this.titleHashValue = titleHashValue;
	}

	@Transient
	public Long getEnTitleHash() {
		return enTitleHash;
	}

	public void setEnTitleHash(Long enTitleHash) {
		this.enTitleHash = enTitleHash;
	}

	@Transient
	public Long getZhTitleHash() {
		return zhTitleHash;
	}

	public void setZhTitleHash(Long zhTitleHash) {
		this.zhTitleHash = zhTitleHash;
	}

	@Transient
	public Long getDoiHash() {
		return doiHash;
	}

	public void setDoiHash(Long doiHash) {
		this.doiHash = doiHash;
	}

	@Transient
	public Long getSourceIdHash() {
		return sourceIdHash;
	}

	public void setSourceIdHash(Long sourceIdHash) {
		this.sourceIdHash = sourceIdHash;
	}

	@Transient
	public Long getUnitHash() {
		return unitHash;
	}

	public void setUnitHash(Long unitHash) {
		this.unitHash = unitHash;
	}

	@Transient
	public Long getPatentNoHash() {
		return patentNoHash;
	}

	public void setPatentNoHash(Long patentNoHash) {
		this.patentNoHash = patentNoHash;
	}

	@Transient
	public Long getPatentOpenNoHash() {
		return patentOpenNoHash;
	}

	public void setPatentOpenNoHash(Long patentOpenNoHash) {
		this.patentOpenNoHash = patentOpenNoHash;
	}

	@Transient
	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	@Transient
	public String getEnAbstract() {
		return enAbstract;
	}

	public void setEnAbstract(String enAbstract) {
		this.enAbstract = enAbstract;
	}

	@Transient
	public String getZhAbstract() {
		return zhAbstract;
	}

	public void setZhAbstract(String zhAbstract) {
		this.zhAbstract = zhAbstract;
	}

	@Transient
	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}

	@Transient
	public Integer getPatentCategory() {
		return patentCategory;
	}

	public void setPatentCategory(Integer patentCategory) {
		this.patentCategory = patentCategory;
	}

	@Transient
	public String getFundInfo() {
		return fundInfo;
	}

	public void setFundInfo(String fundInfo) {
		this.fundInfo = fundInfo;
	}

	public PdwhPublication(Long pubId, String zhTitle, String enTitle) {
		super();
		this.pubId = pubId;
		this.zhTitle = zhTitle;
		this.enTitle = enTitle;
	}

	public Integer getUpdateMark() {
		return updateMark;
	}

	public void setUpdateMark(Integer updateMark) {
		this.updateMark = updateMark;
	}

}
