package com.smate.center.task.model.rcmd.quartz;

import java.io.Serializable;

/**
 * 成果确认单位信息.
 * 
 * @author zjh
 *
 */
public class PubConfirmInsForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5234286673721792964L;
  private Long insId;
  private String insName;
  private Long penddingNum;

  public Long getInsId() {
    return insId;
  }

  public String getInsName() {
    return insName;
  }

  public Long getPenddingNum() {
    return penddingNum;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setPenddingNum(Long penddingNum) {
    this.penddingNum = penddingNum;
  }

}
