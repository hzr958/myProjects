package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

/**
 * 实验室成果model
 * 
 * @author oyh
 * 
 */

public class LabPubModel implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2937854127711908623L;
  private Integer version;
  private Long key;
  private Long pubType = null;
  private Integer pubYear = null;
  private String authors;
  private String title;
  private String source;
  private Integer isTag;
  private Integer isOpen;
  private Long pubId;
  private String pubTypeName;
  private String listInfo = null;
  private Integer seqNo;
  private String citation;
  private String pubIds;
  private String jsonParams;
  private String des3Key;
  private Long labPsnId;
  private Long pId;

  private Integer labPubType;
  private Integer pubRefTotal;
  private String pubAttMembers;
  private Integer isInsAtt;
  private Integer insOrder;

  public String getPubIds() {
    return pubIds;
  }

  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  public String getCitation() {
    return citation;
  }

  public void setCitation(String citation) {
    this.citation = citation;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public String getListInfo() {
    return listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Long getPubType() {
    return pubType;
  }

  public void setPubType(Long pubType) {
    this.pubType = pubType;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Integer getIsTag() {
    return isTag;
  }

  public void setIsTag(Integer isTag) {
    this.isTag = isTag;
  }

  public Integer getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  /**
   * @return the pId
   */
  public Long getPId() {
    return pId;
  }

  /**
   * @param pId the pId to set
   */
  public void setPId(Long pId) {
    this.pId = pId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  /**
   * @return the labPubType
   */
  public Integer getLabPubType() {
    return labPubType;
  }

  /**
   * @param labPubType the labPubType to set
   */
  public void setLabPubType(Integer labPubType) {
    this.labPubType = labPubType;
  }

  /**
   * @return the pubRefTotal
   */
  public Integer getPubRefTotal() {
    return pubRefTotal;
  }

  /**
   * @param pubRefTotal the pubRefTotal to set
   */
  public void setPubRefTotal(Integer pubRefTotal) {
    this.pubRefTotal = pubRefTotal;
  }

  /**
   * @return the pubAttMembers
   */
  public String getPubAttMembers() {
    return pubAttMembers;
  }

  /**
   * @param pubAttMembers the pubAttMembers to set
   */
  public void setPubAttMembers(String pubAttMembers) {
    this.pubAttMembers = pubAttMembers;
  }

  /**
   * @return the isInsAtt
   */
  public Integer getIsInsAtt() {
    return isInsAtt;
  }

  /**
   * @param isInsAtt the isInsAtt to set
   */
  public void setIsInsAtt(Integer isInsAtt) {
    this.isInsAtt = isInsAtt;
  }

  /**
   * @return the insOrder
   */
  public Integer getInsOrder() {
    return insOrder;
  }

  /**
   * @param insOrder the insOrder to set
   */
  public void setInsOrder(Integer insOrder) {
    this.insOrder = insOrder;
  }

  public String getPubTypeName() {
    return pubTypeName;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  /**
   * @return the jsonParams
   */
  public String getJsonParams() {
    return jsonParams;
  }

  /**
   * @return the labPsnId
   */
  public Long getLabPsnId() {
    return labPsnId;
  }

  /**
   * @param labPsnId the labPsnId to set
   */
  public void setLabPsnId(Long labPsnId) {
    this.labPsnId = labPsnId;
  }

  /**
   * @return the key
   */
  public Long getKey() {
    return key;
  }

  /**
   * @param key the key to set
   */
  public void setKey(Long key) {
    this.key = key;
  }

  /**
   * @return the des3Key
   */
  public String getDes3Key() {
    return des3Key;
  }

  /**
   * @param des3Key the des3Key to set
   */
  public void setDes3Key(String des3Key) {
    this.des3Key = des3Key;
  }

  /**
   * @param jsonParams the jsonParams to set
   */
  public void setJsonParams(String jsonParams) {
    this.jsonParams = jsonParams;
  }

}
