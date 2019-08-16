package com.smate.web.mobile.v8pub.vo.pdwh;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.smate.core.base.pub.recommend.model.ConstPubType;
import com.smate.core.base.pub.recommend.model.RecommendDisciplineKey;
import com.smate.core.base.pub.recommend.model.RecommendScienceArea;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.constant.AppForm;
import com.smate.core.base.utils.model.Page;


public class PubRecommendVO {
  private String des3PsnId; // 加密的人员ID
  private Long psnId; // 人员ID
  private Page<PubInfo> page = new Page<PubInfo>(); // 分页查询用
  private String regionNames; // 地区名称
  private Integer timeFlag; // 时间， 1：一周之内，2：一个月之内， 3：一年之内
  // private Integer pageNum; // 页数
  // private Integer pageSize = 5; // 每页显示记录数
  // private Integer totalPages; // 总页数
  private int firstIn = 0; // 是否第一次加载，true：第一次加载
  private boolean fromMobile = false; // 是否是移动端的
  // private List<PubRecommendInfo> infoList; // 页面成果显示的信息

  private List<RecommendScienceArea> areaList; // 科技领域
  private List<RecommendDisciplineKey> keyList; // 关键词
  private Map<String, String> pubYearMap; // 论文出版年份
  private List<ConstPubType> pubTypeList; // 成果类型

  private String defultArea; // 默认科技领域
  private String defultKeyJson = ""; // 默认关键字
  private String defultPubYear; // 默认出版年份
  private String defultPubType; // 默认成果类型

  private String searchArea;// 查询的科技领域
  private String searchPsnKey = "";// 查询的关键字
  private String searchPubYear;// 查询的出版年份
  private String searchPubType;// 查询的成果类型
  private String pubRecommendShow;// 是否显示的是成果推荐

  private String addPsnAreaCode;// 添加的个人的科技领域
  private String addPsnAreaCodeList;// 添加的个人的科技领域
  private String addPsnKeyWord;// 添加的个人关键词
  private String deletePsnAreaCodeList;// 删除的个人的科技领域
  private String deletePsnAreaCode;// 删除的个人的科技领域
  private String deletePsnKeyWord;// 删除的个人关键词
  private Long keyId;// 关键词id（CONST_KEY_DISCODES）
  private String fullTextPubs;// 构建全文图片的加密成果id的json字符串
  private AppForm appForm;
  private List<Map<String, Object>> allScienceAreaList; // 全部的学科
  private String pubDBIds; // 论文收录情况
  private String scienceAreaIds; // 科技领域ID

  private String searchKey;// 填充关键词
  private int keywordsSize;// 显示关键词的条数
  private int currentYear; // 现在的年份
  private String searchString; // 查询的字符串

  private String des3PubId;
  private Long pubId;
  private String pageNo;
  private Integer nowYear;

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getRegionNames() {
    return regionNames;
  }

  public void setRegionNames(String regionNames) {
    this.regionNames = regionNames;
  }

  public Integer getTimeFlag() {
    return timeFlag;
  }

  public void setTimeFlag(Integer timeFlag) {
    this.timeFlag = timeFlag;
  }

  public int getFirstIn() {
    return firstIn;
  }

  public void setFirstIn(int firstIn) {
    this.firstIn = firstIn;
  }

  public boolean isFromMobile() {
    return fromMobile;
  }

  public void setFromMobile(boolean fromMobile) {
    this.fromMobile = fromMobile;
  }

  public Map<String, String> getPubYearMap() {
    return pubYearMap;
  }

  public void setPubYearMap(Map<String, String> pubYearMap) {
    this.pubYearMap = pubYearMap;
  }


  public String getDefultArea() {
    return defultArea;
  }

  public void setDefultArea(String defultArea) {
    this.defultArea = defultArea;
  }

  public String getDefultKeyJson() {
    return defultKeyJson;
  }

  public void setDefultKeyJson(String defultKeyJson) {
    this.defultKeyJson = defultKeyJson;
  }

  public String getDefultPubYear() {
    return defultPubYear;
  }

  public void setDefultPubYear(String defultPubYear) {
    this.defultPubYear = defultPubYear;
  }

  public String getDefultPubType() {
    return defultPubType;
  }

  public void setDefultPubType(String defultPubType) {
    this.defultPubType = defultPubType;
  }

  public String getSearchArea() {
    return searchArea;
  }

