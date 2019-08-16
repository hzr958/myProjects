package com.smate.center.batch.service.pub.mq;


/**
 * 成果指派消息实体.
 * 
 * @author yamingd
 * 
 */
public class PubAssignMessage extends BaseLocalQueneMessage {

  /**
   * 
   */
  private static final long serialVersionUID = 1056785491800196509L;

  private Long pubId;
  private Long insId;
  private Long psnId;
  private Long batchNo;
  // 导入类型，0在线导入，1后台导入
  private Integer importType;
  // 指派模式:1(按单位)/2(按成果)/3(按人指派),请看：PubAssignMessageModeEnum
  private Integer mode;
  // 指派触发事件类别.请看：PubAssignMessageKindEnum
  private Integer messageKind;

  public PubAssignMessage() {
    super();
  }

  /**
   * 构造函数.
   * 
   * @param pubId 成果ID,当mode=2时，该参数必需，否则不必要.
   * @param insId 单位ID,必需
   * @param psnId 单位人员ID,当mode=3时，该参数必需，否则不必要
   * @param mode 指派模式:1(按单位)/2(按成果)/3(按人指派),请看：PubAssignMessageModeEnum
   * @param messageKind // 指派触发事件类别.请看：PubAssignMessageKindEnum
   * @param opPsnId 当前登录人.
   * @param batchNo
   */
  public PubAssignMessage(Long pubId, Long insId, Long psnId, Integer mode, Integer messageKind, Long opPsnId,
      Long batchNo) {
    super(opPsnId);
    this.pubId = pubId;
    this.insId = insId;
    this.psnId = psnId;
    this.mode = mode;
    this.messageKind = messageKind;
    this.batchNo = batchNo;
  }

  public String toString() {
    if (this.pubId != null) {
      return String.format("PubAssignMessage(insId=%d,opPsnId=%d,mode=%d,kind=%d,pubId=%d)",
          new Object[] {this.insId, this.getOpPsnId(), this.mode, this.messageKind, this.pubId});
    } else if (this.psnId != null) {
      return String.format("PubAssignMessage(insId=%d,opPsnId=%d,mode=%d,kind=%d,psnId=%d)",
          new Object[] {this.insId, this.getOpPsnId(), this.mode, this.messageKind, this.psnId});
    } else {
      return String.format("PubAssignMessage(insId=%d,opPsnId=%d,mode=%d,kind=%d,batchNo=%d)",
          new Object[] {this.insId, this.getOpPsnId(), this.mode, this.messageKind, this.batchNo});
    }
  }

  public Integer getImportType() {
    return importType;
  }

  public void setImportType(Integer importType) {
    this.importType = importType;
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
   * @return the psnId
   */
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the mode
   */
  public Integer getMode() {
    return mode;
  }

  /**
   * @param mode the mode to set
   */
  public void setMode(Integer mode) {
    this.mode = mode;
  }

  /**
   * @return the messageKind
   */
  public Integer getMessageKind() {
    return messageKind;
  }

  /**
   * @param messageKind the messageKind to set
   */
  public void setMessageKind(Integer messageKind) {
    this.messageKind = messageKind;
  }

  /**
   * @return the batchNo
   */
  public Long getBatchNo() {
    return batchNo;
  }

  /**
   * @param batchNo the batchNo to set
   */
  public void setBatchNo(Long batchNo) {
    this.batchNo = batchNo;
  }
}
