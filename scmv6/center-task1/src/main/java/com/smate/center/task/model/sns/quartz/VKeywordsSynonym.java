package com.smate.center.task.model.sns.quartz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 拆分中英文关键词信息表
 * 
 * @author LIJUN
 *
 */
@Entity
@Table(name = "V_KEYWORDS_SYNONYM")
public class VKeywordsSynonym {
  private Long Id;
  private Long zhKeywordId;// 中文关键词ID 和V_KEYWORDS_DIC表ID对应
  private String zhKeyword;// 中文关键词
  private String zhKeywordtxt;// 格式化后的中文关键词
  private Long enKeywordId;// 英文关键词ID 和V_KEYWORDS_DIC表ID对应
  private String enKeyword;// 英文关键词
  private String enKeywordtxt;// 格式化后的英文关键词

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_KEYWORDS_SYNONYM", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  @Column(name = "ZH_KEYWORD_ID")
  public Long getZhKeywordId() {
    return zhKeywordId;
  }

  public void setZhKeywordId(Long zhKeywordId) {
    this.zhKeywordId = zhKeywordId;
  }

  @Column(name = "ZH_KEYWORD")
  public String getZhKeyword() {
    return zhKeyword;
  }

  public void setZhKeyword(String zhKeyword) {
    this.zhKeyword = zhKeyword;
  }

  @Column(name = "ZH_KWTXT")
  public String getZhKeywordtxt() {
    return zhKeywordtxt;
  }

  public void setZhKeywordtxt(String zhKeywordtxt) {
    this.zhKeywordtxt = zhKeywordtxt;
  }

  @Column(name = "EN_KEYWORD_ID")
  public Long getEnKeywordId() {
    return enKeywordId;
  }

  public void setEnKeywordId(Long enKeywordId) {
    this.enKeywordId = enKeywordId;
  }

  @Column(name = "EN_KEYWORD")
  public String getEnKeyword() {
    return enKeyword;
  }

  public void setEnKeyword(String enKeyword) {
    this.enKeyword = enKeyword;
  }

  @Column(name = "EN_KWTXT")
  public String getEnKeywordtxt() {
    return enKeywordtxt;
  }

  public void setEnKeywordtxt(String enKeywordtxt) {
    this.enKeywordtxt = enKeywordtxt;
  }

  public VKeywordsSynonym(Long id, Long zhKeywordId, String zhKeyword, String zhKeywordtxt, Long enKeywordId,
      String enKeyword, String enKeywordtxt) {
    super();
    Id = id;
    this.zhKeywordId = zhKeywordId;
    this.zhKeyword = zhKeyword;
    this.zhKeywordtxt = zhKeywordtxt;
    this.enKeywordId = enKeywordId;
    this.enKeyword = enKeyword;
    this.enKeywordtxt = enKeywordtxt;
  }

  public VKeywordsSynonym() {
    super();
  }

}
