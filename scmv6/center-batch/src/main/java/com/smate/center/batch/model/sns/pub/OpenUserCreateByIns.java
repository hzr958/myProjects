package com.smate.center.batch.model.sns.pub;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 为指定机构的用户生成openId
 */

@Entity
@Table(name = "V_OPEN_USER_CREATE_BY_INS")
public class OpenUserCreateByIns implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6860581468391315777L;
  @Id
  @Column(name = "ID")
  private Long id; // 主键id

  @Column(name = "THIRD_SYS_NAME")
  private String thirdSysName; // 第三方系统名字

  @Column(name = "TOKEN")
  private String token; // 第三方系统标记

  @Column(name = "CREATE_DATE")
  private Date createDate; // 创建时间

  @Column(name = "INS_ID")
  private Long insId;

  @Column(name = "STATUS")
  private Integer status;

  public OpenUserCreateByIns() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getThirdSysName() {
    return thirdSysName;
  }

  public void setThirdSysName(String thirdSysName) {
    this.thirdSysName = thirdSysName;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