  public void setSearchArea(String searchArea) {
    this.searchArea = searchArea;
  }

  public String getSearchPubYear() {
    return searchPubYear;
  }

  public void setSearchPubYear(String searchPubYear) {
    this.searchPubYear = searchPubYear;
  }

  public String getSearchPubType() {
    return searchPubType;
  }

  public void setSearchPubType(String searchPubType) {
    this.searchPubType = searchPubType;
  }

  public String getSearchPsnKey() {
    return searchPsnKey;
  }

  public void setSearchPsnKey(String searchPsnKey) {
    this.searchPsnKey = searchPsnKey;
  }


  public String getPubRecommendShow() {
    return pubRecommendShow;
  }

  public void setPubRecommendShow(String pubRecommendShow) {
    this.pubRecommendShow = pubRecommendShow;
  }

  public String getAddPsnAreaCode() {
    return addPsnAreaCode;
  }

  public void setAddPsnAreaCode(String addPsnAreaCode) {
    this.addPsnAreaCode = addPsnAreaCode;
  }

  public String getAddPsnKeyWord() {
    return addPsnKeyWord;
  }

  public void setAddPsnKeyWord(String addPsnKeyWord) {
    this.addPsnKeyWord = addPsnKeyWord;
  }

  public String getDeletePsnAreaCode() {
    return deletePsnAreaCode;
  }

  public void setDeletePsnAreaCode(String deletePsnAreaCode) {
    this.deletePsnAreaCode = deletePsnAreaCode;
  }

  public String getDeletePsnKeyWord() {
    return deletePsnKeyWord;
  }

  public void setDeletePsnKeyWord(String deletePsnKeyWord) {
    this.deletePsnKeyWord = deletePsnKeyWord;
  }

  public Long getKeyId() {
    return keyId;
  }

  public void setKeyId(Long keyId) {
    this.keyId = keyId;
  }

  public String getFullTextPubs() {
    return fullTextPubs;
  }

  public void setFullTextPubs(String fullTextPubs) {
    this.fullTextPubs = fullTextPubs;
  }

  public AppForm getAppForm() {
    return appForm;
  }

  public void setAppForm(AppForm appForm) {
    this.appForm = appForm;
  }

  public String getDeletePsnAreaCodeList() {
    return deletePsnAreaCodeList;
  }

  public void setDeletePsnAreaCodeList(String deletePsnAreaCodeList) {
    this.deletePsnAreaCodeList = deletePsnAreaCodeList;
  }

  public String getAddPsnAreaCodeList() {
    return addPsnAreaCodeList;
  }

  public void setAddPsnAreaCodeList(String addPsnAreaCodeList) {
    this.addPsnAreaCodeList = addPsnAreaCodeList;
  }

  public List<Map<String, Object>> getAllScienceAreaList() {
    return allScienceAreaList;
  }

  public void setAllScienceAreaList(List<Map<String, Object>> allScienceAreaList) {
    this.allScienceAreaList = allScienceAreaList;
  }

  public String getPubDBIds() {
    return pubDBIds;
  }

  public void setPubDBIds(String pubDBIds) {
    this.pubDBIds = pubDBIds;
  }

  public String getScienceAreaIds() {
    return scienceAreaIds;
  }

  public void setScienceAreaIds(String scienceAreaIds) {
    this.scienceAreaIds = scienceAreaIds;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public int getKeywordsSize() {
    return keywordsSize;
  }

  public void setKeywordsSize(int keywordsSize) {
    this.keywordsSize = keywordsSize;
  }

  public int getCurrentYear() {
    return currentYear;
  }

  public void setCurrentYear(int currentYear) {
    this.currentYear = currentYear;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public Page<PubInfo> getPage() {
    return page;
  }

  public void setPage(Page<PubInfo> page) {
    this.page = page;
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

  public List<ConstPubType> getPubTypeList() {
    return pubTypeList;
  }

  public void setPubTypeList(List<ConstPubType> pubTypeList) {
    this.pubTypeList = pubTypeList;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getPageNo() {
    return pageNo;
  }

  public void setPageNo(String pageNo) {
    this.pageNo = pageNo;
  }

  public Integer getNowYear() {
    if (nowYear == null || nowYear == 0) {
      LocalDate now = LocalDate.now();// 取当前日期
      nowYear = now.getYear();
    }
    return nowYear;
  }

  public void setNowYear(Integer nowYear) {
    this.nowYear = nowYear;
  }

}
