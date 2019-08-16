package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果拆分定时器任务表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PUB_SPLIT_XML_TASK")
public class PubSplitXmlTask implements Serializable {

  private static final long serialVersionUID = -5428419521329571172L;
  private Long pubId;// 成果ID.
  private Long result;// 拆分结果(0-未处理；1-拆分成功；2-拆分失败) .
  private Date dealDate;// 处理时间.
  private String pubSource;// 成果来源库.

  public PubSplitXmlTask() {
    super();
  }

  public PubSplitXmlTask(Long result, Date dealDate, String pubSource) {
    super();
    this.result = result;
    this.dealDate = dealDate;
    this.pubSource = pubSource;
  }

  public PubSplitXmlTask(Long pubId, Long result, Date dealDate, String pubSource) {
    super();
    this.pubId = pubId;
    this.result = result;
    this.dealDate = dealDate;
    this.pubSource = pubSource;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "RESULT")
  public Long getResult() {
    return result;
  }

  @Column(name = "DEAL_DATE")
  public Date getDealDate() {
    return dealDate;
  }

  @Column(name = "PUB_SOURCE")
  public String getPubSource() {
    return pubSource;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setResult(Long result) {
    this.result = result;
  }

  public void setDealDate(Date dealDate) {
    this.dealDate = dealDate;
  }

  public void setPubSource(String pubSource) {
    this.pubSource = pubSource;
  }

}
