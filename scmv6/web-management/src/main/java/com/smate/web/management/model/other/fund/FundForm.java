package com.smate.web.management.model.other.fund;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * SIE单位基金资助机构类别检索条件.
 * 
 * @author lichangwen
 * 
 */
public class FundForm implements Serializable {
  private static final long serialVersionUID = 5349977916118226573L;
  // 单位id
  private Long insId;
  // 人员PsnId
  private Long psnId;
  // 查询key
  private String searchKey;
  // 机构类型
  private String agencyTypeId;
  // 机构所在地区
  private String agencyRegionId;
  // 机构名称
  private String agencyName;
  // 类别名称
  private String categoryName;
  // 领域
  private String catDis;
  // 开始日期:默认大于当前日期
  private String startTime;
  private Date startDate;
  // 韯止日期:默认小于三月后
  private String endTime;
  private Date endDate;
  // 是否显示过期，1不显示已经过期的，否则显示
  private Integer expired;
  // 是否是页面检索:0点击菜单查询，1：点击页面检索查询
  private int isSearch;
  // 语言类别：0中文，1英文
  private Integer language;
  // 截止日期：1=1个月，2=2个月，3=3个月
  private Integer newMonth;
  private String id;
  private String domain;
  // 机构id
  private String agencyId;
  // 年份
  private Long year;
  private String agencyOrder;// 机构排序
  private String categoryOrder;// 类别排序
  private String updateDateOrder;// 更新日期排序
  private String orderType;// 根据哪个类型排序
  private String orderTypeBy;// 排序升降
  private String leftMenuType;// 左边栏菜单类型
  private String leftMenuId;// 左边栏菜单id
  private String searchAgency;// 高级检索 资助机构
  private String searchScheme;// 高级检索 机构类别
  private String searchStartDate;// 高级检索 开始时间
  private String searchEndDate;// 高级检索 结束时间
  private String regionId;// 高级检索 地区id
  private String searchCounId;// 高级检索 机构 国家
  private String searchProId;// 高级检索 机构 省
  private String searchCityId;// 高级检索 机构 市
  private String searchType;// 高级检索 机构类型
  private String searchTypeRegionId;// 高级检索 机构类型省市

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

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getAgencyTypeId() {
    return agencyTypeId;
  }

  public void setAgencyTypeId(String agencyTypeId) {
    this.agencyTypeId = agencyTypeId;
  }

  public String getAgencyRegionId() {
    return agencyRegionId;
  }

  public void setAgencyRegionId(String agencyRegionId) {
    this.agencyRegionId = agencyRegionId;
  }

  public String getAgencyName() {
    return agencyName;
  }

  public void setAgencyName(String agencyName) {
    this.agencyName = agencyName;
  }

  public String getCatDis() {
    return catDis;
  }

  public void setCatDis(String catDis) {
    this.catDis = catDis;
  }

  public Date getStartDate() {
    if (StringUtils.isNotBlank(this.startTime)) {
      try {
        startDate = new SimpleDateFormat("yyyy/MM/dd").parse(this.startTime);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    if (StringUtils.isNotBlank(this.endTime)) {
      try {
        endDate = new SimpleDateFormat("yyyy/MM/dd").parse(this.endTime);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Integer getExpired() {
    return expired;
  }

  public void setExpired(Integer expired) {
    this.expired = expired;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public int getIsSearch() {
    return isSearch;
  }

  public void setIsSearch(int isSearch) {
    this.isSearch = isSearch;
  }

  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  public Integer getNewMonth() {
    return newMonth;
  }

  public void setNewMonth(Integer newMonth) {
    this.newMonth = newMonth;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(String agencyId) {
    this.agencyId = agencyId;
  }

  public Long getYear() {
    return year;
  }

  public void setYear(Long year) {
    this.year = year;
  }

  public String getAgencyOrder() {
    return agencyOrder;
  }

  public void setAgencyOrder(String agencyOrder) {
    this.agencyOrder = agencyOrder;
  }

  public String getCategoryOrder() {
    return categoryOrder;
  }

  public void setCategoryOrder(String categoryOrder) {
    this.categoryOrder = categoryOrder;
  }

  public String getUpdateDateOrder() {
    return updateDateOrder;
  }

  public void setUpdateDateOrder(String updateDateOrder) {
    this.updateDateOrder = updateDateOrder;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getOrderTypeBy() {
    return orderTypeBy;
  }

  public void setOrderTypeBy(String orderTypeBy) {
    this.orderTypeBy = orderTypeBy;
  }

  public String getLeftMenuType() {
    return leftMenuType;
  }

  public void setLeftMenuType(String leftMenuType) {
    this.leftMenuType = leftMenuType;
  }

  public String getLeftMenuId() {
    return leftMenuId;
  }

  public void setLeftMenuId(String leftMenuId) {
    this.leftMenuId = leftMenuId;
  }

  public String getSearchAgency() {
    return searchAgency;
  }

  public void setSearchAgency(String searchAgency) {
    this.searchAgency = searchAgency;
  }

  public String getSearchScheme() {
    return searchScheme;
  }

  public void setSearchScheme(String searchScheme) {
    this.searchScheme = searchScheme;
  }

  public String getSearchStartDate() {
    return searchStartDate;
  }

  public void setSearchStartDate(String searchStartDate) {
    this.searchStartDate = searchStartDate;
  }

  public String getSearchEndDate() {
    return searchEndDate;
  }

  public void setSearchEndDate(String searchEndDate) {
    this.searchEndDate = searchEndDate;
  }

  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public String getSearchProId() {
    return searchProId;
  }

  public void setSearchProId(String searchProId) {
    this.searchProId = searchProId;
  }

  public String getSearchCityId() {
    return searchCityId;
  }

  public void setSearchCityId(String searchCityId) {
    this.searchCityId = searchCityId;
  }

  public String getSearchType() {
    return searchType;
  }

  public void setSearchType(String searchType) {
    this.searchType = searchType;
  }

  public String getSearchTypeRegionId() {
    return searchTypeRegionId;
  }

  public void setSearchTypeRegionId(String searchTypeRegionId) {
    this.searchTypeRegionId = searchTypeRegionId;
  }

  public String getSearchCounId() {
    return searchCounId;
  }

  public void setSearchCounId(String searchCounId) {
    this.searchCounId = searchCounId;
  }



}
