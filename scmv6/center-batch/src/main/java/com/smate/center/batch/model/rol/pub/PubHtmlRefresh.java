package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果列表刷新类
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "PUB_HTML_REFRESH")
public class PubHtmlRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2994305293709810303L;
  private Long id;
  private Long pubId;
  private Integer tempCode;
  private Integer status;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_HTML_REFRESH", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  @Column(name = "pub_id")
  public Long getPubId() {
    return pubId;
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

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setTempCode(Integer tempCode) {
    this.tempCode = tempCode;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
