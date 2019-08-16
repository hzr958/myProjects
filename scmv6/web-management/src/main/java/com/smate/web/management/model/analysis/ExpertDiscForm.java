package com.smate.web.management.model.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * 同行专家研究领域(考虑由ConstDiscDataForm实体替换，后期将作废此类，统一管理_MJG_2013-07-29_SCM-3166<公共化各新功能页面左侧的学科代码加载逻辑>).
 * 
 * @author chenxiangrong
 * 
 */
public class ExpertDiscForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2404482825323925256L;
  private Long discId;
  private String discCode;
  private String discName;
  private String discEnName;
  private int expertNum;
  private List<ExpertDiscForm> suberDiscList;

  public Long getDiscId() {
    return discId;
  }

  public void setDiscId(Long discId) {
    this.discId = discId;
  }

  public String getDiscCode() {
    return discCode;
  }

  public void setDiscCode(String discCode) {
    this.discCode = discCode;
  }

  public String getDiscName() {
    return discName;
  }

  public void setDiscName(String discName) {
    this.discName = discName;
  }

  public String getDiscEnName() {
    return discEnName;
  }

  public void setDiscEnName(String discEnName) {
    this.discEnName = discEnName;
  }

  public int getExpertNum() {
    return expertNum;
  }

  public void setExpertNum(int expertNum) {
    this.expertNum = expertNum;
  }

  public List<ExpertDiscForm> getSuberDiscList() {
    return suberDiscList;
  }

  public void setSuberDiscList(List<ExpertDiscForm> suberDiscList) {
    this.suberDiscList = suberDiscList;
  }
}
