package com.smate.web.v8pub.vo.pdwh;

import java.util.List;

public class PdwhPubIndustryVO {

  private String code; // code
  private String name; // 行业名
  private String ename; // 行业的英文名
  private boolean isEnd; // 是否最低级行业
  private List<PdwhPubIndustryVO> children; // 子级行业


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEnd() {
    return isEnd;
  }

  public void setEnd(boolean isEnd) {
    this.isEnd = isEnd;
  }

  public List<PdwhPubIndustryVO> getChildren() {
    return children;
  }

  public void setChildren(List<PdwhPubIndustryVO> children) {
    this.children = children;
  }

  public String getEname() {
    return ename;
  }

  public void setEname(String ename) {
    this.ename = ename;
  }

}
