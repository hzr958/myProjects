package com.smate.web.management.model.mail;

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
 * IRIS申请试用记录(新的)
 * 
 * @author yhx
 *
 */
@Entity
@Table(name = "V_IRISSZ_MAIL_DETAIL")
public class IrisszMailDetail implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "V_SEQ_IRISSZ_MAIL_DETAIL", sequenceName = "V_SEQ_IRISSZ_MAIL_DETAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_IRISSZ_MAIL_DETAIL")
  private Long id;
  @Column(name = "CLIECT_IP")
  private String cliectIP;
  @Column(name = "NAME")
  private String name;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "TEL")
  private String tel;
  @Column(name = "TYPE")
  private Integer type;// 类别 1.科技管理;2.成果推广;3.技术转移
  @Column(name = "INS_NAME")
  private String insName;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "REMARK")
  private String remark;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "DOMAIN")
  private String domain;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCliectIP() {
    return cliectIP;
  }

  public void setCliectIP(String cliectIP) {
    this.cliectIP = cliectIP;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }


}
