package com.smate.web.psn.model.resume;



import java.io.Serializable;

/**
 * 
 * @author liqinghua
 * 
 */
public class ConfigElement implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8948186404393622822L;

  private String elemCode;
  private Integer isShow;

  public ConfigElement() {
    super();
  }

  public ConfigElement(String elemCode, Integer isShow) {
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
