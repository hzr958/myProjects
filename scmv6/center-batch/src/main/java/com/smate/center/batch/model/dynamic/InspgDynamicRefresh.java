package com.smate.center.batch.model.dynamic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 动态刷新表
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * 
 */
@Entity
@Table(name = "V_INSPG_DYN_REFRESH")
public class InspgDynamicRefresh implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -9021369566573146965L;
  private Long id; // 刷新id
  private Long dynId; // 事先生成好动态id
  private Integer dynType; // 动态类型<模板类型> Enum
  private Integer status = 0; // 状态<默认0,失败; 99>
  private String idOriginal; // 数据来源id 可能多个
  private Long inspgId; // 动态产生相关人所属机构id
  private Long psnId; // 动态产生相关人id
  private Date createTime; // 动态创建时间
  private String errorMsg; // 错误记录，默认为空
  private Integer isAsyn = 1; // 0同步restful;1异步timetask;默认为1
  // 非持久化
  private List<Long> fileIds; // 附件列表
  private String content; // 动态内容

  public InspgDynamicRefresh() {
    super();
  }

  public InspgDynamicRefresh(Long id, Long dynId, Integer dynType, Integer status, String idOriginal, Long inspgId,
      Long psnId, Date createTime, String errorMsg, Integer isAsyn) {
    super();
    this.id = id;
    this.dynId = dynId;
    this.dynType = dynType;
    this.status = status;
    this.idOriginal = idOriginal;
    this.inspgId = inspgId;
    this.psnId = psnId;
    this.createTime = createTime;
    this.errorMsg = errorMsg;
    this.isAsyn = isAsyn;
  }

  @Override
  public String toString() {
    return "InspgDynamicRefresh [id=" + id + ", dynType=" + dynType + ",dynId=" + dynId + ", status=" + status
        + ", idOriginal=" + idOriginal + ", inspgId=" + inspgId + ", psnId=" + psnId + ", createTime=" + createTime
        + ", errorMsg=" + errorMsg + ", isAsyn=" + isAsyn + "]";
  }


  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_INSPG_DYNAMIC_REFRESH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "DYN_TYPE")
  public Integer getDynType() {
    return dynType;
  }

  public void setDynType(Integer dynType) {
    this.dynType = dynType;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "ID_ORIGINAL")
  public String getIdOriginal() {
    return idOriginal;
  }

  public void setIdOriginal(String idOriginal) {
    this.idOriginal = idOriginal;
  }

  @Column(name = "INSPG_ID")
  public Long getInspgId() {
    return inspgId;
  }

  public void setInspgId(Long inspgId) {
    this.inspgId = inspgId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Column(name = "ERROR_MSG")
  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  @Column(name = "IS_ASYN")
  public Integer getIsAsyn() {
    return isAsyn;
  }

  public void setIsAsyn(Integer isAsyn) {
    this.isAsyn = isAsyn;
  }

  @Column(name = "DYN_ID")
  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }


  @Transient
  public List<Long> getFileIds() {
    return fileIds;
  }

  public void setFileIds(List<Long> fileIds) {
    this.fileIds = fileIds;
  }

  @Transient
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }



}
