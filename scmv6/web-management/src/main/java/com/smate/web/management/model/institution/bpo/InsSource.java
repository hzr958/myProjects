package com.smate.web.management.model.institution.bpo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位数据来源表.
 * 
 * @author zjh
 * 
 */
@Entity
@Table(name = "INS_SOURCE")
public class InsSource implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6962122818876401924L;
  // 主键，单位ID
  private Long insId;
  // 是否isis关联开通
  private Integer isisLink = 0;
  // 是否isis同步数据
  private Integer isisSync = 0;
  // 在线注册开通
  private Integer onlineReg = 0;
  // 邀请开通
  private Integer inviteReg = 0;

  public InsSource() {
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

  @Column(name = "INVITE_REG")
  public Integer getInviteReg() {
    return inviteReg;
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

  public void setInviteReg(Integer inviteReg) {
    this.inviteReg = inviteReg;
  }
}
