package com.smate.web.mobile.group.vo;

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
 * 群组标签类
 * 
 * @author xiexing
 *
 */
@Entity
@Table(name = "V_GRP_LABEL")
public class GrpLabel {

  /**
   * 主键
   */
  @Id
  @SequenceGenerator(name = "SEQ_V_GRP_LABEL_STORE", sequenceName = "SEQ_V_GRP_LABEL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_V_GRP_LABEL_STORE")
  @Column(name = "LABEL_ID")
  private Long labelId;
  /**
   * 群组id
   */
  @Column(name = "GRP_ID")
  private Long grpId;

  /**
   * 标签名
   */
  @Column(name = "LABEL_NAME")
  private String labelName;

  /**
   * 标签创建人
   */
  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;

  /**
   * 标签创建时间
   */
  @Column(name = "CREATE_DATE")
  private Date createDate;

  /**
   * 0=正常， 1=删除
   */
  @Column(name = "STATUS")
  private Integer status;

  /**
   * 标签更新人
   */
  @Column(name = "UPDATE_PSN_ID")
  private Long updatePsnId;

  /**
   * 标签更新时间
   */
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  /**
   * 资源数量，文件，成果。。。
   */
  @Transient
  private Integer resCount;

  public GrpLabel() {
    super();
  }

  public Long getLabelId() {
    return labelId;
  }

  public void setLabelId(Long labelId) {
    this.labelId = labelId;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getLabelName() {
    return labelName;
  }

  public void setLabelName(String labelName) {
    this.labelName = labelName;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getResCount() {
    return resCount;
  }

  public void setResCount(Integer resCount) {
    this.resCount = resCount;
  }



}
