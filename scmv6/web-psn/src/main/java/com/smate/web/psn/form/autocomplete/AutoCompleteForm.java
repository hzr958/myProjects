package com.smate.web.psn.form.autocomplete;

import java.util.List;

import com.smate.core.base.consts.model.ConstRegion;

/**
 * 自动填词form
 * 
 * @author lhd
 *
 */
public class AutoCompleteForm {

  private Long psnId;// 人员id
  private String searchKey; // 检索关键字
  private String insName; // 机构名称(根据机构名称自动填充部门)
  private List<ConstRegion> regionList; // 地区列表
  private String type;// 提示的种类
  private String des3PsnId;
  private String suggestStr;
  private String suggestPsnName;
  private Long suggestPsnId;
  private String suggestInsName;
  private Long suggestInsId;
  private String suggestKw;
  private Integer suggestType;
  private String locale;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public List<ConstRegion> getRegionList() {
    return regionList;
  }

  public void setRegionList(List<ConstRegion> regionList) {
    this.regionList = regionList;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getSuggestStr() {
    return suggestStr;
  }

  public void setSuggestStr(String suggestStr) {
    this.suggestStr = suggestStr;
  }

  public String getSuggestPsnName() {
    return suggestPsnName;
  }

  public void setSuggestPsnName(String suggestPsnName) {
    this.suggestPsnName = suggestPsnName;
  }

  public Long getSuggestPsnId() {
    return suggestPsnId;
  }

  public void setSuggestPsnId(Long suggestPsnId) {
    this.suggestPsnId = suggestPsnId;
  }

  public String getSuggestInsName() {
    return suggestInsName;
  }

  public void setSuggestInsName(String suggestInsName) {
    this.suggestInsName = suggestInsName;
  }

  public Long getSuggestInsId() {
    return suggestInsId;
  }

  public void setSuggestInsId(Long suggestInsId) {
    this.suggestInsId = suggestInsId;
  }

  public String getSuggestKw() {
    return suggestKw;
  }

  public void setSuggestKw(String suggestKw) {
    this.suggestKw = suggestKw;
  }

  public Integer getSuggestType() {
    return suggestType;
  }

  public void setSuggestType(Integer suggestType) {
    this.suggestType = suggestType;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }


}
