package com.smate.web.group.model.grp.label;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



/**
 * 群组文件 与群组标签关系
 * 
 * @author aijiangbin
 *
 */
@Entity
@Table(name = "V_GRP_FILE_LABEL")
public class GrpFileLabel {

  /**
   * 主键
   */
  @Id
  @SequenceGenerator(name = "SEQ_V_GRP_FILE_LABEL_STORE", sequenceName = "SEQ_V_GRP_FILE_LABEL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_V_GRP_FILE_LABEL_STORE")
  @Column(name = "ID")
  private Long id;
  /**
   * 群组文件id
   */
  @Column(name = "GRP_FILE_ID")
  private Long grpFileId;

  /**
   * 群组标签id
   */
  @Column(name = "GRP_LABEL_ID")
  private Long grpLabelId;


  /**
   * 关系创建人
   */
  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;

  /**
   * 关系创建时间
   */
  @Column(name = "CREATE_DATE")
  private Date createDate;

  /**
   * 0=正常， 1=删除
   */
  @Column(name = "STATUS")
  private Integer status;

  /**
   * 关系更新人
   */
  @Column(name = "UPDATE_PSN_ID")
  private Long updatePsnId;

  /**
   * 关系更新时间
   */
  @Column(name = "UPDATE_DATE")
  private Date updateDate;



  public GrpFileLabel() {
    super();
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGrpFileId() {
    return grpFileId;
  }

  public void setGrpFileId(Long grpFileId) {
    this.grpFileId = grpFileId;
  }

  public Long getGrpLabelId() {
    return grpLabelId;
  }

  public void setGrpLabelId(Long grpLabelId) {
    this.grpLabelId = grpLabelId;
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



}
