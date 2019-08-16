package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 文献检索缓存记录表
 * 
 * @author warrior
 * 
 */
@Entity
@Table(name = "PSN_PUBALL_SEARCH")
public class PsnPubAllSearch implements Serializable {

  private static final long serialVersionUID = -5081065288536702827L;

  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PUBALL_SEARCH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long Id;
  // 人员ID
  @Column(name = "PSN_ID")
  private Long psnId;
  // 成果ID
  @Column(name = "PUBALL_ID")
  private Long pubAllId;
  // 默认排序：按查找索引返回的排序分值(数值越大表示匹配度越高_MJG_SCM-3906).
  @Column(name = "NUM")
  private Integer num;

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPubAllId() {
    return pubAllId;
  }

  public void setPubAllId(Long pubAllId) {
    this.pubAllId = pubAllId;
  }

  public Integer getNum() {
    return num;
  }

  public void setNum(Integer num) {
    this.num = num;
  }

}
