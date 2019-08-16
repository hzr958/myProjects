package com.smate.web.psn.model.msgbox;

import java.io.Serializable;

/**
 * 消息中心form
 *
 * @author wsn
 *
 */
public class MsgBoxForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7769826697575358243L;
  private Long psnId; // 人员ID
  private String des3PsnId;// 加密的人员ID
  private String model;// 显示的模块
  private String whoFirst;// 成果认领和全文认领谁先显示

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getWhoFirst() {
    return whoFirst;
  }

  public void setWhoFirst(String whoFirst) {
    this.whoFirst = whoFirst;
  }



}
