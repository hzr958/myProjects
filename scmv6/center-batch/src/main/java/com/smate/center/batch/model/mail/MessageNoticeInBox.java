/**
 * 
 */
package com.smate.center.batch.model.mail;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 站内通知 收件箱.
 * 
 * @author yangpeihai
 * 
 */
@Entity
@Table(name = "MSG_NOTICE_IN_BOX")
public class MessageNoticeInBox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8053967372304684598L;
  // 主键
  private Long id;
  // 收件人Id
  private Long senderId;
  // 收件人
  private String psnName;
  // 收件人fristName
  private String firstName;
  // 收件人lastName
  private String lastName;
  // 收件人头像路径
  private String psnHeadUrl;
  // 邮件Id
  private Long noticeId;

  // 邮件状态 0--未读 1—已读 2 –已删除
  private Integer status;

  public MessageNoticeInBox() {
    super();
    // TODO Auto-generated constructor stub
  }

  public MessageNoticeInBox(Long noticeId, Integer status) {
    super();
    this.noticeId = noticeId;
    this.status = status;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MSG_NOTICE_IN_BOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the status
   */
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "NOTICE_ID")
  public Long getNoticeId() {
    return noticeId;
  }

  public void setNoticeId(Long noticeId) {
    this.noticeId = noticeId;
  }

  @Column(name = "PSN_ID")
  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  @Column(name = "PSN_NAME")
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Column(name = "PSN_HEAD_URL")
  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

}
