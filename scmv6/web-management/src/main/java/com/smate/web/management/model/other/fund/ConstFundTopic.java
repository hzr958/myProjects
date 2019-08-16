package com.smate.web.management.model.other.fund;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基金专题常量表.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "CONST_FUND_TOPIC")
public class ConstFundTopic implements Serializable {

  private static final long serialVersionUID = -1555490039124073014L;
  private Long fundId;// 基金ID.
  private String category;// 专题领域.
  private String keyword;// 专题关键词.
  private String topic;// 专题.

  public ConstFundTopic() {
    super();
  }

  public ConstFundTopic(Long fundId, String category, String keyword, String topic) {
    super();
    this.fundId = fundId;
    this.category = category;
    this.keyword = keyword;
    this.topic = topic;
  }

  @Id
  @Column(name = "FUND_ID")
  public Long getFundId() {
    return fundId;
  }

  @Column(name = "CATEGORY")
  public String getCategory() {
    return category;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "TOPIC")
  public String getTopic() {
    return topic;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
}
