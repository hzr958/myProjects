package com.smate.web.psn.model.psnhtml;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.web.psn.model.htmlextend.HtmlExtend;

/**
 * 人员列表html实体
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "psn_html")
public class PsnHtml implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1767219172432785105L;

  private Long id;
  private Long psnId;
  private String htmlZh;
  private String htmlEn;
  private Long extendId;
  private Integer tempCode;
  private Long insId;
  private Object sortObj; // 排序对象
  private HtmlExtend htmlExtend;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_HTML", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
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

  @Column(name = "ins_id")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setTempCode(Integer tempCode) {
    this.tempCode = tempCode;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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

  @Transient
  public Object getSortObj() {
    return sortObj;
  }

  public void setSortObj(Object sortObj) {
    this.sortObj = sortObj;
  }

  @Transient
  public HtmlExtend getHtmlExtend() {
    return htmlExtend;
  }

  public void setHtmlExtend(HtmlExtend htmlExtend) {
    this.htmlExtend = htmlExtend;
  }

}
