package com.smate.center.batch.model.rol.pub;

import java.util.List;

import com.smate.center.batch.service.pub.mq.BaseLocalQueneMessage;

/**
 * 成果xml同步队列.
 * 
 * @author yamingd
 * 
 */
public class PubXmlSyncEvent extends BaseLocalQueneMessage {

  /**
   * 
   */
  private static final long serialVersionUID = -1481642707242493494L;

  private Long pubId;
  private Long insId;
  private Long pubOwnerPsnId;
  // 成果xml同步事件枚举.
  private Integer event;
  // 需要查重时设置本属性的重复ID
  private List<Long> snsPubIds;

  public PubXmlSyncEvent() {}

  /**
   * @param pubId 成果ID
   * @param insId 单位ID
   * @param pubOwnerPsnId 成果拥有人
   * @param event 事件类别
   * @param reqPsnId 当前登录人
   */
  public PubXmlSyncEvent(Long pubId, Long insId, Long pubOwnerPsnId, Integer event, Long reqPsnId) {
    super(reqPsnId);
    this.pubId = pubId;
    this.insId = insId;
    this.event = event;
    this.pubOwnerPsnId = pubOwnerPsnId;
  }

  /**
   * @return the pubId
   */
  public Long getPubId() {
    return pubId;
  }

  /**
   * @param pubId the pubId to set
   */
  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  /**
   * @return the insId
   */
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the event
   */
  public Integer getEvent() {
    return event;
  }

  /**
   * @param event the event to set
   */
  public void setEvent(Integer event) {
    this.event = event;
  }

  /**
   * @return the snsPubIds
   */
  public List<Long> getSnsPubIds() {
    return snsPubIds;
  }

  /**
   * @param snsPubIds the snsPubIds to set
   */
  public void setSnsPubIds(List<Long> snsPubIds) {
    this.snsPubIds = snsPubIds;
  }

  public Long getPubOwnerPsnId() {
    return pubOwnerPsnId;
  }

  public void setPubOwnerPsnId(Long pubOwnerPsnId) {
    this.pubOwnerPsnId = pubOwnerPsnId;
  }

}
