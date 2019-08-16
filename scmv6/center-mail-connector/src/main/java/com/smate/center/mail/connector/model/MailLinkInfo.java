package com.smate.center.mail.connector.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 链接信息Json数据封装类
 * 
 * @author zzx
 *
 */
public class MailLinkInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  private String key;// 链接 k 与邮件模版对应
  private String url;// 链接
  private String urlDesc = "";
  private Date timeOutDate = null;// 过期时间 空=不限
  private Integer limitCount = 0;// 限制访问数量 0=不限访问次数

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

  public Date getTimeOutDate() {
    return timeOutDate;
  }

  public void setTimeOutDate(Date timeOutDate) {
    this.timeOutDate = timeOutDate;
  }

  public Integer getLimitCount() {
    return limitCount;
  }

  public void setLimitCount(Integer limitCount) {
    this.limitCount = limitCount;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }



}
