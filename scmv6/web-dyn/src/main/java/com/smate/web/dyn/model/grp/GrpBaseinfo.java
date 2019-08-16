package com.smate.web.dyn.model.grp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

/**
 * 群组主体表
 * 
 * @author AiJiangBin
 *
 */
@Entity
@Table(name = "V_GRP_BASEINFO")
public class GrpBaseinfo implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 1319286676425139099L;

  @Id
  // @SequenceGenerator(name = "SEQ_GRP_BASEINFO_STORE", sequenceName = "SEQ_V_GRP_BASEINFO",
  // allocationSize = 1)
  // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GRP_BASEINFO_STORE")
  @Column(name = "GRP_ID")
  private Long grpId;// 群组id 主键

  @Column(name = "GRP_NO")
  private Long grpNo;// 群组grp 编号

  @Column(name = "GRP_NAME")
  private String grpName;// 群组名字

  @Column(name = "GRP_DESCRIPTION")
  private String grpDescription;// 群组简介

  @Column(name = "GRP_CATEGORY")
  private Integer grpCategory;// 群组类别

  @Column(name = "PROJECT_NO")
  private String projectNo;// 项目批准号、编号

  @Column(name = "PROJECT_STATUS")
  private Integer projectStatus;// 项目状态 ， 0申请 ， 1已通过

  @Column(name = "GRP_AUATARS")
  private String grpAuatars;// 群组头像地址

  @Column(name = "OPEN_TYPE")
  private String openType; // 群组开放类型 公开类型【O = 开放 ， H = 半开放 ， P=隐私】 默认 H

  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;// 群组拥有者

  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;// 群组创建者

  @Column(name = "STATUS")
  private String status;// 群组状态 ， [ 01 = 正常 ， 99删除] 默认值 01

  @Column(name = "CREATE_DATE")
  private Date createDate;// 群组创建日期

  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 群组更新日期


  public GrpBaseinfo() {
    super();
  }

  public GrpBaseinfo(String grpName, String grpDescription, Integer grpCategory, String grpAuatars) {
    super();
    this.grpName = grpName;
    this.grpDescription = grpDescription;
    this.grpCategory = grpCategory;
    this.grpAuatars = grpAuatars;
  }

  public GrpBaseinfo(Long grpId, String grpName) {
    super();
    this.grpId = grpId;
    this.grpName = grpName;
  }

  public GrpBaseinfo(Long grpId, Long grpNo, String grpName, String grpDescription, Integer grpCategory,
      String projectNo, Integer projectStatus, String grpAuatars, String openType, Long ownerPsnId, Long createPsnId,
      String status, Date createDate, Date updateDate) {
    this.grpId = grpId;
    this.grpNo = grpNo;
    this.grpName = grpName;
    this.grpDescription = grpDescription;
    this.grpCategory = grpCategory;
    this.projectNo = projectNo;
    this.projectStatus = projectStatus;
    this.grpAuatars = grpAuatars;
    this.openType = openType;
    this.ownerPsnId = ownerPsnId;
    this.createPsnId = createPsnId;
    this.status = status;
    this.createDate = createDate;
    this.updateDate = updateDate;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getGrpNo() {
    return grpNo;
  }

  public void setGrpNo(Long grpNo) {
    this.grpNo = grpNo;
  }

  public String getGrpName() {
    return grpName;
  }

  public void setGrpName(String grpName) {
    this.grpName = grpName;
  }

  public String getGrpDescription() {
    return grpDescription;
  }

  public void setGrpDescription(String grpDescription) {
    this.grpDescription = grpDescription;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public String getProjectNo() {
    return projectNo;
  }

  public void setProjectNo(String projectNo) {
    this.projectNo = projectNo;
  }

  public Integer getProjectStatus() {
    return projectStatus;
  }

  public void setProjectStatus(Integer projectStatus) {
    this.projectStatus = projectStatus;
  }

  public String getGrpAuatars() {
    // 群组默认图片
    if (StringUtils.isNotBlank(grpAuatars) && grpAuatars.contains("/resscmwebsns/images_v5/50X50g.gif")) {
      grpAuatars = grpAuatars.replace("/resscmwebsns/images_v5/50X50g.gif", "/resmod/smate-pc/img/logo_grpdefault.png");
    } else if (StringUtils.isBlank(grpAuatars)) {
      grpAuatars = "/resmod/smate-pc/img/logo_grpdefault.png";
    }
    return grpAuatars;
  }

  public void setGrpAuatars(String grpAuatars) {
    this.grpAuatars = grpAuatars;
  }


  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }



}
