package com.smate.center.open.model.interconnection;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 互联互通 刷新 群组成果记录表
 * 
 * @author AiJiangBin
 *
 */
@Entity
@Table(name = "V_UNION_REFRESH_GROUP_PUB_LOG")
public class UnionRefreshGroupPubLog {

  /**
   * 
   */
  private static final long serialVersionUID = -5834164417691193247L;


  private Long id;
  private String groupCode;
  private Long psnId;
  private Long openId;
  private String token;
  private Date logDate;
  private String msg;


  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_UNION_PUB_LOG", sequenceName = "seq_union_refresh_group_pub", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_UNION_PUB_LOG")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "GROUP_CODE")
  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "OPEN_ID")
  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

  @Column(name = "TOKEN")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Column(name = "LOG_DATE")
  public Date getLogDate() {
    return logDate;
  }

  public void setLogDate(Date logDate) {
    this.logDate = logDate;
  }

  @Column(name = "MSG")
  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public UnionRefreshGroupPubLog() {
    super();
    // TODO Auto-generated constructor stub
  }


}
