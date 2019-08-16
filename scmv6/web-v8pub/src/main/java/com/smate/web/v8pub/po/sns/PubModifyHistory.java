package com.smate.web.v8pub.po.sns;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 个人库成果修改历史记录
 * 
 * @author yhx
 *
 */
@Document(collection = "V_PUB_MODIFY_HISTORY")
public class PubModifyHistory implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  private String id;// 主键
  @Indexed
  private Long pubId;// 成果ID
  @Indexed
  private Long psnId;// 成果拥有者
  private Long version;// 版本号
  private String pubJson;// 成果内容
  private Date createDate;// 创建时间

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getPubJson() {
    return pubJson;
  }

  public void setPubJson(String pubJson) {
    this.pubJson = pubJson;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
