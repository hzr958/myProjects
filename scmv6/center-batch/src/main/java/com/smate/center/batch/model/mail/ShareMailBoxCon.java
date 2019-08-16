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
 * 推荐发件箱内容表_MJG_SCM-5910.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "SHARE_MAILBOX_CON")
public class ShareMailBoxCon implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6495409377169156252L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SHARE_MAILBOX_CON", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "MAIL_ID")
  private Long mailId;// 推荐发件ID.
  @Column(name = "TITLE_ZH")
  private String titleZh;// 中文标题.
  @Column(name = "TITLE_EN")
  private String titleEn;// 英文标题.
  @Column(name = "EXT_OTHER_INFO")
  private String extOtherInfo;// 扩展信息.
  @Column(name = "CONTENT")
  private String content;// 推荐理由.

  public ShareMailBoxCon() {
    super();
  }

  public ShareMailBoxCon(Long id, Long mailId, String titleZh, String titleEn, String extOtherInfo, String content) {
    super();
    this.id = id;
    this.mailId = mailId;
    this.titleZh = titleZh;
    this.titleEn = titleEn;
    this.extOtherInfo = extOtherInfo;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public String getTitleZh() {
    return titleZh;
  }

  public void setTitleZh(String titleZh) {
    this.titleZh = titleZh;
  }

  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  public String getExtOtherInfo() {
    return extOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    this.extOtherInfo = extOtherInfo;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
