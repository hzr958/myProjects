package com.smate.center.batch.model.pdwh.pubimport;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class PdwhPubImportContext implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -8352362828006491737L;

  private Integer pubType;

  private Integer dbId;

  private Date now; // 导入时的时间；

  private Long dupPubId;// 重复的成果

  private Integer priorityFlag = 0; // 1：当前导入成果优先级大于等于当前，需要覆盖已保存xml文件；0：当前导入成果优先级小于当前，不需要覆盖已保存xml文件

  private Long insId; // 单位id

  private Map<String, Object> pdwhPubInfoMap;// 从xml获取的导入成果信息

  private Integer replaceFlag = 0; // 1.完全替换已经成果信息；0.正常流程覆盖(默认)

  private String operation; // 标记当前操作：update 或者是saveNew；

  private Long currentPsnId; // 标记当前操作人，可能是后台导入或者sns检索导入同步回pdwh；后台导入标识为2L

  public PdwhPubImportContext() {
    super();
  }

  public Integer getReplaceFlag() {
    return replaceFlag;
  }

  public void setReplaceFlag(Integer replaceFlag) {
    if (replaceFlag == null) {
      this.replaceFlag = 0;
    }
    this.replaceFlag = replaceFlag;
  }

  public Map<String, Object> getPdwhPubInfoMap() {
    return pdwhPubInfoMap;
  }

  public void setPdwhPubInfoMap(Map<String, Object> pdwhPubInfoMap) {
    this.pdwhPubInfoMap = pdwhPubInfoMap;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Date getNow() {
    return now;
  }

  public void setNow(Date now) {
    this.now = now;
  }

  public Integer getPriorityFlag() {
    return priorityFlag;
  }

  public void setPriorityFlag(Integer priorityFlag) {
    this.priorityFlag = priorityFlag;
  }

  public Long getDupPubId() {
    return dupPubId;
  }

  public void setDupPubId(Long dupPubId) {
    this.dupPubId = dupPubId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public Long getCurrentPsnId() {
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }


}
