package com.smate.center.open.model.interconnection;

/**
 * 移动端关联form
 * 
 * @author aijiangbin
 *
 */
public class MobileRelationForm {

  private String token; // 动态token
  private String back; // 重定向，第三方的url
  private String type; // 获取动态token接口传入的类型参数 dynamicTokenType
  private String zhlastName;// 姓
  private String zhfirstName; // 名

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getBack() {
    return back;
  }

  public void setBack(String back) {
    this.back = back;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getZhlastName() {
    return zhlastName;
  }

  public void setZhlastName(String zhlastName) {
    this.zhlastName = zhlastName;
  }

  public String getZhfirstName() {
    return zhfirstName;
  }

  public void setZhfirstName(String zhfirstName) {
    this.zhfirstName = zhfirstName;
  }



}
