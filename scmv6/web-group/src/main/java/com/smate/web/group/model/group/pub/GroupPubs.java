package com.smate.web.group.model.group.pub;

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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组成果关系表.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "GROUP_PUBS")
public class GroupPubs implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3259682082695418013L;
  // 群组成果ID
  private Long groupPubsId;
  // 群组ID
  private Long groupId;
  // 成果ID
  private Long pubId;
  // 加密成果ID
  private String des3pubId;
  // 成果的拥有者.
  private Long ownerPsnId;
  // 作者
  private String authorNames;
  // 作者(只用于展示数据)
  private String authorNamesForShow;
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
  // 全文附件NodeId
  private Integer fullTextNodeId;
  private Integer sourceDbId;

  private Integer listEi = 0;
  private Integer listSci = 0;
  private Integer listIstp = 0;
  private Integer listSsci = 0;
  // 小图片路径 全文图片
  private String fileTypeIcoUrl;

  private int groupPubsIsShowComment = 0;// "评论"是否要显示链接

  private Integer labeled;
  private Integer relevance;
  private Date createDate = new Date();// 成果添加到群组的时间

  private Date updateDate;

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

  @Transient
  public String getFileTypeIcoUrl() {
    return fileTypeIcoUrl;
  }

  public void setFileTypeIcoUrl(String fileTypeIcoUrl) {
    this.fileTypeIcoUrl = fileTypeIcoUrl;
  }

  @Transient
  public String getDes3pubId() {
    if (StringUtils.isBlank(des3pubId) && this.pubId != null) {
      this.des3pubId = ServiceUtil.encodeToDes3(this.pubId.toString());
    }
    return des3pubId;
  }

  public void setDes3pubId(String des3pubId) {
    this.des3pubId = des3pubId;
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

  @Transient
  public String getAuthorNamesForShow() {
    return authorNamesForShow;
  }

  public void setAuthorNamesForShow(String authorNamesForShow) {
    this.authorNamesForShow = authorNamesForShow;
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
