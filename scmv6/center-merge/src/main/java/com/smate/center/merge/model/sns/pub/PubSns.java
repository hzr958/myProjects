package com.smate.center.merge.model.sns.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人库成果基础信息.
 * 
 * @author tsz
 * @date 2018/06/01 15:55
 */
@Entity
@Table(name = "V_PUB_SNS")
public class PubSns extends PubSuper {
  private static final long serialVersionUID = -1491582107838535689L;
  /**
   * 成果id.
   */
  @Id
  @Column(name = "PUB_ID")
  protected Long pubId;
  /**
   * 记录来源.
   */
  @Column(name = "RECORD_FROM")
  private int recordFrom;
  /**
   * 成果状态.
   */
  @Column(name = "STATUS")
  private int status;

  /**
   * 最新修改版本号
   */
  @Column(name = "VERSION")
  private Long version;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(int recordFrom) {
    this.recordFrom = recordFrom;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
