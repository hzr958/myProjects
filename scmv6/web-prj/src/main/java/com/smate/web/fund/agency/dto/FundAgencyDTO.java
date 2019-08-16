package com.smate.web.fund.agency.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 基金机构列表.
 * 
 * @author lichangwen
 * 
 */

public class FundAgencyDTO implements Serializable {

  private Long id;
  private String des3Id;
  // 机构中文名称
  private String nameZh;
  // 机构英文名称
  private String nameEn;
  // 显示名称
  private String nameView;
  // 机构标识
  private Integer flag;
  // 机构类别CONST_FUND_POSITION表id字段
  private Long type;
  // 类别显示名称
  private String typeView;
  // 国家/地区
  private Long regionId;
  // 地市的父级id
  private Long superRegionId;
  // 国家地区名称
  private String regionName;
  // 机构代码
  private String code;
  // 机构缩写
  private String abbr;
  // 地址
  private String address;
  // 显示地址
  private String viewAddress;
  // 英文地址
  private String enAddress;
  // 机构网址
  private String url;
  // 联系方式
  private String contact;
  // logo图片地址
  private String logoUrl;
  // 单位Id,0为bpo管理的基准数据
  private Long insId;
  private Long psnId;
  private Date createDate;
  // 审核状态：0通过，1拒绝
  private Integer status = 0;
  // 单位名称冗余
  private String insName;
  // 最近使用时间
  private Date useDate;
  // 当前机构的基金数
  private Long fundCount;

  private Integer selectStatus = 0;// 选中状态

  private Long shareCount;

  private Long interestCount;

  private Long awardCount;

  private Integer interested;

  private Integer hasAward;

  public FundAgencyDTO() {
    super();
  }

  public FundAgencyDTO(Long id, String nameZh, String nameEn, String code) {
    super();
    this.id = id;
    this.nameZh = nameZh;
    this.nameEn = nameEn;
    this.code = code;
  }

  public FundAgencyDTO(Long regionId, String nameZh, String nameEn) {
    super();
    this.regionId = regionId;
    this.nameZh = nameZh;
    this.nameEn = nameEn;
  }

  public FundAgencyDTO(String nameZh, String nameEn, String logoUrl, Long regionId) {
    super();
    this.regionId = regionId;
    this.nameZh = nameZh;
    this.nameEn = nameEn;
    this.logoUrl = logoUrl;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public String getNameZh() {
    return nameZh;
  }

  public void setNameZh(String nameZh) {
    this.nameZh = nameZh;
  }

  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  public String getNameView() {
    return nameView;
  }

  public void setNameView(String nameView) {
    this.nameView = nameView;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public Long getType() {
    return type;
  }

  public void setType(Long type) {
    this.type = type;
  }

  public String getTypeView() {
    return typeView;
  }

  public void setTypeView(String typeView) {
    this.typeView = typeView;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public Long getSuperRegionId() {
    return superRegionId;
  }

  public void setSuperRegionId(Long superRegionId) {
    this.superRegionId = superRegionId;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getAbbr() {
    return abbr;
  }

  public void setAbbr(String abbr) {
    this.abbr = abbr;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getViewAddress() {
    return viewAddress;
  }

  public void setViewAddress(String viewAddress) {
    this.viewAddress = viewAddress;
  }

  public String getEnAddress() {
    return enAddress;
  }

  public void setEnAddress(String enAddress) {
    this.enAddress = enAddress;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Date getUseDate() {
    return useDate;
  }

  public void setUseDate(Date useDate) {
    this.useDate = useDate;
  }

  public Long getFundCount() {
    return fundCount;
  }

  public void setFundCount(Long fundCount) {
    this.fundCount = fundCount;
  }

  public Integer getSelectStatus() {
    return selectStatus;
  }

  public void setSelectStatus(Integer selectStatus) {
    this.selectStatus = selectStatus;
  }

  public Long getShareCount() {
    return shareCount;
  }

  public void setShareCount(Long shareCount) {
    this.shareCount = shareCount;
  }

  public Long getInterestCount() {
    return interestCount;
  }

  public void setInterestCount(Long interestCount) {
    this.interestCount = interestCount;
  }

  public Long getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Long awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getInterested() {
    return interested;
  }

  public void setInterested(Integer interested) {
    this.interested = interested;
  }

  public Integer getHasAward() {
    return hasAward;
  }

  public void setHasAward(Integer hasAward) {
    this.hasAward = hasAward;
  }

}
