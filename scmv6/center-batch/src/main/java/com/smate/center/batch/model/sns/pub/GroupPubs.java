package com.smate.center.batch.model.sns.pub;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组成果关系表.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "GROUP_PUBS")
public class GroupPubs implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2800243644858088442L;
  // 群组成果ID
  private Long groupPubsId;
  // 加密群组成果ID
  private String des3GroupPubsId;

  // 群组ID
  private Long groupId;

  // 加密群组ID
  private String des3GroupId;
  // 成果ID
  private Long pubId;
  // 成果的拥有者.
  private Long ownerPsnId;
  // 作者
  private String authorNames;
  // 来源
  private String briefDesc;
  // 出版年份
  private Integer publishYear;
  // 出版月份
  private Integer publishMonth;
  // 出版日期
  private Integer publishDay;
  // 引用次数
  private Integer citedTimes;
  // 文章所属的期刊的影响因子
  private String impactFactors;
  // 中文标题
  private String zhTitle;
  // 外文标题
  private String enTitle;
  // 成果所在节点
  private Integer nodeId;

  // 成果类型 const_pub_type
  private Integer typeId;
  // 成果加密ID
  private String des3Id;

  // 类别名称
  private String typeName;

  // 查询表格显示用,该字段标记为@Transient
  private String htmlAbstract;

  // 全文，fulltext_fileid为空，则取fulltext_url
  private String fulltextFileid;
  // 全文附件后缀
  private String fulltextExt;
  // 全文
  private String fulltextUrl;
  // 引用情况，用逗号分隔(如：SCI,EI)
  private String citedList;

  // 群组文件夹ID，多个用逗号隔开
  private String groupFolderIds;
  // 引用URL
  private String citedUrl;
  // 引用次数更新时间
  private Date citedDate;
  // 引用情况
  private String citedInfo;
  // 全文附件NodeId
  private Integer fullTextNodeId;
  private Integer sourceDbId;

  private Integer listEi = 0;
  private Integer listSci = 0;
  private Integer listIstp = 0;
  private Integer listSsci = 0;
  private int groupPubsIsShowComment = 0;// "评论"是否要显示链接
  private Date createDate = new Date();// 成果添加到群组的时间
  private Date updateDate;
  private Integer labeled;
  private Integer relevance;

  public GroupPubs() {
    super();
  }

  public GroupPubs(Long pubId, Integer nodeId) {
    super();
    this.pubId = pubId;
    this.nodeId = nodeId;
  }

  @Id
  @Column(name = "GROUP_PUBS_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_PUBS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getGroupPubsId() {
    return groupPubsId;
  }

  @Column(name = "CITED_URL")
  public String getCitedUrl() {
    return citedUrl;
  }

  @Column(name = "CITED_DATE")
  public Date getCitedDate() {
    return citedDate;
  }

  @Transient
  public String getCitedInfo() {
    return citedInfo;
  }

  public void setCitedInfo(String citedInfo) {
    this.citedInfo = citedInfo;
  }

  public void setCitedUrl(String citedUrl) {
    this.citedUrl = citedUrl;
  }

  public void setCitedDate(Date citedDate) {
    this.citedDate = citedDate;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorNames() {
    return authorNames;
  }

  @Column(name = "BRIEF_DESC")
  public String getBriefDesc() {
    return briefDesc;
  }

  @Column(name = "PUBLISH_YEAR")
  public Integer getPublishYear() {
    return publishYear;
  }

  @Column(name = "PUBLISH_MONTH")
  public Integer getPublishMonth() {
    return publishMonth;
  }

  @Column(name = "PUBLISH_DAY")
  public Integer getPublishDay() {
    return publishDay;
  }

  @Column(name = "CITED_TIMES")
  public Integer getCitedTimes() {
    return citedTimes;
  }

  @Column(name = "IMPACT_FACTORS")
  public String getImpactFactors() {
    return impactFactors;
  }

  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  public void setGroupPubsId(Long groupPubsId) {
    this.groupPubsId = groupPubsId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Transient
  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    if (this.pubId == null && StringUtils.isNotBlank(des3Id)) {
      this.pubId = Long.valueOf(ServiceUtil.decodeFromDes3(des3Id));
    }
    this.des3Id = des3Id;
  }

  @Transient
  public String getDes3GroupId() {
    if (this.groupId != null && des3GroupId == null) {
      des3GroupId = ServiceUtil.encodeToDes3(this.groupId.toString());
    }
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    if (this.groupId == null && StringUtils.isNotBlank(des3GroupId)) {
      this.groupId = Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId));
    }
    this.des3GroupId = des3GroupId;
  }

  @Column(name = "PUB_TYPE")
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  @Column(name = "FULLTEXT_FILEID")
  public String getFulltextFileid() {
    return fulltextFileid;
  }

  @Column(name = "FULLTEXT_URL")
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  @Column(name = "FULLTEXT_FILEEXT")
  public String getFulltextExt() {
    return fulltextExt;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  public void setFulltextFileid(String fulltextFileid) {
    this.fulltextFileid = fulltextFileid;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  @Transient
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  @Transient
  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  public void setHtmlAbstract(String htmlAbstract) {
    this.htmlAbstract = htmlAbstract;
  }

  @Column(name = "CITED_LIST")
  public String getCitedList() {
    return citedList;
  }

  public void setCitedList(String citedList) {
    this.citedList = citedList;
  }

  @Column(name = "GROUP_FOLDER_IDS")
  public String getGroupFolderIds() {
    return groupFolderIds;
  }

  public void setGroupFolderIds(String groupFolderIds) {
    this.groupFolderIds = groupFolderIds;
  }

  @Column(name = "SOURCE_DB_ID")
  public Integer getSourceDbId() {
    return sourceDbId;
  }

  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  @Transient
  public String getDes3GroupPubsId() {
    if (this.groupPubsId != null && des3GroupPubsId == null) {
      des3GroupPubsId = ServiceUtil.encodeToDes3(this.groupPubsId.toString());
    }
    return des3GroupPubsId;
  }

  public void setDes3GroupPubsId(String des3GroupPubsId) {
    if (this.groupPubsId == null && StringUtils.isNotBlank(des3GroupPubsId)) {
      this.groupPubsId = Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupPubsId));
    }
    this.des3GroupPubsId = des3GroupPubsId;
  }

  @Column(name = "FULLTEXT_NODEID")
  public Integer getFullTextNodeId() {
    return fullTextNodeId;
  }

  public void setFullTextNodeId(Integer fullTextNodeId) {
    this.fullTextNodeId = fullTextNodeId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "LIST_EI")
  public Integer getListEi() {
    return listEi;
  }

  @Column(name = "LIST_SCI")
  public Integer getListSci() {
    return listSci;
  }

  @Column(name = "LIST_ISTP")
  public Integer getListIstp() {
    return listIstp;
  }

  @Column(name = "LIST_SSCI")
  public Integer getListSsci() {
    return listSsci;
  }

  public void setListEi(Integer listEi) {
    this.listEi = listEi;
  }

  public void setListSci(Integer listSci) {
    this.listSci = listSci;
  }

  public void setListIstp(Integer listIstp) {
    this.listIstp = listIstp;
  }

  public void setListSsci(Integer listSsci) {
    this.listSsci = listSsci;
  }

  @Transient
  public int getGroupPubsIsShowComment() {
    return groupPubsIsShowComment;
  }

  public void setGroupPubsIsShowComment(int groupPubsIsShowComment) {
    this.groupPubsIsShowComment = groupPubsIsShowComment;
  }

  @Column(name = "LABELED")
  public Integer getLabeled() {
    return labeled;
  }

  public void setLabeled(Integer labeled) {
    this.labeled = labeled;
  }

  @Column(name = "RELEVANCE")
  public Integer getRelevance() {
    return relevance;
  }

  public void setRelevance(Integer relevance) {
    this.relevance = relevance;
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

}
