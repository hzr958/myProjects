package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_UNION_LOGIN_LOG")
public class UserUnionLoginLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3388465611166142251L;
  private Long psnId;
  private Integer isLogin = 0;// 是否登录过
  private Integer isUnion = 0;// 是否关联过其他业务系统
  private Integer psnFundScore;// 基金推荐加分分数（业务系统有关联加3分，登录过加2分）

  public UserUnionLoginLog() {
    super();
  }

  public UserUnionLoginLog(Long psnId) {
    super();
    this.psnId = psnId;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "IS_LOGIN")
  public Integer getIsLogin() {
    return isLogin;
  }

  public void setIsLogin(Integer isLogin) {
    this.isLogin = isLogin;
  }

  @Column(name = "IS_UNION")
  public Integer getIsUnion() {
    return isUnion;
  }

  public void setIsUnion(Integer isUnion) {
    this.isUnion = isUnion;
  }

  @Column(name = "PSN_FUND_SCORE")
  public Integer getPsnFundScore() {
    return psnFundScore;
  }

  public void setPsnFundScore(Integer psnFundScore) {
    this.psnFundScore = psnFundScore;
  }

}
