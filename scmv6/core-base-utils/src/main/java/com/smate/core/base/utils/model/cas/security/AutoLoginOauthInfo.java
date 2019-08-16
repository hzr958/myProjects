package com.smate.core.base.utils.model.cas.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 自动登录 信息记录表 （记录 一个自动登录的 id 能不能用）
 * 
 * @author tsz
 *
 */
@Entity
// 表名与类名不相同时重新定义表名.
@Table(name = "sys_auto_login")
public class AutoLoginOauthInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6697278832178003424L;

  @Id
  @Column(name = "security_Id")
  private String securityId; // 加密id (主键)
  @Column(name = "CREATE_TIME")
  private Date createTime; // 创建时间
  @Column(name = "OVERDUE_TIME")
  private Date overdueTime; // 过期时间
  @Column(name = "USE_TIMES")
  private Integer useTimes; // 使用次数 (有些登录加密串 是只能使用一次的 ) 0表示未使用过
  @Column(name = "PSN_ID")
  private Long psnId; // 对应 加密 id 是属于哪个人的
  @Column(name = "LOGIN_TYPE")
  private String loginType; // 这次生成的自动登录 是属于 哪种方式的（有第3方系统的，邮件的.....）
  @Column(name = "LAST_USE_TIME")
  private Date lastUseTime; // 最后使用时间
  @Column(name = "TOKEN")
  private String token;

  public AutoLoginOauthInfo() {
    super();
  }

  public AutoLoginOauthInfo(String securityId, Date createTime, Date overdueTime, Integer useTimes, Long psnId,
      String loginType) {
    super();
    this.securityId = securityId;
    this.createTime = createTime;
    this.overdueTime = overdueTime;
    this.useTimes = useTimes;
    this.psnId = psnId;
    this.loginType = loginType;
  }


  public String getSecurityId() {
    return securityId;
  }

  public void setSecurityId(String securityId) {
    this.securityId = securityId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getOverdueTime() {
    return overdueTime;
  }

  public void setOverdueTime(Date overdueTime) {
    this.overdueTime = overdueTime;
  }

  public Integer getUseTimes() {
    return useTimes;
  }

  public void setUseTimes(Integer useTimes) {
    this.useTimes = useTimes;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getLoginType() {
    return loginType;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }

  public Date getLastUseTime() {
    return lastUseTime;
  }

  public void setLastUseTime(Date lastUseTime) {
    this.lastUseTime = lastUseTime;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
