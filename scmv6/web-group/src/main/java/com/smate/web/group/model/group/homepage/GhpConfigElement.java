package com.smate.web.group.model.group.homepage;

import java.io.Serializable;

/**
 * 群组主页设置元素JSON实体.
 * 
 * @author liqinghua
 * 
 */
public class GhpConfigElement implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6644125718130532780L;
  /**
   * 
   */
  private String elemCode;
  private Integer isShow;

  public GhpConfigElement() {
    super();
  }

  public GhpConfigElement(String elemCode, Integer isShow) {
    super();
    this.elemCode = elemCode;
    this.isShow = isShow;
  }

  public String getElemCode() {
    return elemCode;
  }

  public void setElemCode(String elemCode) {
    this.elemCode = elemCode;
  }

  public Integer getIsShow() {
    return isShow;
  }

  public void setIsShow(Integer isShow) {
    this.isShow = isShow;
  }

}
