package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RERMC_PSN_KW_HOT")
public class RermcPsnKwHot implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5797974089286607112L;
  // 主键
  private Long id;
  // 人员id
  private Long psnId;
  // 热词id
  private Long kwhotId;
  // 热词
  private String keyword;
  // 热词小写
  private String kwText;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "kwhot_id")
  public Long getKwhotId() {
    return kwhotId;
  }

  @Column(name = "keyword")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "kw_text")
  public String getKwText() {
    return kwText;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setKwhotId(Long kwhotId) {
    this.kwhotId = kwhotId;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKwText(String kwText) {
    this.kwText = kwText;
  }
}
