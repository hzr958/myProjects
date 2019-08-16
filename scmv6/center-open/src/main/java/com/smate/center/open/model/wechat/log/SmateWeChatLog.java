package com.smate.center.open.model.wechat.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * smate微信关系日志实体.
 * 
 * @author xys
 *
 */
@Entity
@Table(name = "SMATE_WECHAT_LOG")
public class SmateWeChatLog {

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SMATE_WECHAT_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键id

  @Column(name = "SMATE_OPENID")
  private Long smateOpenId;// 科研之友openid

  @Column(name = "WECHAT_OPENID")
  private String weChatOpenId;// 微信openid

  @Column(name = "TYPE")
  private String type;// 1-绑定；2-解绑；

  @Column(name = "STATUS")
  private int status;// 1：成功； -1：失败；

  @Column(name = "WECHAT_RESULT")
  private String weChatResult;// 微信接口调用返回结果

  @Column(name = "CREATE_TIME")
  private Date createTime;// 创建时间

  public SmateWeChatLog() {

  }

  public SmateWeChatLog(String weChatOpenId, Long smateOpenId, String type, int status, String weChatResult,
      Date createTime) {
    this.weChatOpenId = weChatOpenId;
    this.smateOpenId = smateOpenId;
    this.type = type;
    this.status = status;
    this.weChatResult = weChatResult;
    this.createTime = createTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSmateOpenId() {
    return smateOpenId;
  }

  public void setSmateOpenId(Long smateOpenId) {
    this.smateOpenId = smateOpenId;
  }

  public String getWeChatOpenId() {
    return weChatOpenId;
  }

  public void setWeChatOpenId(String weChatOpenId) {
    this.weChatOpenId = weChatOpenId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getWeChatResult() {
    return weChatResult;
  }

  public void setWeChatResult(String weChatResult) {
    this.weChatResult = weChatResult;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


}
