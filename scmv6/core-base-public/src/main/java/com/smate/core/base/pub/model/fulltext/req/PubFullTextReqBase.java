package com.smate.core.base.pub.model.fulltext.req;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.smate.core.base.enums.converter.PubFullTextReqStatusEnumAttributeConverter;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;

/**
 * 全文请求记录
 * 
 * @author houchuanjie
 *
 */
@Entity
@Table(name = "V_PUB_FULLTEXT_REQ_BASE")
public class PubFullTextReqBase {

  /**
   * 全文请求记录id
   */
  private Long reqId;
  /**
   * 全文请求的成果id
   */
  private Long pubId;
  /**
   * 成果所属的库，0-基准库，1-个人库
   */
  private PubDbEnum pubDb;
  /**
   * 发起请求的人员id
   */
  private Long reqPsnId;
  /**
   * 请求时间
   */
  private Date reqDate;
  /**
   * 处理时间
   */
  private Date updateDate;
  /**
   * 处理人id
   */
  private Long updatePsnId;
  /**
   * 处理状态，默认为未处理
   */
  private PubFullTextReqStatusEnum status = PubFullTextReqStatusEnum.UNPROCESSED;

  @SequenceGenerator(name = "SEQ_PUB_FULLTEXT_REQ", sequenceName = "SEQ_V_PUB_FULLTEXT_REQ_BASE")
  @GeneratedValue(generator = "SEQ_PUB_FULLTEXT_REQ")
  @Id
  @Column(name = "REQ_ID")
  public Long getReqId() {
    return reqId;
  }

  public void setReqId(Long reqId) {
    this.reqId = reqId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  /**
   * 获取成果所属的库，结合PubDbEnum中的SNS,PDWH来判断属于哪个库
   * 
   * @return 0-基准库，1-个人库
   */
  @Column(name = "PUB_DB")
  @Enumerated(EnumType.ORDINAL)
  public PubDbEnum getPubDb() {
    return pubDb;
  }

  /**
   * 设置成果所属的库，结合PubBelong中的SNS,PDWH来设置属于哪个库
   */
  public void setPubDb(PubDbEnum pubDb) {
    this.pubDb = pubDb;
  }

  @Column(name = "REQ_PSN_ID")
  public Long getReqPsnId() {
    return reqPsnId;
  }

  public void setReqPsnId(Long reqPsnId) {
    this.reqPsnId = reqPsnId;
  }

  @Column(name = "REQ_DATE")
  public Date getReqDate() {
    return reqDate;
  }

  public void setReqDate(Date reqDate) {
    this.reqDate = reqDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "UPDATE_PSN_ID")
  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
  }

  @Column(name = "STATUS")
  @Convert(converter = PubFullTextReqStatusEnumAttributeConverter.class)
  public PubFullTextReqStatusEnum getStatus() {
    return status;
  }

  public void setStatus(PubFullTextReqStatusEnum status) {
    this.status = status;
  }

}
