package com.smate.center.task.model.group;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 给进展报告的项目负责人发邮件临时表
 * 
 * @author zll
 *
 */
@Entity
@Table(name = "PRJ_GRP_TMP")
public class PrjGrpTmp implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6819563759792332410L;

  @Id
  @Column(name = "GRP_ID")
  private Long grpId;// 群组id 主键

  @Column(name = "GRP_NAME")
  private String grpName;// 群组名字

  @Column(name = "PROJECT_NO")
  private String projectNo;// 项目批准号、编号

  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;// 群组拥有者

  @Column(name = "STATUS")
  private Integer status;// 5 没有项目成果

  public PrjGrpTmp() {
    super();
  }

  public PrjGrpTmp(Long grpId, String grpName, Long ownerPsnId) {
    this.grpId = grpId;
    this.grpName = grpName;
    this.ownerPsnId = ownerPsnId;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getGrpName() {
    return grpName;
  }

  public void setGrpName(String grpName) {
    this.grpName = grpName;
  }

  public String getProjectNo() {
    return projectNo;
  }

  public void setProjectNo(String projectNo) {
    this.projectNo = projectNo;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
