package com.smate.center.oauth.model.security;


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
 * 第三方系统注册表
 */

@Entity
@Table(name = "V_OPEN_THIRD_REG")
public class OpenThirdReg implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6737764431383962046L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_OPEN_THIRD_REG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; // 主键id
  @Column(name = "THIRD_SYS_NAME")
  private String thirdSysName; // 第三方系统名字
  @Column(name = "TOKEN")
  private String token; // 第三方系统标记
  @Column(name = "CREATE_DATE")
  private Date createDate; // 创建时间

  @Column(name = "PERMIT_IP")
  private String permitIp; // 允许的ip

  public OpenThirdReg() {
    super();
  }

  public String getPermitIp() {
    return permitIp;
  }

  public void setPermitIp(String permitIp) {
    this.permitIp = permitIp;
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



}
