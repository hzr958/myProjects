package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author fanzhiqiang
 * 
 */
@Entity
@Table(name = "INS_REF_DB")
public class InsRefDb implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -1470144565137453590L;
  private InsRefDbId insRefDbId;
  // 标识当前记录是否要用.
  private Integer enabled;

  private Integer seqNo;
  // 用户进行登录验证的URL（同一db可以在不同机构中使用不同的登录验证方式） 针对内部用户.
  private String loginUrl;
  // 接受用户查询请求的URL（同一db可以在不同机构中使用不同的url处理查询请求）针对内部用户.
  private String actionUrl;
  // 用户进行登录验证的URL（同一db可以在不同机构中使用不同的登录验证方式） 针对外部用户.
  private String loginUrlInside;
  // 接受用户查询请求的URL同一db可以在不同机构中使用不同的url处理查询请求）针对外部用户.
  private String actionUrlInside;
  // 用户打开全文时替换的URL同一db可以在不同机构中使用不同的url处理查询请求）针对外部用户.
  private String fulltextUrl;
  // 用户打开全文时替换的URL同一db可以在不同机构中使用不同的url处理查询请求）针对外部用户.
  private String fulltextUrlInside;

  /**
   * 联合主键.
   * 
   * @return id
   */
  @EmbeddedId
  public InsRefDbId getInsRefDbId() {
    return insRefDbId;
  }

  public void setInsRefDbId(InsRefDbId insRefDbId) {
    this.insRefDbId = insRefDbId;
  }

  public Integer getEnabled() {
    return enabled;
  }

  public void setEnabled(Integer enabled) {
    this.enabled = enabled;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "LOGIN_URL")
  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  @Column(name = "ACTION_URL")
  public String getActionUrl() {
    return actionUrl;
  }

  public void setActionUrl(String actionUrl) {
    this.actionUrl = actionUrl;
  }

  @Column(name = "LOGIN_URL_INSIDE")
  public String getLoginUrlInside() {
    return loginUrlInside;
  }

  public void setLoginUrlInside(String loginUrlInside) {
    this.loginUrlInside = loginUrlInside;
  }

  @Column(name = "ACTION_URL_INSIDE")
  public String getActionUrlInside() {
    return actionUrlInside;
  }

  public void setActionUrlInside(String actionUrlInside) {
    this.actionUrlInside = actionUrlInside;
  }

  @Column(name = "FULLTEXT_URL")
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  @Column(name = "FULLTEXT_URL_INSIDE")
  public String getFulltextUrlInside() {
    return fulltextUrlInside;
  }

  public void setFulltextUrlInside(String fulltextUrlInside) {
    this.fulltextUrlInside = fulltextUrlInside;
  }
  /*
   * @Transient public String getConstDisciplineId() { return constDisciplineId; }
   * 
   * 
   * public void setConstDisciplineId(String constDisciplineId) { this.constDisciplineId =
   * constDisciplineId; }
   */
}
