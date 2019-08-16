package com.smate.web.institution.form;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 页面显示的机构信息
 *
 * @author aijiangbin
 * @create 2019-07-04 10:48
 **/
public class InstitutionInfo {


    public  Integer  insId ;  // 机构id
    public  String  des3InsId = "";  // 机构id
    public String  insName = ""; // 机构名
    public String  logoUrl = ""; //
    public String  domain = ""; //
    public String  manageUrl = ""; //
    public String  visitUrl = ""; //
    public Integer  stView = 0; // 阅读统计数
    public Integer  stShare = 0; // 分享统计数
    public Integer  stFollow = 0; // 关注统计数



  public String getVisitUrl() {
    return visitUrl;
  }

  public void setVisitUrl(String visitUrl) {
    this.visitUrl = visitUrl;
  }

  public String getManageUrl() {
    return manageUrl;
  }

  public void setManageUrl(String manageUrl) {
    this.manageUrl = manageUrl;
  }

  public String getDes3InsId() {
    if( NumberUtils.isNotNullOrZero(insId) && StringUtils.isBlank(des3InsId)){
      des3InsId = Des3Utils.encodeToDes3(Integer.toString(insId));
    }
    return des3InsId;
  }

  public void setDes3InsId(String des3InsId) {
    this.des3InsId = des3InsId;
  }


  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public Integer getInsId() {
    return insId;
  }

  public void setInsId(Integer insId) {
    this.insId = insId;
  }

  public Integer getStView() {
    return stView;
  }

  public void setStView(Integer stView) {
    this.stView = stView;
  }

  public Integer getStShare() {
    return stShare;
  }

  public void setStShare(Integer stShare) {
    this.stShare = stShare;
  }

  public Integer getStFollow() {
    return stFollow;
  }

  public void setStFollow(Integer stFollow) {
    this.stFollow = stFollow;
  }
}
