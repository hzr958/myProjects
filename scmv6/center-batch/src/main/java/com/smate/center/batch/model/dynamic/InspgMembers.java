package com.smate.center.batch.model.dynamic;

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
 * 机构成员表
 * 
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 */

@Entity
@Table(name = "V_INSPG_MEMBERS")
public class InspgMembers implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8486528057235500017L;
  private Long id; // 成员id
  private Long inspgId; // 机构主页id
  private Integer type; // 成员类型 0个人，1机构
  private String name; // 成员名字
  private String description; // 成员描述
  private String homePage; // 成员主页
  private String avatars; // 成员头像
  private Date createDate; // 创建日期

  public InspgMembers() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_INSPG_MEMBERS", allocationSize = 1)
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

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Column(name = "HOME_PAGE")
  public String getHomePage() {
    return homePage;
  }

  public void setHomePage(String homePage) {
    this.homePage = homePage;
  }

  @Column(name = "AVATARS")
  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
