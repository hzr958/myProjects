package com.smate.center.task.model.sns.msg;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 消息内容实体类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_MSG_CONTENT")
public class MsgContent implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 消息内容ID
   */
  @Id
  @Column(name = "CONTENT_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_MSG_CONTENT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long contentId;
  /**
   * 消息内容，存放json、 页面显示需要的字段 （人员信息除外）
   */
  @Column(name = "CONTENT")
  private String content;

  public MsgContent() {
    super();
  }

  public MsgContent(Long contentId, String content) {
    super();
    this.contentId = contentId;
    this.content = content;
  }

  public Long getContentId() {
    return contentId;
  }

  public void setContentId(Long contentId) {
    this.contentId = contentId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  @Override
  public String toString() {
    return "MsgContent [contentId=" + contentId + ", content=" + content + "]";
  }
}
