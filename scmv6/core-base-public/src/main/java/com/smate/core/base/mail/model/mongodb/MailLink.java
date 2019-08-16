package com.smate.core.base.mail.model.mongodb;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 邮件链接记录表
 * 
 * @author zzx
 *
 */
@Document(collection = "V_MAIL_LINK")
public class MailLink implements Serializable {
  private static final long serialVersionUID = 7331198806041511574L;
  @Id
  @Indexed
  private String linkId;// 链接id 附加当邮件短地址使用
  private String url;// 原链接地址
  private String urlDesc;// 描述
  private Integer count;// 已访问数量
  private Integer limitCount;// 限制访问数量 0=不限访问次数
  private Date timeOutDate;// 过期时间 空=不限
  private Date createDate;// 创建时间
  private Date updateDate;// 更新时间
  @Indexed
  private Long mailId;// 邮件id
  private String linkKey;
  @Indexed
  private Integer templateCode;

  public MailLink() {
    super();
  }

  public MailLink(String linkId, String url, String urlDesc, Integer count, Integer limitCount, Date timeOutDate,
      Date createDate, Date updateDate, Long mailId, String linkKey, Integer templateCode) {
    super();
    this.linkId = linkId;
    this.url = url;
    this.urlDesc = urlDesc;
    this.count = count;
    this.limitCount = limitCount;
    this.timeOutDate = timeOutDate;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.mailId = mailId;
    this.linkKey = linkKey;
    this.templateCode = templateCode;
  }

  public String getLinkId() {
    return linkId;
  }

  public void setLinkId(String linkId) {
    this.linkId = linkId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrlDesc() {
    return urlDesc;
  }

  public void setUrlDesc(String urlDesc) {
    this.urlDesc = urlDesc;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getLimitCount() {
    return limitCount;
  }

  public void setLimitCount(Integer limitCount) {
    this.limitCount = limitCount;
  }

  public Date getTimeOutDate() {
    return timeOutDate;
  }

  public void setTimeOutDate(Date timeOutDate) {
    this.timeOutDate = timeOutDate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public String getLinkKey() {
    return linkKey;
  }

  public void setLinkKey(String linkKey) {
    this.linkKey = linkKey;
  }

  public Integer getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(Integer templateCode) {
    this.templateCode = templateCode;
  }

}
