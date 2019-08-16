package com.smate.web.v8pub.po.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果和项目的引用模板
 * 
 * @author lhd
 */
@Entity
@Table(name = "QUOTE_TEMPLATE_INFO")
public class QuoteTemplateInfo implements Serializable {

  private static final long serialVersionUID = -6405775658522450333L;

  public final static Integer TYPE_PUB = 1;
  public final static Integer TYPE_PRJ = 2;

  @Id
  @Column(name = "ID")
  private Long id;

  // 模板类型 1：成果 2：项目
  @Column(name = "TYPE")
  private Integer type;

  // 成果类型
  @Column(name = "PUB_TYPE")
  private Integer pubType;

  // 引用的中文名称
  @Column(name = "QUOTE_ZH_NAME")
  private String quoteZhName;

  // 引用的英文名称
  @Column(name = "QUOTE_EN_NAME")
  private String quoteEnName;

  // 模板名称
  @Column(name = "TEMPLATE_NAME")
  private String templateName;

  // 状态 1：使用 0：不使用
  @Column(name = "STATUS")
  private Integer status;

  // 排序
  @Column(name = "SEQ_NO")
  private Integer seqNo;


  public QuoteTemplateInfo() {}



  public Long getId() {
    return id;
  }

  public Integer getType() {
    return type;
  }

  public String getQuoteZhName() {
    return quoteZhName;
  }

  public String getQuoteEnName() {
    return quoteEnName;
  }

  public String getTemplateName() {
    return templateName;
  }

  public Integer getStatus() {
    return status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setQuoteZhName(String quoteZhName) {
    this.quoteZhName = quoteZhName;
  }

  public void setQuoteEnName(String quoteEnName) {
    this.quoteEnName = quoteEnName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
