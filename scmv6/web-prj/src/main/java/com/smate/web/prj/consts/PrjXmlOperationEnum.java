package com.smate.web.prj.consts;

/**
 * Xml操作枚举项目.
 * 
 * @author liqinghua
 * 
 */
public enum PrjXmlOperationEnum {

  /**
   * 录入.
   */
  Enter("0"),
  /**
   * 检索导入.
   */
  Import("1");

  private String value;

  private PrjXmlOperationEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }



}
