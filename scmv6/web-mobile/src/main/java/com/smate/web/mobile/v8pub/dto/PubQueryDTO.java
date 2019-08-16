package com.smate.web.mobile.v8pub.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.po.PubPO;
import com.smate.web.mobile.v8pub.po.PubPdwhPO;
import com.smate.web.mobile.v8pub.po.PubSnsPO;

/**
 * 成果查询的传输对象
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
public class PubQueryDTO {

  /**
   * 查询的成员psnId
   */
  public String des3SearchPsnId;
  /**
   * 当前用户id
   */
  public Long psnId;

  public Long searchPsnId;
  /**
   * 查询成果的人员list集合
   */
  public List<Long> searchPsnIdList;
  /**
   * 查询成果排除的成果id
   */
  public List<Long> excludePsnIdList;
  /**
   * open系统查询公开成果 默认为false，查询所有成果
   */
  public boolean openPub = false;
  /**
   * 查询的群组ID
   */
  public String des3SearchGrpId;
  public String des3GrpId;
  public Long searchGrpId;
  /**
   * 查询的群组成员ID
   */
  public String des3SearchGrpMemberId;
  public Long searchGrpMemberId;
  /**
   * 查询项目成果 ， 项目群组要传递
   */
  public boolean searchPrjPub = false;
  /**
   * 查询文献成果 项目群组要传递
   */
  public boolean searchRefPub = false;

  /**
   * 是否是本人 当前用户和和searchPsnId比较，相同为true
   */
  public boolean self = false;

  /**
   * 查询成果列表的服务类型，例如群组成果列表服务，等
   */
  public String serviceType = "";

  /**
   * 查询关键词
   */
  public String searchKey = "";
  /**
   * 成果类型 多个类型逗号隔离 例如： 4,3,5
   */
  public String pubType = "";
  /**
   * 收录列表，多个逗号隔离 例如：ei,sci
   */
  public String includeType = "";
  /**
   * 发表年份 多个逗号隔离 例如： 2015,2014
   */
  public String publishYear = "";
  /**
   * 排序的字段 citations:引用次数 title：标题 publishDate：发表日期 gmtModified ： 更新时间 下面是群组排序说明： createDate ： 创建时间
   * 默认排序 publishDate ： 最新发表 citations ： 最多引用排序
   */
  public String orderBy = "";
  /**
   * 获取第几页数据，默认第一页
   */
  public Integer pageNo = 1;

  /**
   * 数据库 获取的第一条数据
   */
  public Integer first = 0;
  /**
   * 每页数据的条数，默认10条
   */
  public Integer pageSize = 10;
  /**
   * 总条数
   */
  public Long totalCount = 0L;
  /**
   * 总条数
   */
  public Long totalPages = 0L;

  /**
   * 数据库查询的成果数据
   */
  public List<PubPO> pubList;
  /**
   * 数据库查询的成果数据
   */
  public List<PubSnsPO> pubSnsList;
  /**
   * 数据库查询的成果数据
   */
  public List<PubPdwhPO> pubPdwhList;
  /**
   * 成果ids
   */
  public List<Long> pubIds = new ArrayList<>();

  /**
   * 人主页-成果认领-查看全部 1:是 0:否
   */
  public Integer isAll = 0;
  private boolean isQueryAll = true;
  /**
   * 成果认领 操作了的成果数
   */
  public Integer confirmCount = 0;
  /**
   * 是否查找统计数，默认为true
   */
  public boolean searchCount = true;

  public Date pubUpdateDate; // 成果更新时间
  private String searchArea;// 查询的科技领域
  private Integer searchPubYear;// 查询的出版年份
  private String searchPubType;// 查询的成果类型
  private String searchString;// 检索字符串
  private String searchLanguage;// 语言
  /**
   * 查询成果的pubId
   */
  public Long searchPubId;

  public String getDes3SearchPsnId() {
    return des3SearchPsnId;
  }

  public void setDes3SearchPsnId(String des3SearchPsnId) {
    this.des3SearchPsnId = des3SearchPsnId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getSearchPsnId() {
    if ((searchPsnId == null || searchPsnId == 0L) && StringUtils.isNotBlank(des3SearchPsnId)) {
      String des3 = Des3Utils.decodeFromDes3(des3SearchPsnId);
      searchPsnId = NumberUtils.toLong(des3);
    } else if (searchPsnId == null || searchPsnId == 0L) {
      searchPsnId = SecurityUtils.getCurrentUserId();
    }
    return searchPsnId;
  }

  public void setSearchPsnId(Long searchPsnId) {
    this.searchPsnId = searchPsnId;
  }

  public String getDes3SearchGrpId() {
    return des3SearchGrpId;
  }

  public void setDes3SearchGrpId(String des3SearchGrpId) {
    this.des3SearchGrpId = des3SearchGrpId;
  }

  public Long getSearchGrpId() {
    if ((searchGrpId == null || searchGrpId == 0L) && StringUtils.isNotBlank(des3SearchGrpId)) {
      String des3 = Des3Utils.decodeFromDes3(des3SearchGrpId);
      searchGrpId = NumberUtils.toLong(des3);
    } else if ((searchGrpId == null || searchGrpId == 0L) && StringUtils.isNotBlank(des3GrpId)) {
      String des3 = Des3Utils.decodeFromDes3(des3GrpId);
      searchGrpId = NumberUtils.toLong(des3);
    }
    return searchGrpId;
  }

  public void setSearchGrpId(Long searchGrpId) {
    this.searchGrpId = searchGrpId;
  }

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public String getIncludeType() {
    return includeType;
  }

  public void setIncludeType(String includeType) {
    this.includeType = includeType;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public List<PubPO> getPubList() {
    return pubList;
  }

  public List<PubSnsPO> getPubSnsList() {
    return pubSnsList;
  }

  public void setPubSnsList(List<PubSnsPO> pubSnsList) {
    this.pubSnsList = pubSnsList;
  }

  public List<PubPdwhPO> getPubPdwhList() {
    return pubPdwhList;
  }

  public void setPubPdwhList(List<PubPdwhPO> pubPdwhList) {
    this.pubPdwhList = pubPdwhList;
  }

  public void setPubList(List<PubPO> pubList) {
    this.pubList = pubList;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public Long getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Long TotalPages) {
    this.totalPages = totalPages;
  }

  public Integer getFirst() {
    if (first == null || first < 0) {
      first = 0;
    }
    if (pageNo != null && pageNo > 1) {
      first = (pageNo - 1) * pageSize;
    }
    return first;
  }

  public void setFirst(Integer first) {
    this.first = first;
  }

  public String getDes3SearchGrpMemberId() {
    return des3SearchGrpMemberId;
  }

  public void setDes3SearchGrpMemberId(String des3SearchGrpMemberId) {
    this.des3SearchGrpMemberId = des3SearchGrpMemberId;
  }

  public Long getSearchGrpMemberId() {
    if ((searchGrpMemberId == null || searchGrpMemberId == 0L) && StringUtils.isNotBlank(des3SearchGrpMemberId)) {
      searchGrpMemberId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3SearchGrpMemberId));
    }
    return searchGrpMemberId;
  }

  public void setSearchGrpMemberId(Long searchGrpMemberId) {
    this.searchGrpMemberId = searchGrpMemberId;
  }

  public boolean isSearchPrjPub() {
    return searchPrjPub;
  }

  public void setSearchPrjPub(boolean searchPrjPub) {
    this.searchPrjPub = searchPrjPub;
  }

  public boolean isSearchRefPub() {
    return searchRefPub;
  }

  public void setSearchRefPub(boolean searchRefPub) {
    this.searchRefPub = searchRefPub;
  }

  public Integer getIsAll() {
    return isAll;
  }

  public void setIsAll(Integer isAll) {
    this.isAll = isAll;
  }

  public Integer getConfirmCount() {
    return confirmCount;
  }

  public void setConfirmCount(Integer confirmCount) {
    this.confirmCount = confirmCount;
  }

  public List<Long> getPubIds() {
    return pubIds;
  }

  public void setPubIds(List<Long> pubIds) {
    this.pubIds = pubIds;
  }

  public boolean isSelf() {
    if (self) {
      return self;
    }
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    if (currentPsnId == null || currentPsnId == 0L) {
      self = false;
    } else if (searchPsnId == null || searchPsnId == 0L) {
      self = false;
    } else if (searchPsnId.longValue() == currentPsnId.longValue()) {
      self = true;
    }
    return self;
  }

  public void setSelf(boolean self) {
    this.self = self;
  }

  public boolean isSearchCount() {
    return searchCount;
  }

  public void setSearchCount(boolean searchCount) {
    this.searchCount = searchCount;
  }

  public Long getSearchPubId() {
    return searchPubId;
  }

  public void setSearchPubId(Long searchPubId) {
    this.searchPubId = searchPubId;
  }

  public Date getPubUpdateDate() {
    return pubUpdateDate;
  }

  public void setPubUpdateDate(Date pubUpdateDate) {
    this.pubUpdateDate = pubUpdateDate;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public List<Long> getSearchPsnIdList() {
    return searchPsnIdList;
  }

  public void setSearchPsnIdList(List<Long> searchPsnIdList) {
    this.searchPsnIdList = searchPsnIdList;
  }

  public List<Long> getExcludePsnIdList() {
    return excludePsnIdList;
  }

  public void setExcludePsnIdList(List<Long> excludePsnIdList) {
    this.excludePsnIdList = excludePsnIdList;
  }

  public boolean isOpenPub() {
    return openPub;
  }

  public void setOpenPub(boolean openPub) {
    this.openPub = openPub;
  }

  public String getSearchArea() {
    return searchArea;
  }

  public void setSearchArea(String searchArea) {
    this.searchArea = searchArea;
  }

  public Integer getSearchPubYear() {
    return searchPubYear;
  }

  public void setSearchPubYear(Integer searchPubYear) {
    this.searchPubYear = searchPubYear;
  }

  public String getSearchPubType() {
    return searchPubType;
  }

  public void setSearchPubType(String searchPubType) {
    this.searchPubType = searchPubType;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public String getSearchLanguage() {
    return searchLanguage;
  }

  public void setSearchLanguage(String searchLanguage) {
    this.searchLanguage = searchLanguage;
  }

  public boolean getIsQueryAll() {
    return isQueryAll;
  }

  public void setIsQueryAll(boolean isQueryAll) {
    this.isQueryAll = isQueryAll;
  }


}
