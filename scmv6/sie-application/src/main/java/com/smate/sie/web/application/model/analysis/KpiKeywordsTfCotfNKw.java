package com.smate.sie.web.application.model.analysis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 相关学科中间表（获取关键词）
 * 
 * @author sjzhou
 *
 */
@Entity
@Table(name = "KPI_KEYWORDS_TF_COTF_N_KW")
public class KpiKeywordsTfCotfNKw implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2713647893545499046L;
  private KpiKeywordsTfCotfNKwPk kwPk;
  private Integer counts;

  public KpiKeywordsTfCotfNKw() {
    super();
  }

  @EmbeddedId
  public KpiKeywordsTfCotfNKwPk getKwPk() {
    return kwPk;
  }

  public void setKwPk(KpiKeywordsTfCotfNKwPk kwPk) {
    this.kwPk = kwPk;
  }

  @Column(name = "COUNTS")
  public Integer getCounts() {
    return counts;
  }

  public void setCounts(Integer counts) {
    this.counts = counts;
  }

}
