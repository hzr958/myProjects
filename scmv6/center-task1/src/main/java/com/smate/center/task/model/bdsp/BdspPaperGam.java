package com.smate.center.task.model.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 论文社交记录
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_PAPER_GAM")
public class BdspPaperGam implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  /**
   * 引用数
   */
  @Column(name = "CITATION")
  private Integer citation = 0;
  /**
   * 下载数
   */
  @Column(name = "DOWNLOAD")
  private Integer download = 0;
  /**
   * 阅读数
   */
  @Column(name = "VIEWCOUNT")
  private Integer view = 0;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getCitation() {
    return citation;
  }

  public void setCitation(Integer citation) {
    this.citation = citation;
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
