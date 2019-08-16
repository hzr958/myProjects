package com.smate.core.base.utils.model.cas.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 同步NSFC用户..
 * 
 * @author cwli
 */
@Entity
@Table(name = "SYS_ROL_USER")
public class SysRolUser implements Serializable {
  private static final long serialVersionUID = -302924477116101106L;
  // SNSWS验证码关联(PsnInfoService-setPsnConnectedByCode,SetPsnConnectedByCodeComponent-execute)
  public final int FLAG_SNS_CODE = 1;
  // SNSWS密码关联(PsnInfoService-setPsnConnectedByCode)
  public final int FLAG_SNS_PWD = 2;
  // SNSWS直接设置帐号关联(PsnInfoService-setPsnConnected)
  public final int FLAG_SNS_LIKE = 3;
  // iris业务系统跳转过来在单位注册(InsPersonRegisterService-setPsnConnect)
  public final int FLAG_ROL_REGIST = 4;
  // isis人员匹配邮件确认(NsfcSyncPersonService-comfirmPerson)
  public final int FLAG_NSFC_COMFIRM = 5;
  // isis手工关联SCM人员(NsfcSyncPersonService-manulMatchPerson)
  public final int FLAG_NSFC_MANUL = 6;
  // isis的人员在SCM注册(NsfcSyncPersonService-registPerson)
  public final int FLAG_NSFC_REGIST = 7;
  // sns同步NSFC项目数据(SnsSyncRolService-saveNsfcSyncProject)
  public final int FLAG_SNS_SYNC_NSFC_PRJ = 8;
  // sns同步NSFC人员数据(SnsSyncRolService-saveRolSyncPerson)
  public final int FLAG_SNS_SYNC_NSFC_PSN = 9;
  // 科创委的人员同步数据（KcwSyncRegistPersonComponent）
  public final int FLAG_KCW_SYNC_PSN = 10;

  private Long id;

  private Long psnId;

  private String guid; // 整合其他系统使用的GUID

  private Long insId;// 标识来源于哪个rol

  private Long pcode;// 基金委用户对应ID
  // 关联方式：见本类常数FLAG_****
  private Integer flag;
  // 关联时间
  private Date lastDate;

  public SysRolUser() {
    super();
  }

  public SysRolUser(Long insId) {
    super();
    this.insId = insId;
  }

  public SysRolUser(String guid, Long insId) {
    super();
    this.guid = guid;
    this.insId = insId;
  }

  public SysRolUser(Long psnId, String guid, Long insId) {
    super();
    this.psnId = psnId;
    this.guid = guid;
    this.insId = insId;
  }

  public SysRolUser(Long psnId, Long pcode, Long insId) {
    super();
    this.psnId = psnId;
    this.pcode = pcode;
    this.insId = insId;
  }

  public SysRolUser(Long pcode, Long insId) {
    super();
    this.pcode = pcode;
    this.insId = insId;
  }

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SYS_ROL_USER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "GUID")
  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PCODE")
  public Long getPcode() {
    return pcode;
  }

  public void setPcode(Long pcode) {
    this.pcode = pcode;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "FLAG")
  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  @Column(name = "LAST_DATE")
  public Date getLastDate() {
    return lastDate;
  }

  public void setLastDate(Date lastDate) {
    this.lastDate = lastDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
