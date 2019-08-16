package com.smate.center.merge.model.sns.thirdparty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 微信openid与scm-openid关系表 历史表.
 * 
 * @since 6.0.1
 */
@Entity
@Table(name = "V_WECHAT_RELATION_HIS")
public class WeChatRelationHis implements Serializable {
  private static final long serialVersionUID = 896812447028492075L;
  @Id
  @Column(name = "ID")
  private Long id;// 主键id
  @Column(name = "WECHAT_OPENID")
  private String webChatOpenId;// 微信openid
  @Column(name = "SMATE_OPENID")
  private Long smateOpenId;// 科研之友openid
  @Column(name = "CREATE_TIME")
  private Date createTime;// 创建时间
  @Column(name = "DEL_TIME")
  private Date delTime;// 移动到历史表时间

  public WeChatRelationHis() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getWebChatOpenId() {
    return webChatOpenId;
  }

  public void setWebChatOpenId(String webChatOpenId) {
    this.webChatOpenId = webChatOpenId;
  }

  public Long getSmateOpenId() {
    return smateOpenId;
  }

  public void setSmateOpenId(Long smateOpenId) {
    this.smateOpenId = smateOpenId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getDelTime() {
    return delTime;
  }

  public void setDelTime(Date delTime) {
    this.delTime = delTime;
  }
}
