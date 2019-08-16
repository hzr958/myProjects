package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 中国省.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CONST_CN_PROVINCE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ConstCnProvince implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -3863729852432368455L;
  // 省份ID，对应const_region的id
  private Long prvId;
  // 省份代码
  private String prvCode;
  private String zhName;
  private String enName;
  // 同级别的英文排序字段
  private Integer zhSeq;
  // 同级别的英文排序字段
  private Integer enSeq;
  // 1省，2直辖市
  private Integer type;

  @Id
  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  @Column(name = "PRV_CODE")
  public String getPrvCode() {
    return prvCode;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  @Column(name = "ZH_SEQ")
  public Integer getZhSeq() {
    return zhSeq;
  }

  @Column(name = "EN_SEQ")
  public Integer getEnSeq() {
    return enSeq;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  public void setPrvCode(String prvCode) {
    this.prvCode = prvCode;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setZhSeq(Integer zhSeq) {
    this.zhSeq = zhSeq;
  }

  public void setEnSeq(Integer enSeq) {
    this.enSeq = enSeq;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
