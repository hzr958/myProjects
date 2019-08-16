package com.smate.web.prj.xml;

/**
 * @author yamingd Xml校验的字段类
 */
public class PrjXmlErrorField {

  /**
   * 字段名.
   */
  private String name;
  /**
   * 错误编号.(0:为空).
   */
  private int errorNo;

  public PrjXmlErrorField(String name, int errorNo) {
    this.name = name;
    this.errorNo = errorNo;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the errorNo
   */
  public int getErrorNo() {
    return errorNo;
  }

  /**
   * @param errorNo the errorNo to set
   */
  public void setErrorNo(int errorNo) {
    this.errorNo = errorNo;
  }

}
