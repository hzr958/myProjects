package com.smate.center.open.model.register;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * 人员注册
 * 
 * @author tsz
 */
public class RolSyncPerson implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1387199814576032366L;
  // 申请人guid
  private String guid;
  // 申请人pcode
  private String pcode;
  // 申请人姓名
  private String cname;
  // 申请人邮件
  private String email;
  // 申请人所在单位
  private String orgname;
  // 申请人单位性质
  private String orgnature;
  // 来源哪个rol
  private Long insId;
  private String synfrom;
  // 项目编号
  private String prjcode;
  // 报告年份
  private String rptyear;

  public RolSyncPerson() {
    super();
  }

  public RolSyncPerson(Long insId) {
    super();
    this.insId = insId;
  }

  public RolSyncPerson(String guid, String pcode, String cname, String email, String orgname, String orgnature,
      Long insId) {
    super();
    this.guid = guid;
    this.pcode = pcode;
    this.cname = cname;
    this.email = email;
    this.orgname = orgname;
    this.orgnature = orgnature;
    this.insId = insId;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getPcode() {
    return pcode;
  }

  public void setPcode(String pcode) {
    this.pcode = pcode;
  }

  public String getCname() {
    return cname;
  }

  public void setCname(String cname) {
    this.cname = cname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getOrgname() {
    return orgname;
  }

  public void setOrgname(String orgname) {
    this.orgname = orgname;
  }

  public String getOrgnature() {
    return orgnature;
  }

  public void setOrgnature(String orgnature) {
    this.orgnature = orgnature;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the synFrom
   */
  public String getSynfrom() {
    return synfrom;
  }

  /**
   * @param synFrom the synFrom to set
   */
  public void setSynfrom(String synfrom) {
    this.synfrom = synfrom;
  }

  /**
   * @return the prjcode
   */
  public String getPrjcode() {
    return prjcode;
  }

  /**
   * @param prjcode the prjcode to set
   */
  public void setPrjcode(String prjcode) {
    this.prjcode = prjcode;
  }

  /**
   * @return the rptyear
   */
  public String getRptyear() {
    return rptyear;
  }

  /**
   * @param rptyear the rptyear to set
   */
  public void setRptyear(String rptyear) {
    this.rptyear = rptyear;
  }

}
