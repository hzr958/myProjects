package com.smate.web.management.model.institution.bpo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位更改标记，方便检索式维护查询.
 * 
 * @author zjh
 * 
 */
@Entity
@Table(name = "INS_ALIAS_STATUS")
public class InsAliasStatus implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8737266868019483791L;
  // isis新关联开通(0/1)
  private Integer isisLink = 0;
  // isis同步数据(0/1)
  private Integer isisSync = 0;
  // 在线注册开通(0/1)
  private Integer onlineReg = 0;
  // 单位改名(0/1)
  private Integer nameCg = 0;
  // 检索组确认状态(0/1)
  private Integer confirmStatus = 0;
  // 数据是否完整
  private Integer dtComplete = 0;
  // 最后标记时间
  private Date cgDate;
  // 主键，单位id
  private Long insId;

  public InsAliasStatus() {
    super();
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "ISIS_LINK")
  public Integer getIsisLink() {
    return isisLink;
  }

  @Column(name = "ISIS_SYNC")
  public Integer getIsisSync() {
    return isisSync;
  }

  @Column(name = "ONLINE_REG")
  public Integer getOnlineReg() {
    return onlineReg;
  }

  @Column(name = "NAME_CG")
  public Integer getNameCg() {
    return nameCg;
  }

  @Column(name = "CONFIRM_STATUS")
  public Integer getConfirmStatus() {
    return confirmStatus;
  }

  public void setConfirmStatus(Integer confirmStatus) {
    this.confirmStatus = confirmStatus;
  }

  @Column(name = "CG_DATE")
  public Date getCgDate() {
    return cgDate;
  }

  @Column(name = "DT_COMPLETE")
  public Integer getDtComplete() {
    return dtComplete;
  }

  public void setDtComplete(Integer dtComplete) {
    this.dtComplete = dtComplete;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setIsisLink(Integer isisLink) {
    this.isisLink = isisLink;
  }

  public void setIsisSync(Integer isisSync) {
    this.isisSync = isisSync;
  }

  public void setOnlineReg(Integer onlineReg) {
    this.onlineReg = onlineReg;
  }

  public void setNameCg(Integer nameCg) {
    this.nameCg = nameCg;
  }

  public void setCgDate(Date cgDate) {
    this.cgDate = cgDate;
  }

}
