package com.smate.sie.center.task.model;

import java.io.Serializable;

/**
 * 项目查重参数.
 * 
 * @author yexingyuan
 * 
 */
public class PrjDupParam implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2554547522457203028L;
  private String zhTitle;
  private String enTitle;
  private String externalNo;
  private String prjFromName;

  public String getZhTitle() {
    return zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public String getExternalNo() {
    return externalNo;
  }

  public String getPrjFromName() {
    return prjFromName;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public void setExternalNo(String externalNo) {
    this.externalNo = externalNo;
  }

  public void setPrjFromName(String prjFromName) {
    this.prjFromName = prjFromName;
  }

}
