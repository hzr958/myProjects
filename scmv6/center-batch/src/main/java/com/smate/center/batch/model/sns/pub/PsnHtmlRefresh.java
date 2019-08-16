package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员列表刷新类
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "PSN_HTML_REFRESH")
public class PsnHtmlRefresh implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 7540622713333822815L;
  private Long id;
  private Long psnId;
  private Integer tempCode;
  private Integer status;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_HTML_REFRESH", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "temp_code")
  public Integer getTempCode() {
    return tempCode;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setTempCode(Integer tempCode) {
    this.tempCode = tempCode;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
