package com.smate.web.v8pub.service.fileimport.extract;

import java.io.Serializable;

/**
 * 成果收录信息
 * 
 * @author aijiangbin
 * @date 2018年7月31日
 */
public class SitationInfo implements Serializable {

  private static final long serialVersionUID = 8025997320192196427L;
  /**
   * ei sci ssci istp
   */
  public String libraryName; // 收录机构名
  public Integer sitStatus; // 收录状态 0:未收录 ，1:收录
  public Integer sitOriginStatus;

  public String getLibraryName() {
    return libraryName;
  }

  public void setLibraryName(String libraryName) {
    this.libraryName = libraryName;
  }

  public Integer getSitStatus() {
    return sitStatus;
  }

  public void setSitStatus(Integer sitStatus) {
    this.sitStatus = sitStatus;
  }

  public Integer getSitOriginStatus() {
    return sitOriginStatus;
  }

  public void setSitOriginStatus(Integer sitOriginStatus) {
    this.sitOriginStatus = sitOriginStatus;
  }



}
