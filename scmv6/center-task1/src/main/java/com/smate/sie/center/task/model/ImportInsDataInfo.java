package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 单位信息临时表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "IMPORT_INS_DATA_INFO")
public class ImportInsDataInfo implements Serializable {


  private static final long serialVersionUID = 7817647948269633791L;

  private ImportInsDataInfoId id;
  // 中文名
  private String zhName;
  // 英文名
  // private String enName;
  // 机构类型
  private String nature;
  // 所在国家
  private String country;
  // 所在省
  private String prov;
  // 所在城市
  private String city;
  // 所在县区
  private String dis;
  // 单位联系人
  private String contactName;
  // 联系邮箱
  private String contactEmail;
  // 联系电话
  private String contactTel;
  // 单位同步状态标记，-1：不需要进行同步，0：需要进行同步，1：同步成功，2：失败
  private Long synFlag;
  // 单位同步异常信息
  private String synMsg;
  // 单位地址
  private String address;
  // 单位网址
  private String url;
  // 社会统一信用代码
  private String uniformId1;

  @Id
  @AttributeOverrides({@AttributeOverride(name = "orgCode", column = @Column(name = "ORG_CODE")),
      @AttributeOverride(name = "token", column = @Column(name = "TOKEN"))})
  public ImportInsDataInfoId getId() {
    return id;
  }

  public void setId(ImportInsDataInfoId id) {
    this.id = id;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  // @Column(name = "EN_NAME")
  // public String getEnName() {
  // return enName;
  // }

  @Column(name = "NATURE")
  public String getNature() {
    return nature;
  }

  @Column(name = "PROV")
  public String getProv() {
    return prov;
  }

  @Column(name = "CITY")
  public String getCity() {
    return city;
  }


  @Column(name = "DIS")
  public String getDis() {
    return dis;
  }


  public void setDis(String dis) {
    this.dis = dis;
  }

  @Column(name = "CONTACT_NAME")
  public String getContactName() {
    return contactName;
  }

  @Column(name = "CONTACT_EMAIL")
  public String getContactEmail() {
    return contactEmail;
  }

  @Column(name = "CONTACT_TEL")
  public String getContactTel() {
    return contactTel;
  }

  @Column(name = "SYN_FLAG")
  public Long getSynFlag() {
    return synFlag;
  }

  @Column(name = "SYN_MSG")
  public String getSynMsg() {
    return synMsg;
  }

  @Column(name = "ADDRESS")
  public String getAddress() {
    return address;
  }

  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  @Column(name = "UNIFORM_ID1")
  public String getUniformId1() {
    return uniformId1;
  }

  @Column(name = "COUNTRY")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  // public void setEnName(String enName) {
  // this.enName = enName;
  // }

  public void setNature(String nature) {
    this.nature = nature;
  }

  public void setProv(String prov) {
    this.prov = prov;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public void setContactTel(String contactTel) {
    this.contactTel = contactTel;
  }

  public void setSynFlag(Long synFlag) {
    this.synFlag = synFlag;
  }

  public void setSynMsg(String synMsg) {
    this.synMsg = synMsg;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setUniformId1(String uniformId1) {
    this.uniformId1 = uniformId1;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
