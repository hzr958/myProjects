package com.smate.center.task.model.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 专利社交记录
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_PATENT_GAM")
public class BdspPatentGam implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  /**
   * 转化金额
   */
  @Column(name = "AMT")
  private Double amt = 0.0;
  /**
   * 专利转化次数
   */
  @Column(name = "CONVERT")
  private Integer convert = 0;
  /**
   * 下载次数
   */
  @Column(name = "DOWNLOAD")
  private Integer download = 0;
  /**
   * 阅读次数
   */
  @Column(name = "VIEWCOUNT")
  private Integer view = 0;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Double getAmt() {
    return amt;
  }

  public void setAmt(Double amt) {
    this.amt = amt;
  }

  public Integer getConvert() {
    return convert;
  }

  public void setConvert(Integer convert) {
    this.convert = convert;
  }

  public Integer getDownload() {
    return download;
  }

  public void setDownload(Integer download) {
    this.download = download;
  }

  public Integer getView() {
    return view;
  }

  public void setView(Integer view) {
    this.view = view;
  }


}
