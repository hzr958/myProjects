package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

/**
 * 
 * @author oyh
 * 
 */

public class NsfcPrpPubModel implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8255572678550722278L;
  /**
   * 申报书id
   */
  private Long prpId;
  /**
   * 申请书guid
   */
  private String isisGuid;
  /**
   * 申请书名称
   */
  private String title;
  /**
   * 申请书年份
   */
  private Integer prpYear;
  /**
   * 申请书状态
   */
  private Integer status;
  /**
   * 申报人psnId
   */
  private Long prpPsnId;

  private String pubIds;
  // 检索成果
  private Integer beginYear;
  private Integer endYear;
  private Integer pubTypeId;
  private Long folderId;
  private Long groupId;
  private String pubTitle;
  private String pubOrder;
  private Integer myTab;
  // 近五年代表性论著数
  private Long repPubCount;

  private String jsonParams;

  // 成果序列
  private String seqJson;
  // 申请书版本号
  private Integer version;

  /**
   * @return the prpId
   */

  public Long getPrpId() {
    return prpId;
  }

  /**
   * @param prpId the prpId to set
   */
  public void setPrpId(Long prpId) {
    this.prpId = prpId;
  }

  /**
   * @return the isisGuid
   */
  public String getIsisGuid() {
    return isisGuid;
  }

  /**
   * @param isisGuid the isisGuid to set
   */
  public void setIsisGuid(String isisGuid) {
    this.isisGuid = isisGuid;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the prpYear
   */
  public Integer getPrpYear() {
    return prpYear;
  }

  /**
   * @param prpYear the prpYear to set
   */
  public void setPrpYear(Integer prpYear) {
    this.prpYear = prpYear;
  }

  /**
   * @return the status
   */
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the prpPsnId
   */
  public Long getPrpPsnId() {
    return prpPsnId;
  }

  /**
   * @param prpPsnId the prpPsnId to set
   */
  public void setPrpPsnId(Long prpPsnId) {
    this.prpPsnId = prpPsnId;
  }

  /**
   * @return the pubTypeId
   */
  public Integer getPubTypeId() {
    return pubTypeId;
  }

  /**
   * @param pubTypeId the pubTypeId to set
   */
  public void setPubTypeId(Integer pubTypeId) {
    this.pubTypeId = pubTypeId;
  }

  /**
   * @return the folderId
   */
  public Long getFolderId() {
    return folderId;
  }

  /**
   * @param folderId the folderId to set
   */
  public void setFolderId(Long folderId) {
    this.folderId = folderId;
  }

  /**
   * @return the groupId
   */
  public Long getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the beginYear
   */
  public Integer getBeginYear() {
    return beginYear;
  }

  /**
   * @param beginYear the beginYear to set
   */
  public void setBeginYear(Integer beginYear) {
    this.beginYear = beginYear;
  }

  /**
   * @return the endYear
   */
  public Integer getEndYear() {
    return endYear;
  }

  /**
   * @param endYear the endYear to set
   */
  public void setEndYear(Integer endYear) {
    this.endYear = endYear;
  }

  /**
   * @return the myTab
   */
  public Integer getMyTab() {
    return myTab;
  }

  /**
   * @return the pubIds
   */
  public String getPubIds() {
    return pubIds;
  }

  /**
   * @param pubIds the pubIds to set
   */
  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  /**
   * @param myTab the myTab to set
   */
  public void setMyTab(Integer myTab) {
    this.myTab = myTab;
  }

  /**
   * @return the jsonParams
   */
  public String getJsonParams() {
    return jsonParams;
  }

  /**
   * @param jsonParams the jsonParams to set
   */
  public void setJsonParams(String jsonParams) {
    this.jsonParams = jsonParams;
  }

  /**
   * @return the repPubCount
   */
  public Long getRepPubCount() {
    return repPubCount;
  }

  /**
   * @param repPubCount the repPubCount to set
   */
  public void setRepPubCount(Long repPubCount) {
    this.repPubCount = repPubCount;
  }

  public String getSeqJson() {
    return seqJson;
  }

  public void setSeqJson(String seqJson) {
    this.seqJson = seqJson;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getPubTitle() {
    return pubTitle;
  }

  public void setPubTitle(String pubTitle) {
    this.pubTitle = pubTitle;
  }

  public String getPubOrder() {
    return pubOrder;
  }

  public void setPubOrder(String pubOrder) {
    this.pubOrder = pubOrder;
  }

}
