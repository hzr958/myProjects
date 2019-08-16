package com.smate.sie.core.base.utils.model.auditlog;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 审计日志.
 * 
 * @author yxy
 */

@Entity
@Table(name = "COM_AUDIT_LOG")
public class SieComAuditLog implements Serializable {

  private static final long serialVersionUID = 5721479031711878471L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_COM_AUDIT_LOG", sequenceName = "SEQ_COM_AUDIT_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COM_AUDIT_LOG")
  private Long id;// 主键

  @Column(name = "AUD_USER")
  private Long user;// 操作员

  @Column(name = "AUD_USER_NAME")
  private String username;// 操作员姓名

  @Column(name = "AUD_CONTENT")
  private String content;// 详细内容

  @Column(name = "AUD_LOG_NAME")
  private String logName;// 日志分类名称

  @Column(name = "AUD_ACTION_NAME")
  private String actionName;// 操作功能名称

  @Column(name = "AUD_DATE")
  private Date date;// 时间
  /**
   * 对应com_audit_trail表aud_id
   */
  @Column(name = "AUD_ID")
  private Long audId;

  public Long getAudId() {
    return audId;
  }

  public void setAudId(Long audId) {
    this.audId = audId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUser() {
    return user;
  }

  public void setUser(Long user) {
    this.user = user;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getLogName() {
    return logName;
  }

  public void setLogName(String logName) {
    this.logName = logName;
  }

  public String getActionName() {
    return actionName;
  }

  public void setActionName(String actionName) {
    this.actionName = actionName;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

}
