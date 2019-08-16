package com.smate.core.base.mail.model.mongodb;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 邮件链接访问记录
 * 
 * @author zzx
 *
 */
@Document(collection = "V_MAIL_LINK_RECORD")
public class MailLinkRecord implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  private String id;// 主键
  private String ip;// ip
  @Indexed
  private Long psnId;// 人员id,非科研之友用户设置为0
  private Date createDate;// 创建时间
  @Indexed
  private String linkId;// 链接id

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getLinkId() {
    return linkId;
  }

  public void setLinkId(String linkId) {
    this.linkId = linkId;
  }

}
