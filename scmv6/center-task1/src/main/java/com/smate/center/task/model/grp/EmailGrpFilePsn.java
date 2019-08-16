package com.smate.center.task.model.grp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EMAIL_GRP_FILE_PSN")
public class EmailGrpFilePsn {
  // 群组ID
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_EMAIL_GROUP_FILE_PSN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;// 上传文件的psnid
  @Column(name = "GRP_ID")
  private Long grpId;// 添加文件的群组
  @Column(name = "GRP_FILE_ID")
  private Long grpFileId;// 添加到群组的群组文件id
  @Column(name = "CREATE_DATE")
  private Date createDate;// 把文件添加到群组的时间
  @Column(name = "STATUS")
  private String status;// 0=未发送 ， 1=发送成功， 99 =发送失败
  @Column(name = "FILE_MODULE_TYPE")
  private Integer fileModuleType;// 0=群组文件;1= 作业;2= 课件

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getGrpFileId() {
    return grpFileId;
  }

  public void setGrpFileId(Long grpFileId) {
    this.grpFileId = grpFileId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getFileModuleType() {
    return fileModuleType;
  }

  public void setFileModuleType(Integer fileModuleType) {
    this.fileModuleType = fileModuleType;
  }



}

