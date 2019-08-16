package com.smate.center.batch.model.sns.wechat.template;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * smate微信模板关系实体.
 * 
 * @author xys
 *
 */
@Entity
@Table(name = "SMATE_WECHAT_TEMPLATE")
public class SmateWeChatTemplate {

  @Id
  @Column(name = "SMATE_TEMP_ID")
  public Long smateTempId;// smate模板id
  @Column(name = "WECHAT_TEMP_ID")
  public String wechatTempId;// 微信模板id
  @Column(name = "ENABLED")
  public String enabled;// 0-禁用；1-启用；

  public Long getSmateTempId() {
    return smateTempId;
  }

  public void setSmateTempId(Long smateTempId) {
    this.smateTempId = smateTempId;
  }

  public String getWechatTempId() {
    return wechatTempId;
  }

  public void setWechatTempId(String wechatTempId) {
    this.wechatTempId = wechatTempId;
  }

  public String getEnabled() {
    return enabled;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }

}
