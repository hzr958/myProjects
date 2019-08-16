package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PDWH_PUB_CLASSIFICATION_NSFC")
public class PdwhPubForClassificationNsfc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4376403044442541044L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "DB_ID")
  private Integer dbId;

  @Column(name = "STATUS")
  private Integer status; // 1.成果分类完成；2.成果分类完成但是未映射至scm分类；3.成果分类出错；7.成果的期刊jnlid为空；8.通过jnlid没有取到对应期刊数据通过jnlid没有取到对应期刊数据；9.成果为空

  public PdwhPubForClassificationNsfc() {
    super();
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
