package com.smate.web.mobile.dyn.vo;

import java.io.Serializable;

/**
 * 移动端动态VO
 * 
 * @author wsn
 * @date Jun 18, 2019
 */
public class SmateDynVO implements Serializable {

  private String resType; // 资源类型
  private String des3ResId; // 资源加密ID

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public String getDes3ResId() {
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }


}
