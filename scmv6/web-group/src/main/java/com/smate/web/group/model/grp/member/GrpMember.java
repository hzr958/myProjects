package com.smate.web.group.model.grp.member;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 群组成员关系实体类.
 * 
 * @author zzx
 * 
 */
@Entity
@Table(name = "V_GRP_MEMBER")
public class GrpMember implements Serializable {

  private static final long serialVersionUID = -1690711197224784175L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GRP_MEMBER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键
  @Column(name = "GRP_ID")
  private Long grpId;// 群组Id
  @Column(name = "PSN_ID")
  private Long psnId;// 群组成员Id
  @Column(name = "GRP_ROLE")
  private Integer grpRole;// 群组中的角色[1=群组拥有者,2=管理员, 3=组员]
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间
  @Column(name = "STATUS")
  private String status;// 群组成员状态[01=正常,99=删除（被移除出群组）,98=删除（自动退出群组）]
  @Column(name = "LAST_VISIT_DATE")
  private Date lastVisitDate;// 最后访问时间 访问自己的群组
  @Column(name = "TOP_DATE")
  private Date topDate;// 置顶时间

  public GrpMember() {}

  public GrpMember(Long id, Long psnId) {
    super();
    this.id = id;
    this.psnId = psnId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getGrpRole() {
    return grpRole;
  }

  public void setGrpRole(Integer grpRole) {
    this.grpRole = grpRole;
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

  public Date getLastVisitDate() {
    return lastVisitDate;
  }

  public void setLastVisitDate(Date lastVisitDate) {
    this.lastVisitDate = lastVisitDate;
  }

  public Date getTopDate() {
    return topDate;
  }

  public void setTopDate(Date topDate) {
    this.topDate = topDate;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

}
