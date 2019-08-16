package com.smate.center.task.model.pdwh.pub;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * 原始数据与基准库成果关联关系表
 * 
 * @author zll
 *
 */
@Entity
@Table(name = "ORIGINAL_PDWH_PUB_RELATION")
public class OriginalPdwhPubRelation {

  private Long id;// Id
  private Long xmlId;// 原始数据id
  private String seqNo;// 原始数据序列
  private Long insId;// 成果单位
  private Long psnId;// 成果导入人员
  private Integer recordFrom;// 原始数据来源 1：后台导入，2：在线导入，3：crossref
  private Integer status;// 处理状态,状态为2是更新引用新增的成果,不用处理
  private Long pdwhPubId;
  private String errorMsg;
  private Date createDate;
  private Date updateDate;

  public OriginalPdwhPubRelation() {
    super();
  }

  public OriginalPdwhPubRelation(Long xmlId, String seqNo, Long insId, Long psnId, Integer recordFrom, Integer status,
      Date createDate) {
    super();
    this.xmlId = xmlId;
    this.seqNo = seqNo;
    this.insId = insId;
    this.psnId = psnId;
    this.recordFrom = recordFrom;
    this.status = 0;
    this.createDate = createDate;
  }

  public OriginalPdwhPubRelation(Long xmlId, Long psnId, Integer recordFrom, Integer status, Date createDate) {
    super();
    this.xmlId = xmlId;
    this.psnId = psnId;
    this.recordFrom = recordFrom;
    this.status = status;
    this.createDate = createDate;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ORIGINALE_PDWH_PUB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "XML_ID")
  public Long getXmlId() {
    return xmlId;
  }

  public void setXmlId(Long xmlId) {
    this.xmlId = xmlId;
  }

  @Column(name = "SEQ_NO")
  public String getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(String seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "RECORD_FROM")
  public Integer getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
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

  @Column(name = "ERROR_MSG")
  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }
}
