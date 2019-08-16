package com.smate.center.task.model.search;

import java.io.Serializable;

/**
 * 用户检索form表单.
 * 
 * @author mjg
 * @version 5.0
 * @since 2014-11-05
 */
public class UserSearchDataForm implements Serializable {

  private static final long serialVersionUID = 9073628171121618803L;
  private Long psnId;
  private String zhInfo;
  private String enInfo;
  private int scoreNum;
  private String insNameZh;// 用户所属单位中文名称_MJG_SCM-5103.
  private String insNameEn;// 用户所属单位英文名称_MJG_SCM-5103.
  private String insNameAbbr;// 所属单位简写_MJG_SCM-5103.
  private Long insId;// 所属单位ID_MJG_SCM-5103.

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getZhInfo() {
    return zhInfo;
  }

  public void setZhInfo(String zhInfo) {
    this.zhInfo = zhInfo;
  }

  public String getEnInfo() {
    return enInfo;
  }

  public void setEnInfo(String enInfo) {
    this.enInfo = enInfo;
  }

  public int getScoreNum() {
    return scoreNum;
  }

  public void setScoreNum(int scoreNum) {
    this.scoreNum = scoreNum;
  }

  public String getInsNameZh() {
    return insNameZh;
  }

  public void setInsNameZh(String insNameZh) {
    this.insNameZh = insNameZh;
  }

  public String getInsNameEn() {
    return insNameEn;
  }

  public void setInsNameEn(String insNameEn) {
    this.insNameEn = insNameEn;
  }

  public String getInsNameAbbr() {
    return insNameAbbr;
  }

  public void setInsNameAbbr(String insNameAbbr) {
    this.insNameAbbr = insNameAbbr;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
