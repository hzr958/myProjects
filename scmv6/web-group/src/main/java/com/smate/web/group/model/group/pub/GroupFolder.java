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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 群组文件夹.
 * 
 * @author tj
 * 
 */
@Entity
@Table(name = "GROUP_FOLDER")
public class GroupFolder implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8751175436100394963L;
  // 群组文件夹主键
  private Long groupFolderId;
  // 文件夹名称
  private String folderName;
  // 群组文件夹类型(群组文件夹类型[P=成果，R=文献，F=文件，J=项目，W=作业，M=教学课件])
  private String folderType;
  // 创建时间
  private Date createDate;
  // 0:不可用；1：可用
  private String enabled;
  // 创建人psn_id
  private Long createPsnId;
  // 群组ID
  private Long groupId;
  // 更新时间
  private Date updateDate;
  // 更新人psn_id
  private Long updatePsnId;

  // 成果、文献或文件的数量
  private Integer folderSum = 0;

  @Id
  @Column(name = "GROUP_FOLDER_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_FOLDER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getGroupFolderId() {
    return groupFolderId;
  }

  @Column(name = "FOLDER_NAME")
  public String getFolderName() {
    return folderName;
  }

  @Column(name = "FOLDER_TYPE")
  public String getFolderType() {
    return folderType;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "ENABLED")
  public String getEnabled() {
    return enabled;
  }

  public void setGroupFolderId(Long groupFolderId) {
    this.groupFolderId = groupFolderId;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public void setFolderType(String folderType) {
    this.folderType = folderType;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }

  @Column(name = "CREATE_PSN_ID")
  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  @Column(name = "UPDATE_PSN_ID")
  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
  }

  @Column(name = "FOLDER_SUM")
  public Integer getFolderSum() {
    return folderSum;
  }

  public void setFolderSum(Integer folderSum) {
    this.folderSum = folderSum;
  }

}
