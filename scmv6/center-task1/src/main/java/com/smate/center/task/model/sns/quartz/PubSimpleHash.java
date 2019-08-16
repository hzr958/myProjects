package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 新成果查重hash表
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "V_PUB_SIMPLE_HASH")
public class PubSimpleHash implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8651416291835606340L;
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;// 成果ID
  @Column(name = "EN_HASH_CODE")
  private String enHashCode;// 英文名称+日期+类型生成的哈希值
  @Column(name = "ZH_HASH_CODE")
  private String zhHashCode;// 中文名称+日期+类型生成的哈希值
  @Column(name = "TP_HASH_ZH")
  private String tpHashZh;// 中文名称+类型生成的哈希值
  @Column(name = "TP_HASH_EN")
  private String tpHashEn;// 英文名称+类型生成的哈希值

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getEnHashCode() {
    return enHashCode;
  }

  public void setEnHashCode(String enHashCode) {
    this.enHashCode = enHashCode;
  }

  public String getZhHashCode() {
    return zhHashCode;
  }

  public void setZhHashCode(String zhHashCode) {
    this.zhHashCode = zhHashCode;
  }

  public String getTpHashZh() {
    return tpHashZh;
  }

  public void setTpHashZh(String tpHashZh) {
    this.tpHashZh = tpHashZh;
  }

  public String getTpHashEn() {
    return tpHashEn;
  }

  public void setTpHashEn(String tpHashEn) {
    this.tpHashEn = tpHashEn;
  }

}
