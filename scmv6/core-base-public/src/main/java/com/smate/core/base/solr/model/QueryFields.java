package com.smate.core.base.solr.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.model.Page;

public class QueryFields implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7762095870982999769L;

  // 语言设置
  public static final String LANGUAGE_ALL = "ALL";
  public static final String LANGUAGE_ZH = "ZH_CN";
  public static final String LANGUAGE_EN = "EN";

  // 按字段排序
  public static final String ORDER_YEAR = "YEAR";
  public static final String ORDER_DEFAULT = "DEFAULT";
  public static final String ORDER_PUBLISHYEAR = "pubYear";// 出版年份
  public static final String ORDER_CITATIONS = "pubCitations";// 引用次数
  public static final String ORDER_READCOUNT = "readCount";// 阅读次数
  public static final String ORDER_DOWNLOADCOUNT = "downLoadCount";// 下载次数

  // 用于异步加载，查询第一次，第二次
  public static final Integer SEQ_1 = 1;
  public static final Integer SEQ_2 = 2;

  // 检索框内容
  private String searchString;
  private Integer pubYear;
  private Integer pubTypeId;
  private Integer patYear;
  private Integer patTypeId;
  private String patTypeIdStr; // 多个专利类型拼接的字符串
  private List<Integer> pubTypeIdList;// 多个成果类型过滤
  private String language;
  private int searchType;// 1-论文；2-专利；
  private String orderBy;
  // 查询顺序，用于异步加载
  private Integer seqQuery;
  // 平台---pc、mobile
  private String platform;
  // 第一次进页面，如果没有查询到结果，提示输入关键字查询
  private Integer firstLoad;

  private String login;
  // 默认赋值false，不然如果为null,赋值给boolean时会报错
  private Boolean isDoi = false;
  private String fromPage;// 来源页面标识 mobileSearchFriend:移动端-联系菜单-发现好友页面
  private List<Long> psnIdList;// 移动端-发现好友-检索与发现人员-需要排除的psnId
  private List<String> insNameList;// 根据关键词检索人员需要排除的单位
  private List<Long> psnRegionIds;
  private boolean needExcludeFriendId; // 是否需要排除好友ID

  private String searchSuggestion;// 检索提示
  private List<Long> excludedPubIds; // 需要排除的PubIds
  private String years;// 年份格式(2016 2017 2012 2013)
  private List<Integer> psnScienceAreaIds;
  private String desPsnId;

  private List<Integer> pubScienceAreaIds; // 成果科技领域
  private String scienceAreaIds; // 成果科技领域
  private Integer timeGap; // 时间标志，具体可看成果推荐处
  private String pubTypes; // 字符串类型的成果类型，
  private String pubYearStr;
  private String searchPsnKey; // 个人关键词检索
  /**
   * "3" : "2014 年以来","1" : "2018 年以来","2" : "2016 年以来","0" : "不限"
   */
  private String pubYearItem;
  private String pubTypeIdStr;
  private String pubCategoryStr;
  private String dbCodeStr;
  private Page Page;
  private String pubDBIds; // 成果收录情况
  private String des3PubIds; // 成果加密ID
  private Set<String> useNames;
  private Set<String> insNames;
  private String name; // 检索人员名字
  private String position;// 检索人员职称 , solr中 title字段。包含职称
  private String insName;// 检索人员机构单位

  private String title;// 检索成果标题
  private String Keywords;// 检索成果关键词
  private String authors;// 检索成果作者
  private String addr;// 提取到的地址或者
  private String md5Author;
  private Integer pubSuperCatgory;// 成果科技领域过滤
  private String pubSuperCategorys;// 多个领域拼接字符串
  private Long regionCode;// 成果地区过滤
  private String[] FacetField;// 分类统计

  public void buildPubYearStr() {
    if ("3".equals(this.pubYearItem)) {
      this.pubYearStr = "2018,2017,2016,2015,2014";
    } else if ("2".equals(this.pubYearItem)) {
      this.pubYearStr = "2018,2017,2016";
    } else if ("1".equals(this.pubYearItem)) {
      this.pubYearStr = "2018";
    } else if ("0".equals(this.pubYearItem)) {
      this.pubYearStr = "";
    }
  }

  public String getPubYearStr() {
    return pubYearStr;
  }

  public void setPubYearStr(String pubYearStr) {
    this.pubYearStr = pubYearStr;
  }

  public String getPubTypeIdStr() {
    return pubTypeIdStr;
  }

  public void setPubTypeIdStr(String pubTypeIdStr) {
    this.pubTypeIdStr = pubTypeIdStr;
  }

  public String getPubCategoryStr() {
    return pubCategoryStr;
  }

  public void setPubCategoryStr(String pubCategoryStr) {
    this.pubCategoryStr = pubCategoryStr;
  }

  public List<Long> getPsnIdList() {
    return psnIdList;
  }

  public void setPsnIdList(List<Long> psnIdList) {
    this.psnIdList = psnIdList;
  }

  public List<String> getInsNameList() {
    return insNameList;
  }

  public void setInsNameList(List<String> insNameList) {
    this.insNameList = insNameList;
  }

  public List<Long> getPsnRegionIds() {
    return psnRegionIds;
  }

  public void setPsnRegionIds(List<Long> psnRegionIds) {
    this.psnRegionIds = psnRegionIds;
  }

  public String getFromPage() {
    return fromPage;
  }

  public void setFromPage(String fromPage) {
    this.fromPage = fromPage;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public Integer getPubTypeId() {
    return pubTypeId;
  }

  public void setPubTypeId(Integer pubTypeId) {
    this.pubTypeId = pubTypeId;
  }

  public String getLanguage() {
    if (StringUtils.isBlank(language)) {
      this.language = LANGUAGE_EN;
    }
    return language;
  }

  public void setLanguage(String language) {
    if (StringUtils.isBlank(language)) {
      this.language = LANGUAGE_EN;
    } else {
      this.language = language;
    }
  }

  public int getSearchType() {
    return searchType;
  }

  public void setSearchType(int searchType) {
    this.searchType = searchType;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    if (StringUtils.isBlank(orderBy)) {
      this.orderBy = ORDER_DEFAULT;
    } else {
      this.orderBy = orderBy;
    }
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

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public Integer getPatYear() {
    return patYear;
  }

  public void setPatYear(Integer patYear) {
    this.patYear = patYear;
  }

  public Integer getPatTypeId() {
    return patTypeId;
  }

  public void setPatTypeId(Integer patTypeId) {
    this.patTypeId = patTypeId;
  }

  public Integer getFirstLoad() {
    return firstLoad;
  }

  public void setFirstLoad(Integer firstLoad) {
    this.firstLoad = firstLoad;
  }

  public Boolean getIsDoi() {
    return isDoi;
  }

  public void setIsDoi(Boolean isDoi) {
    this.isDoi = isDoi;
  }

  public boolean getNeedExcludeFriendId() {
    return needExcludeFriendId;
  }

  public void setNeedExcludeFriendId(boolean needExcludeFriendId) {
    this.needExcludeFriendId = needExcludeFriendId;
  }

  public String getSearchSuggestion() {
    return searchSuggestion;
  }

  public void setSearchSuggestion(String searchSuggestion) {
    this.searchSuggestion = searchSuggestion;
  }

  public List<Long> getExcludedPubIds() {
    return excludedPubIds;
  }

  public void setExcludedPubIds(List<Long> excludedPubIds) {
    this.excludedPubIds = excludedPubIds;
  }

  public List<Integer> getPubTypeIdList() {
    return pubTypeIdList;
  }

  public void setPubTypeIdList(List<Integer> pubTypeIdList) {
    this.pubTypeIdList = pubTypeIdList;
  }

  public String getYears() {
    return years;
  }

  public void setYears(String years) {
    this.years = years;
  }

  public List<Integer> getPsnScienceAreaIds() {
    return psnScienceAreaIds;
  }

  public void setPsnScienceAreaIds(List<Integer> psnScienceAreaIds2) {
    this.psnScienceAreaIds = psnScienceAreaIds2;
  }

  public String getDesPsnId() {
    return desPsnId;
  }

  public void setDesPsnId(String desPsnId) {
    this.desPsnId = desPsnId;
  }

  public List<Integer> getPubScienceAreaIds() {
    return pubScienceAreaIds;
  }

  public void setPubScienceAreaIds(List<Integer> pubScienceAreaIds) {
    this.pubScienceAreaIds = pubScienceAreaIds;
  }

  public String getScienceAreaIds() {
    return scienceAreaIds;
  }

  public void setScienceAreaIds(String scienceAreaIds) {
    this.scienceAreaIds = scienceAreaIds;
  }

  public Integer getTimeGap() {
    return timeGap;
  }

  public void setTimeGap(Integer timeGap) {
    this.timeGap = timeGap;
  }

  public String getPubTypes() {
    return pubTypes;
  }

  public void setPubTypes(String pubTypes) {
    this.pubTypes = pubTypes;
  }

  public Page getPage() {
    return Page;
  }

  public void setPage(Page page) {
    Page = page;
  }

  public String getDbCodeStr() {
    return dbCodeStr;
  }

  public void setDbCodeStr(String dbCodeStr) {
    this.dbCodeStr = dbCodeStr;
  }

  public String getPubDBIds() {
    return pubDBIds;
  }

  public void setPubDBIds(String pubDBIds) {
    this.pubDBIds = pubDBIds;
  }

  public String getDes3PubIds() {
    return des3PubIds;
  }

  public void setDes3PubIds(String des3PubIds) {
    this.des3PubIds = des3PubIds;
  }

  public Set<String> getUseNames() {
    return useNames;
  }

  public void setUseNames(Set<String> useNames) {
    this.useNames = useNames;
  }

  public Set<String> getInsNames() {
    return insNames;
  }

  public void setInsNames(Set<String> insNames) {
    this.insNames = insNames;
  }

  public String getPubYearItem() {
    return pubYearItem;
  }

  public void setPubYearItem(String pubYearItem) {
    this.pubYearItem = pubYearItem;
  }

  public String getSearchPsnKey() {
    return searchPsnKey;
  }

  public void setSearchPsnKey(String searchPsnKey) {
    this.searchPsnKey = searchPsnKey;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getKeywords() {
    return Keywords;
  }

  public void setKeywords(String keywords) {
    Keywords = keywords;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getPatTypeIdStr() {
    return patTypeIdStr;
  }

  public void setPatTypeIdStr(String patTypeIdStr) {
    this.patTypeIdStr = patTypeIdStr;
  }

  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  public String getMd5Author() {
    return md5Author;
  }

  public void setMd5Author(String md5Author) {
    this.md5Author = md5Author;
  }

  public Integer getPubSuperCatgory() {
    return pubSuperCatgory;
  }

  public void setPubSuperCatgory(Integer pubSuperCatgory) {
    this.pubSuperCatgory = pubSuperCatgory;
  }

  public String getPubSuperCategorys() {
    return pubSuperCategorys;
  }

  public void setPubSuperCategorys(String pubSuperCategorys) {
    this.pubSuperCategorys = pubSuperCategorys;
  }

  public Long getRegionCode() {
    return regionCode;
  }

  public void setRegionCode(Long regionCode) {
    this.regionCode = regionCode;
  }

  public String[] getFacetField() {
    return FacetField;
  }

  public void setFacetField(String[] facetField) {
    FacetField = facetField;
  }



}
