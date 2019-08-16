package com.smate.center.open.model.nsfc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author tsz
 * 
 */
@Embeddable
public class NsfcPsnInsPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8222486498062965153L;
  /** 人员Id. */
  private Long psnId;
  /** 单位Id. */
  private Long insId;

  public NsfcPsnInsPk() {
    super();

  }

  public NsfcPsnInsPk(Long psnId, Long insId) {
    super();
    this.psnId = psnId;
    this.insId = insId;
  }

  /**
   * @return psnId
   */
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
