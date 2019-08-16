package com.smate.core.base.pub.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.pub.recommend.model.ConstPubType;
import com.smate.core.base.pub.recommend.model.RecommendDisciplineKey;
import com.smate.core.base.pub.recommend.model.RecommendScienceArea;
import com.smate.core.base.utils.security.Des3Utils;

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
   * 查找本人的成果，默认为true
   */
  public boolean searchSelfPub = true;
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
   * 人主页-成果认领-查看全部 1:是 0:否
   */
  public Integer isAll = 0;
  /**
   * 是否分页查询 1:否0:是
   */
  public Integer isPage = 0;
  /**
   * 成果认领 操作了的成果数
   */
  public Integer confirmCount = 0;

  /**
   * 查询成果的pubId
   */
  public Long searchPubId;

  public Date pubUpdateDate; // 成果更新时间
  /**
   * 成果id集合
   */
  public List<Long> searchPubIdList;

  public List<Integer> permissions;

  public String uuid;

  public String authorNames;// 作者

  public Integer beginPublishYear;// 开始发表年份

  public Integer endPublishYear;// 最后发表年份

  private List<RecommendScienceArea> areaList; // 科技领域
  private List<RecommendDisciplineKey> keyList; // 关键词
  private Map<String, String> pubYearMap; // 论文出版年份
  private List<ConstPubType> pubTypeList; // 成果类型
  private String DefultArea;
  private String defultKeyJson; // 默认关键字
  private String searchArea;// 查询的科技领域
  private String searchPsnKey;// 查询的关键字
  private Integer searchPubYear;// 查询的出版年份
  private String searchPubType;// 查询的成果类型
  private Integer SearchPubTypeId;
  private Integer SearchPatTypeId;
  private Integer searchPatYear;// 查询的出版年份
  private String searchLanguage;// 查询的语言
  private boolean searchDoi = false;// 是否查询Doi
  /**
   * 是否查询所有成果 true:查询所有 false:查询未被删除的成果
   */
  public boolean isQueryAll = true;
  /**
   * 当前用户id
   */
  public Long psnId;

  public boolean notFulltextAndSortUrl = false;// 是否需要全文和短地址,false需要，true不需要
  /**
   * 查询的prjId
   */
  public Long searchPrjId;

  public String getDes3SearchPsnId() {
    return des3SearchPsnId;
  }

  public void setDes3SearchPsnId(String des3SearchPsnId) {
    this.des3SearchPsnId = des3SearchPsnId;
  }

  public Long getSearchPsnId() {
    if ((searchPsnId == null || searchPsnId == 0L) || StringUtils.isNotBlank(des3SearchPsnId)) {
      String des3 = Des3Utils.decodeFromDes3(des3SearchPsnId);
      searchPsnId = NumberUtils.toLong(des3);
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
    if ((searchGrpId == null || searchGrpId == 0L) || StringUtils.isNotBlank(des3SearchGrpId)) {
      String des3 = Des3Utils.decodeFromDes3(des3SearchGrpId);
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

  public boolean isSearchSelfPub() {
    return searchSelfPub;
  }

  public void setSearchSelfPub(boolean searchSelfPub) {
    this.searchSelfPub = searchSelfPub;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
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

  public Integer getIsPage() {
    return isPage;
  }

  public void setIsPage(Integer isPage) {
    this.isPage = isPage;
  }

  public List<Long> getSearchPubIdList() {
    return searchPubIdList;
  }

  public void setSearchPubIdList(List<Long> searchPubIdList) {
    this.searchPubIdList = searchPubIdList;
  }

  public List<Integer> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<Integer> permissions) {
    this.permissions = permissions;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public Integer getBeginPublishYear() {
    return beginPublishYear;
  }

  public void setBeginPublishYear(Integer beginPublishYear) {
    this.beginPublishYear = beginPublishYear;
  }

  public Integer getEndPublishYear() {
    return endPublishYear;
  }

  public void setEndPublishYear(Integer endPublishYear) {
    this.endPublishYear = endPublishYear;
  }

  public boolean isQueryAll() {
    return isQueryAll;
  }

  public void setQueryAll(boolean isQueryAll) {
    this.isQueryAll = isQueryAll;
  }

  public boolean isSelf() {
    return self;
  }

  public void setSelf(boolean self) {
    this.self = self;
  }

  public List<RecommendScienceArea> getAreaList() {
    return areaList;
  }

  public void setAreaList(List<RecommendScienceArea> areaList) {
    this.areaList = areaList;
  }

  public List<RecommendDisciplineKey> getKeyList() {
    return keyList;
  }

  public void setKeyList(List<RecommendDisciplineKey> keyList) {
    this.keyList = keyList;
  }

  public Map<String, String> getPubYearMap() {
    return pubYearMap;
  }

  public void setPubYearMap(Map<String, String> pubYearMap) {
    this.pubYearMap = pubYearMap;
  }

  public List<ConstPubType> getPubTypeList() {
    return pubTypeList;
  }

  public void setPubTypeList(List<ConstPubType> pubTypeList) {
    this.pubTypeList = pubTypeList;
  }

  public String getDefultArea() {
    return DefultArea;
  }

  public void setDefultArea(String defultArea) {
    DefultArea = defultArea;
  }

  public String getDefultKeyJson() {
    return defultKeyJson;
  }

  public void setDefultKeyJson(String defultKeyJson) {
    this.defultKeyJson = defultKeyJson;
  }

  public String getSearchArea() {
    return searchArea;
  }

  public void setSearchArea(String searchArea) {
    this.searchArea = searchArea;
  }

  public String getSearchPsnKey() {
    return searchPsnKey;
  }

  public void setSearchPsnKey(String searchPsnKey) {
    this.searchPsnKey = searchPsnKey;
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

  public Integer getSearchPubTypeId() {
    return SearchPubTypeId;
  }

  public void setSearchPubTypeId(Integer searchPubTypeId) {
    SearchPubTypeId = searchPubTypeId;
  }

  public Integer getSearchPatTypeId() {
    return SearchPatTypeId;
  }

  public void setSearchPatTypeId(Integer searchPatTypeId) {
    SearchPatTypeId = searchPatTypeId;
  }

  public Integer getSearchPatYear() {
    return searchPatYear;
  }

  public void setSearchPatYear(Integer searchPatYear) {
    this.searchPatYear = searchPatYear;
  }

  public boolean isSearchDoi() {
    return searchDoi;
  }

  public void setSearchDoi(boolean searchDoi) {
    this.searchDoi = searchDoi;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public boolean isNotFulltextAndSortUrl() {
    return notFulltextAndSortUrl;
  }

  public void setNotFulltextAndSortUrl(boolean notFulltextAndSortUrl) {
    this.notFulltextAndSortUrl = notFulltextAndSortUrl;
  }

  public String getSearchLanguage() {
    return searchLanguage;
  }

  public void setSearchLanguage(String searchLanguage) {
    this.searchLanguage = searchLanguage;
  }

  public Long getSearchPrjId() {
    return searchPrjId;
  }

  public void setSearchPrjId(Long searchPrjId) {
    this.searchPrjId = searchPrjId;
  }


}
