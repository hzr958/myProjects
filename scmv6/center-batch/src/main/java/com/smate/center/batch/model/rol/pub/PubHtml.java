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
 * 成果列表html实体
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "pub_html")
public class PubHtml implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1767219172432785105L;

  private Long id;
  private Long pubId;
  private String htmlZh;
  private String htmlEn;
  private Long extendId;
  private Integer tempCode;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_HTML", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  @Column(name = "pub_id")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "html_zh")
  public String getHtmlZh() {
    return htmlZh;
  }

  @Column(name = "html_en")
  public String getHtmlEn() {
    return htmlEn;
  }

  @Column(name = "extend_id")
  public Long getExtendId() {
    return extendId;
  }

  @Column(name = "temp_code")
  public Integer getTempCode() {
    return tempCode;
  }

  public void setTempCode(Integer tempCode) {
    this.tempCode = tempCode;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setExtendId(Long extendId) {
    this.extendId = extendId;
  }

  public void setHtmlZh(String htmlZh) {
    this.htmlZh = htmlZh;
  }

  public void setHtmlEn(String htmlEn) {
    this.htmlEn = htmlEn;
  }
}
