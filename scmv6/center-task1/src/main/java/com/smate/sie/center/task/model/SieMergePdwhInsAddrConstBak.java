package com.smate.sie.center.task.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库标准单位地址信息常量备份表
 * 
 * @author YEXINGYUAN
 * @date 2018/10
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "MERGE_PDWH_INS_ADDR_CONST")
public class SieMergePdwhInsAddrConstBak implements Serializable {

  private Long constId;// 主键id
  private Long insId;// 单位id
  private String insName;// 单位名
  private String country;// 国家
  private String province;// 省
  private String city;// 市
  private String fullAddr;// 完整地址
  private Integer language;// 单位语言，1中文，2英文
  private Integer constStatus = 0;// 单位常量的状态，0默认，1 ins_id信息已被修改，2省市其他信息被修改
  private Integer lastOperator = 0;// 最后操作人，0默认为scm后台操作，SIE修改 为1 ，其他2
  private Long insNameHash;// 机构名hash值
  private Integer addrStatus = 0;// 单位具体地址可信度信息，0没有地址信息，1可信度高（百度api可以查到唯一地址），2可信度中（百度查到多个地址<只记录了一个>），3可信度较低（自动从名称中提取的省市）
  private Date updateTime;
  private Integer enable;// 0不可用，1可用 和institution表相同

  @Column(name = "ENABLE")
  public Integer getEnabled() {
    return enable;
  }

  public void setEnabled(Integer enabled) {
    this.enable = enabled;
  }

  public SieMergePdwhInsAddrConstBak() {
    super();
  }

  @Id
  @Column(name = "CONST_ID")
  public Long getConstId() {
    return constId;
  }

  public void setConstId(Long constId) {
    this.constId = constId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "COUNTRY")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Column(name = "PROVINCE")
  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  @Column(name = "CITY")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Column(name = "FULL_ADDR")
  public String getFullAddr() {
    return fullAddr;
  }

  public void setFullAddr(String fullAddr) {
    this.fullAddr = fullAddr;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  @Column(name = "CONST_STATUS")
  public Integer getConstStatus() {
    return constStatus;
  }

  public void setConstStatus(Integer constStatus) {
    this.constStatus = constStatus;
  }

  @Column(name = "LAST_OPERATOR")
  public Integer getLastOperator() {
    return lastOperator;
  }

  public void setLastOperator(Integer lastOperator) {
    this.lastOperator = lastOperator;
  }

  @Column(name = "ADDR_STATUS")
  public Integer getAddrStatus() {
    return addrStatus;
  }

  public void setAddrStatus(Integer addrStatus) {
    this.addrStatus = addrStatus;
  }

  @Column(name = "INS_NAME_HASH")
  public Long getInsNameHash() {
    return insNameHash;
  }

  public void setInsNameHash(Long insNameHash) {
    this.insNameHash = insNameHash;
  }

  @Column(name = "UPDATE_TIME")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
