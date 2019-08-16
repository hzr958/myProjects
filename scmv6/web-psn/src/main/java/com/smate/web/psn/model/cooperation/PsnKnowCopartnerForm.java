package com.smate.web.psn.model.cooperation;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.string.ServiceUtil;

public class PsnKnowCopartnerForm implements Serializable {

  private static final long serialVersionUID = -854925212778872623L;

  // 当前人员加密Id
  private String des3CurrentId;
  // 个人psnId
  private Long psnId;
  private String des3PsnId; // 人员加密串
  // 合作者psnId
  private Long cptPsnId;
  private String des3CptPsnId;
  // 合作者姓名
  private String cptName;
  private String cptFirstName;
  private String cptLastName;
  // 合作者头像url
  private String cptHeadUrl;
  // 合作者头衔
  private String cptViewTitel;
  // 合作类型：4成果合作，5项目合作
  private String cptTypes;
  // 成果合作得分
  private Double pubScore;
  // 项目合作得分
  private Double prjScore;
  // 成果合作次数
  private Integer pubCount;
  // 成果合作次数
  private Integer prjCount;

  private Integer isFriend;
  // 是否是第一次加载
  private boolean firstPage = true;

  private String cptPsnViewName;// 合作者名称(显示用)_MJG_SCM-5707.

  private Integer isAll = 0;// 成果合作者-查看全部 1:是 0:否
  private Integer showList = 0; // 是否只显示人员列表 0：上面的标题也显示，1：只显示人员列表
  private boolean hasLogin = true; // 是否登录

  public Integer getIsAll() {
    return isAll;
  }

  public void setIsAll(Integer isAll) {
    this.isAll = isAll;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      if (StringUtils.isNotEmpty(this.des3PsnId)) {
        return Long.valueOf(ServiceUtil.decodeFromDes3(this.des3PsnId));
      }
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getCptPsnId() {
    return cptPsnId;
  }

  public void setCptPsnId(Long cptPsnId) {
    this.cptPsnId = cptPsnId;
  }

  public String getDes3CptPsnId() {
    return des3CptPsnId;
  }

  public void setDes3CptPsnId(String des3CptPsnId) {
    this.des3CptPsnId = des3CptPsnId;
  }

  public String getCptName() {
    return cptName;
  }

  public void setCptName(String cptName) {
    this.cptName = cptName;
  }

  public String getCptFirstName() {
    return cptFirstName;
  }

  public void setCptFirstName(String cptFirstName) {
    this.cptFirstName = cptFirstName;
  }

  public String getCptLastName() {
    return cptLastName;
  }

  public void setCptLastName(String cptLastName) {
    this.cptLastName = cptLastName;
  }

  public String getCptHeadUrl() {
    return cptHeadUrl;
  }

  public void setCptHeadUrl(String cptHeadUrl) {
    this.cptHeadUrl = cptHeadUrl;
  }

  public String getCptViewTitel() {
    return cptViewTitel;
  }

  public void setCptViewTitel(String cptViewTitel) {
    this.cptViewTitel = cptViewTitel;
  }

  public String getCptTypes() {
    return cptTypes;
  }

  public void setCptTypes(String cptTypes) {
    this.cptTypes = cptTypes;
  }

  public Double getPubScore() {
    return pubScore;
  }

  public void setPubScore(Double pubScore) {
    this.pubScore = pubScore;
  }

  public Double getPrjScore() {
    return prjScore;
  }

  public void setPrjScore(Double prjScore) {
    this.prjScore = prjScore;
  }

  public Integer getPubCount() {
    return pubCount;
  }

  public void setPubCount(Integer pubCount) {
    this.pubCount = pubCount;
  }

  public Integer getPrjCount() {
    return prjCount;
  }

  public void setPrjCount(Integer prjCount) {
    this.prjCount = prjCount;
  }

  public Integer getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(Integer isFriend) {
    this.isFriend = isFriend;
  }

  public String getCptPsnViewName() {
    return cptPsnViewName;
  }

  public void setCptPsnViewName(String cptPsnViewName) {
    this.cptPsnViewName = cptPsnViewName;
  }

  public boolean getFirstPage() {
    return firstPage;
  }

  public void setFirstPage(boolean firstPage) {
    this.firstPage = firstPage;
  }

  public String getDes3CurrentId() {
    return des3CurrentId;
  }

  public void setDes3CurrentId(String des3CurrentId) {
    this.des3CurrentId = des3CurrentId;
  }

  public Integer getShowList() {
    return showList;
  }

  public void setShowList(Integer showList) {
    this.showList = showList;
  }

  public boolean getHasLogin() {
    return hasLogin;
  }

  public void setHasLogin(boolean hasLogin) {
    this.hasLogin = hasLogin;
  }

}
