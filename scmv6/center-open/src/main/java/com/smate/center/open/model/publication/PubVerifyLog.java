package com.smate.center.open.model.publication;

import com.smate.core.base.utils.string.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * 成果验证日志
 *
 * @author aijiangbin
 * @create 2018-12-06 10:21
 **/
@Entity
@Table (name = "V_PUB_VERIFY_LOG")
public class PubVerifyLog {
  @Id
  @Column (name = "ID")
  @SequenceGenerator (name = "SEQ_STORE", sequenceName = "SEQ_V_PUB_VERIFY_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long id ;  //每验证一条成果，就添加一条记录

  @Column (name = "TITLE")
  public String title; //验证pub的标题  length = 2000*2/3=1333

  @Column (name = "ITEM_STATUS")
  public Integer itemStatus; //论文验证状态

  @Column (name = "ITEM")
  public String item ;//验证的论文数据  length = 1333

  @Column (name = "CORRECT_DATA")
  public String correlationData ;//正确数据  length = 1333

  @Column (name = "TYPE")
  public String type ;//具体信息匹配的结果

  @Column (name = "ITEM_MSG")
  public String itemMsg ;//具体信息匹配的结果

  @Column (name = "GMT_CREATE")
  public Date gmtCreate  ; //创建时间

  @Column (name = "SERVICE_TYPE")
  public Integer serviceType;//服务类型，1=论文验证；2=主页成果验证

  @Column (name = "PARTICIPANT_NAMES")
  public String participantNames; //参与人  length = 500

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    if(StringUtils.isNotBlank(title) && title.length()>1333){
      title = title.substring(0,1333);
    }
    this.title = title;
  }

  public Integer getItemStatus() {
    return itemStatus;
  }

  public void setItemStatus(Integer itemStatus) {
    this.itemStatus = itemStatus;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    if(StringUtils.isNotBlank(item) && item.length()>1333){
      item = item.substring(0,1333);
    }
    this.item = item;
  }

  public String getCorrelationData() {
    return correlationData;
  }

  public void setCorrelationData(String correlationData) {
    if(StringUtils.isNotBlank(correlationData) && correlationData.length()>1333){
      correlationData = correlationData.substring(0,1333);
    }
    this.correlationData = correlationData;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getItemMsg() {
    return itemMsg;
  }

  public void setItemMsg(String itemMsg) {
    this.itemMsg = itemMsg;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Integer getServiceType() {
    return serviceType;
  }

  public void setServiceType(Integer serviceType) {
    this.serviceType = serviceType;
  }

  public String getParticipantNames() {
    return participantNames;
  }

  public void setParticipantNames(String participantNames) {
    this.participantNames = participantNames;
  }
}
