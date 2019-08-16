package com.smate.sie.web.application.model.analysis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 相关学科中间表（获取学科）
 * 
 * @author sjzhou
 *
 */
@Entity
@Table(name = "KPI_KEYWORDS_TF_COTF_N_DIS")
public class KpiKeywordsTfCotfNDis implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2713647893545499046L;
  private KpiKeywordsTfCotfNDisPk disPk;
  private Integer counts;

  public KpiKeywordsTfCotfNDis() {
    super();
  }

  @EmbeddedId
  public KpiKeywordsTfCotfNDisPk getDisPk() {
    return disPk;
  }

  public void setDisPk(KpiKeywordsTfCotfNDisPk disPk) {
    this.disPk = disPk;
  }

  @Column(name = "COUNTS")
  public Integer getCounts() {
    return counts;
  }

  public void setCounts(Integer counts) {
    this.counts = counts;
  }

}
