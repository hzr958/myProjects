package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果推荐给读者统计
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "RECOM_READER_STATISTICS")
public class RecomReaderStatistics implements Serializable {

  private static final long serialVersionUID = 2747513071427605470L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_RECOM_READER_STATISTICS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "RECOM_PSN_ID")
  private Long recomPsnId;

  // 被推荐的东西的主键
  @Column(name = "ACTION_KEY")
  private Long actionKey;

  // 被推荐东西的类型 详情请看DynamicConstant.java
  @Column(name = "ACTION_TYPE")
  private Integer actionType;

  // 操作日期
  @Column(name = "RECOMMEND_DATE")
  private Date recommendDate;

  // 格式化的日期，方便查询（不带时分秒）
  @Column(name = "FORMATE_DATE")
  private Long formateDate;

  @Column(name = "COUNT")
  private Long count;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getRecomPsnId() {
    return recomPsnId;
  }

  public void setRecomPsnId(Long recomPsnId) {
    this.recomPsnId = recomPsnId;
  }

  public Long getActionKey() {
    return actionKey;
  }

  public void setActionKey(Long actionKey) {
    this.actionKey = actionKey;
  }

  public Integer getActionType() {
    return actionType;
  }

  public void setActionType(Integer actionType) {
    this.actionType = actionType;
  }

  public Date getRecommendDate() {
    return recommendDate;
  }

  public void setRecommendDate(Date recommendDate) {
    this.recommendDate = recommendDate;
  }

  public Long getFormateDate() {
    return formateDate;
  }

  public void setFormateDate(Long formateDate) {
    this.formateDate = formateDate;
  }

  public Long getCount() {
    if (count == null) {
      return 0l;
    }
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

}
