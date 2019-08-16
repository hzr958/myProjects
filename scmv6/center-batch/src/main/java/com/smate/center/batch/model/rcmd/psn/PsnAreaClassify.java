package com.smate.center.batch.model.rcmd.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员领域大类，默认使用前两个项目大类，无项目情况下使用成果期刊大类.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_AREA_CLASSIFY")
public class PsnAreaClassify implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2571358586796056381L;
  private Long id;
  // 人员ID
  private Long psnId;
  // 人员领域分类
  private String classify;

  public PsnAreaClassify() {
    super();
  }

  public PsnAreaClassify(Long psnId, String classify) {
    super();
    this.psnId = psnId;
    this.classify = classify;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_AREA_CLASSIFY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "CLASSIFY")
  public String getClassify() {
    return classify;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setClassify(String classify) {
    this.classify = classify;
  }

}
