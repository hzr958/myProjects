package com.smate.center.open.model.inspg;

import java.io.Serializable;
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
 * 机构主页管理员关系实体类
 * 
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "V_INSPG_ADMIN")
public class InspgAdmin implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -140315258949779538L;

  private Long id; // 主键,需要序列
  private Long inspgId;
  private Long psnId;
  private Date updateTime;
  private Integer type;// 类型 11:创建人,22:管理员
  private String psnName; // 人员姓名 Transient
  private String showTitle;// 人员头衔Transient
  private String psnAvatars;// 人员头像Transient
  private boolean isCurFriend; // 于当前登录人是好友关系？

  public InspgAdmin() {
    super();
  }

  public InspgAdmin(Long inspgId, Long psnId) {
    super();
    this.inspgId = inspgId;
    this.psnId = psnId;
  }

  public InspgAdmin(Long id, Long inspgId, Long psnId, Date updateTime) {
    super();
    this.id = id;
    this.inspgId = inspgId;
    this.psnId = psnId;
    this.updateTime = updateTime;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_INSPG_ADMIN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  @Column(name = "UPDATE_TIME")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Transient
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Transient
  public String getShowTitle() {
    return showTitle;
  }

  public void setShowTitle(String showTitle) {
    this.showTitle = showTitle;
  }

  @Transient
  public String getPsnAvatars() {
    return psnAvatars;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  @Transient
  public boolean getIsCurFriend() {
    return isCurFriend;
  }

  public void setIsCurFriend(boolean isCurFriend) {
    this.isCurFriend = isCurFriend;
  }



}
