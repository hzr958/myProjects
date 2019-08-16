package com.smate.web.psn.model.htmlextend;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * html扩展实体
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "HTML_EXTEND")
public class HtmlExtend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1767219172432785105L;

  private Long id;
  private String htmlZh;
  private String htmlEn;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_HTML_EXTEND", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  @Column(name = "EXTEND_HTML_ZH")
  public String getHtmlZh() {
    return htmlZh;
  }

  @Column(name = "EXTEND_HTML_EN")
  public String getHtmlEn() {
    return htmlEn;
  }

  public void setHtmlZh(String htmlZh) {
    this.htmlZh = htmlZh;
  }

  public void setHtmlEn(String htmlEn) {
    this.htmlEn = htmlEn;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
