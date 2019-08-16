package com.smate.web.v8pub.service.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.psn.RecommendDisciplineKey;
import com.smate.web.v8pub.po.sns.psn.RecommendScienceArea;

/**
 * 成果查询的传输对象
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
public class PubQueryDTO {
  // 用于异步加载，查询第一次，第二次
  public static final Integer SEQ_1 = 1;
  public static final Integer SEQ_2 = 2;

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
   * solr查询的成果数据
   */
  public Map<String, Object> pubInfoMap;

  /**
   * 成果ids
   */
  public List<Long> pubIds = new ArrayList<>();

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
   * 是否查找统计数，默认为true
   */
  public boolean searchCount = true;

  public Date pubUpdateDate; // 成果更新时间

  /**
   * 查询成果的pubId
   */
  public Long searchPubId;
  /**
   * 查询成果的pubIds
   */
  public List<Long> searchPubIdList;

  public List<Integer> permissions;

  public String uuid;

  public String authorNames;// 作者

  public Integer beginPublishYear;// 开始发表年份

  public Integer endPublishYear;// 最后发表年份
  /**
   * 是否查询所有成果 true:查询所有 false:查询未被删除的成果
   */
  public boolean isQueryAll = true;
  private List<RecommendScienceArea> areaList; // 科技领域
  private String scienceAreaIds;
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

  private String searchString;// 检索字符串
  // 默认赋值false，不然如果为null,赋值给boolean时会报错
  private Boolean isDoi = false;
  private String login;// 是否登录
  // 查询顺序，用于异步加载
  private Integer seqQuery;
  private Map<Long, Long> yearMap;
  private Map<Long, Long> pubTypeMap;
  private Map<String, Long> languageMap;
  private Map<String, Date> collectPubDateMap; // 收藏成果时间Map
  private boolean searchDoi = false;// 是否查询Doi
  private String SearchLanguage;// 查询的语言
  public boolean notFulltextAndSortUrl = false;// 是否需要全文和短地址,false需要，true不需要
  private String des3PubId;// 成果id

  private Boolean importGrpMemberPubs = false;// 导入群组成员成果
  // 作为下拉框选中，直接用详细信息选择
  private String suggestPsnName;
  private String suggestPsnId;
  private String suggestInsName;
  private String suggestInsId;
  private String suggestType;
  private String suggestKw;
  private Long pubIndex; // 成果下标序号（在查询结果中是第几个）
  // 查询的prjId
  public Long searchPrjId;

  public Boolean getImportGrpMemberPubs() {
    return importGrpMemberPubs;
  }

  public void setImportGrpMemberPubs(Boolean importGrpMemberPubs) {
    this.importGrpMemberPubs = importGrpMemberPubs;
  }


  public String getDes3SearchPsnId() {
    return des3SearchPsnId;
  }

  public void setDes3SearchPsnId(String des3SearchPsnId) {
    this.des3SearchPsnId = des3SearchPsnId;
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

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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
    if (CollectionUtils.isEmpty(pubList)) {
      return new ArrayList<>();
    }
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

  public Map<String, Object> getPubInfoMap() {
    return pubInfoMap;
  }

  public void setPubInfoMap(Map<String, Object> pubInfoMap) {
    this.pubInfoMap = pubInfoMap;
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

  public void setTotalPages(Long totalPages) {
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
    if (importGrpMemberPubs) {
      return false;
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
    List<Long> psnIds = new ArrayList<>();
    // psnId 可能为integer 类型
    if (searchPsnIdList != null && searchPsnIdList.size() > 0) {
      for (int i = 0; i < searchPsnIdList.size(); i++) {
        Object object = searchPsnIdList.get(i);
        psnIds.add(NumberUtils.toLong(object.toString()));
      }
      searchPsnIdList = psnIds;
    }
    return searchPsnIdList;
  }

  public void setSearchPsnIdList(List<Long> searchPsnIdList) {
    this.searchPsnIdList = searchPsnIdList;
  }

  public List<Long> getExcludePsnIdList() {
    List<Long> pubIds = new ArrayList<>();
    // pubIds 可能为integer 类型
    if (excludePsnIdList != null && excludePsnIdList.size() > 0) {
      for (int i = 0; i < excludePsnIdList.size(); i++) {
        Object object = excludePsnIdList.get(i);
        pubIds.add(NumberUtils.toLong(object.toString()));
      }
      excludePsnIdList = pubIds;
    }
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
    List<Long> pubIds = new ArrayList<>();
    // pubId 可能为integer 类型
    if (searchPubIdList != null && searchPubIdList.size() > 0) {
      for (int i = 0; i < searchPubIdList.size(); i++) {
        Object object = searchPubIdList.get(i);
        pubIds.add(NumberUtils.toLong(object.toString()));
      }
      searchPubIdList = pubIds;
    }
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

  public boolean getIsQueryAll() {
    return isQueryAll;
  }

  public void setIsQueryAll(boolean isQueryAll) {
    this.isQueryAll = isQueryAll;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public Boolean getIsDoi() {
    return isDoi;
  }

  public void setIsDoi(Boolean isDoi) {
    this.isDoi = isDoi;
  }

  public Integer getSeqQuery() {
    return seqQuery;
  }

  public void setSeqQuery(Integer seqQuery) {
    if (seqQuery == null) {
      this.seqQuery = SEQ_1;
    } else {
      this.seqQuery = seqQuery;
    }
  }

  public Integer getSearchPubTypeId() {
    return SearchPubTypeId;
  }

  public void setSearchPubTypeId(Integer searchPubTypeId) {
    SearchPubTypeId = searchPubTypeId;
  }

  public Map<Long, Long> getYearMap() {
    return yearMap;
  }

  public void setYearMap(Map<Long, Long> yearMap) {
    this.yearMap = yearMap;
  }

  public Map<Long, Long> getPubTypeMap() {
    return pubTypeMap;
  }

  public void setPubTypeMap(Map<Long, Long> pubTypeMap) {
    this.pubTypeMap = pubTypeMap;
  }

  public Map<String, Long> getLanguageMap() {
    return languageMap;
  }

  public void setLanguageMap(Map<String, Long> languageMap) {
    this.languageMap = languageMap;
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

  public Map<String, Date> getCollectPubDateMap() {
    return collectPubDateMap;
  }

  public void setCollectPubDateMap(Map<String, Date> collectPubDateMap) {
    this.collectPubDateMap = collectPubDateMap;
  }

  public boolean isSearchDoi() {
    return searchDoi;
  }

  public void setSearchDoi(boolean searchDoi) {
    this.searchDoi = searchDoi;
  }

  public boolean isNotFulltextAndSortUrl() {
    return notFulltextAndSortUrl;
  }

  public void setNotFulltextAndSortUrl(boolean notFulltextAndSortUrl) {
    this.notFulltextAndSortUrl = notFulltextAndSortUrl;
  }

  public String getScienceAreaIds() {
    return scienceAreaIds;
  }

  public void setScienceAreaIds(String scienceAreaIds) {
    this.scienceAreaIds = scienceAreaIds;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public String getSearchLanguage() {
    return SearchLanguage;
  }

  public void setSearchLanguage(String searchLanguage) {
    SearchLanguage = searchLanguage;
  }

  public String getSuggestPsnName() {
    return suggestPsnName;
  }

  public void setSuggestPsnName(String suggestPsnName) {
    this.suggestPsnName = suggestPsnName;
  }

  public String getSuggestPsnId() {
    return suggestPsnId;
  }

  public void setSuggestPsnId(String suggestPsnId) {
    this.suggestPsnId = suggestPsnId;
  }

  public String getSuggestInsName() {
    return suggestInsName;
  }

  public void setSuggestInsName(String suggestInsName) {
    this.suggestInsName = suggestInsName;
  }

  public String getSuggestType() {
    return suggestType;
  }

  public void setSuggestType(String suggestType) {
    this.suggestType = suggestType;
  }

  public String getSuggestKw() {
    return suggestKw;
  }

  public void setSuggestKw(String suggestKw) {
    this.suggestKw = suggestKw;
  }

  public String getSuggestInsId() {
    return suggestInsId;
  }

  public void setSuggestInsId(String suggestInsId) {
    this.suggestInsId = suggestInsId;
  }

  public Long getPubIndex() {
    return pubIndex;
  }

  public void setPubIndex(Long pubIndex) {
    this.pubIndex = pubIndex;
  }

  public Long getSearchPrjId() {
    return searchPrjId;
  }

  public void setSearchPrjId(Long searchPrjId) {
    this.searchPrjId = searchPrjId;
  }


}
