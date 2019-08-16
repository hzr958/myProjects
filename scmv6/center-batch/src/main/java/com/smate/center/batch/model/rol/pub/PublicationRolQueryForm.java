package com.smate.center.batch.model.rol.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.center.batch.model.sns.pub.ConstDictionary;
import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 查询页Form,封装查询条件.
 * 
 * @author liqinghua
 * 
 */
public class PublicationRolQueryForm extends BasePublicationRolForm {

  /**
   * 
   */
  private static final long serialVersionUID = -855850927203939438L;

  /**
   * 发表年份(开始).
   */
  private Integer fromYear;

  private Integer fromMonth;
  /**
   * 发表年份(结束).
   */
  private Integer toYear;
  private Integer toMonth;
  // 具体年份
  private Integer queryYear;
  // 成果状态
  private List<ConstDictionary> pubStatusList;
  /**
   * 成果类型Id.
   */
  private Integer typeId;

  // 部门ID
  private Long deptId;
  // 部门他ID，查询提交带单位的前缀串，必须去除
  private String strDeptId;
  private String des3DeptId;
  // 部门名称
  private String deptName;
  // 父部门id
  private String strSuperDeptId;
  // 成果人员ID
  private Long psnId;
  // 成果标题
  private String pubTitle;
  // 成果语言
  private String pubLang;
  // 只检索确认过的成果
  private String onlyConfirmAuthor;
  // 只列出第一作者成果
  private String onlyFirstAuthor;
  // 只列出数据有错的成果.
  private boolean onlyInvalidPub = false;
  // 只列出有重复记录的成果.
  private boolean listHasDupPubs = false;
  // 成果作者是否完成匹配(0/1)
  private Integer authorState;
  // 成果状态.
  private Integer pubStatus;
  // 作者名字
  private String authorName;
  private String memberName;
  // 由作者的名字找到的id
  private String authorIds;
  // 由作者的名字找到的id
  private List<Long> authoerIdsList;
  // 成果认领状态列表
  private List<ConstDictionary> authorStatusList;
  // 成果认领状态
  private Long authorStatus;
  // 只列出导入的成果：0/1
  private boolean onlyImportsPub = false;
  // 只有查询操作
  private boolean onlySearch = false;

  private String actionMsg;
  // 单位JSON串，用于查询条件树使用
  private String unitJson;
  /**
   * 排序.
   */
  private String orderCtFirst;
  private String orderFirst;
  private String orderCtSecond;
  private String orderSecond;
  private String orderCategory;
  /**
   * 发布状态.
   */
  private Integer openStatus;

  private String queryType;
  private String queryValue;
  private Long queryPubId;
  private String unrelatedType;

  /**
   * 成果列表.
   */
  private String pubIds;
  private Long pubId;
  private String des3Id;

  /**
   * 排序字段.
   */
  private String order;
  private String orderBy;
  // 成果类型
  private List<ConstPubType> pubTypes;
  // 成果SIG
  private List<Map<String, Object>> sigList;
  // 用于判断是否点击查询按钮操作进入
  private Integer isSearch;
  private String isSearchShow;
  private String exportType;

  private Page<PublicationRol> pubPage = null;
  private List<PubDupQueryItemRow> dupItemList = null;
  private Integer listSci = 0;
  private Integer listSsci = 0;
  private Integer listEi = 0;
  private Integer listIstp = 0;
  // KPI统计是否完整，0/1/2全部，统计信息完整，统计信息不完整
  private Integer kpiValid;
  // 当前单位
  private Long currentInsId;
  private String currentInsName;
  // 补充统计数据完整性数据
  private String fillKpiData;
  // 是否是作者关联关系检索
  private boolean authorRelationSearch = false;
  // 作者关系检索：0全部,1成果作者和部门人员已关联,2成果作者未关联
  private Integer authorRelation = 0;
  // 查找部门人员名称
  private String unitAuthor;
  // 查找人员是否是第一作者
  private boolean firstAuthor = false;
  // 关联关系:0全部,1已确认关联,2未确认关联
  private Integer relationType = 0;
  private String unitAuthorDefault;

  private boolean showSelectUnit = true;

  // 是否显示收录情况
  private boolean showListInfo = false;
  // 是否显示ISI影响因子
  private boolean showImpactFactors = false;

  // 页面简单搜索的内容
  private String simpleSearchContent;
  // 插件返回的更新引用xml
  private String citedXml;

  private Map yearsMap;

  private String searchId;
  // 左边快速浏览名称：如：publishYear
  private String searchName;

  // 开放存储类型
  private List<ConstOAType> oaTypes;
  // 开放存储类型id
  private Long oaTypeId;

  private String isAllPub;// rol-2321 左边栏的全部成果

  public List<PubDupQueryItemRow> getDupItemList() {
    return dupItemList;
  }

  public void setDupItemList(List<PubDupQueryItemRow> dupItemList) {
    this.dupItemList = dupItemList;
  }

  public String getQueryType() {
    return queryType;
  }

  public String getQueryValue() {
    return queryValue;
  }

  public void setQueryType(String queryType) {
    this.queryType = queryType;
  }

  public String getFillKpiData() {
    return fillKpiData;
  }

  public void setFillKpiData(String fillKpiData) {
    this.fillKpiData = fillKpiData;
  }

  public Page<PublicationRol> getPubPage() {
    return pubPage;
  }

  public void setPubPage(Page<PublicationRol> pubPage) {
    this.pubPage = pubPage;
  }

  public void setQueryValue(String queryValue) {
    this.queryValue = queryValue;
  }

  public Long getQueryPubId() {
    return queryPubId;
  }

  public Long getCurrentInsId() {
    return currentInsId;
  }

  public String getCurrentInsName() {
    return currentInsName;
  }

  public void setCurrentInsId(Long currentInsId) {
    this.currentInsId = currentInsId;
  }

  public void setCurrentInsName(String currentInsName) {
    this.currentInsName = currentInsName;
  }

  public Integer getListSci() {
    return listSci;
  }

  public Integer getListSsci() {
    return listSsci;
  }

  public String getOnlyConfirmAuthor() {
    return onlyConfirmAuthor;
  }

  public void setOnlyConfirmAuthor(String onlyConfirmAuthor) {
    this.onlyConfirmAuthor = onlyConfirmAuthor;
  }

  public Integer getListEi() {
    return listEi;
  }

  public Integer getListIstp() {
    return listIstp;
  }

  public void setListSci(Integer listSci) {
    this.listSci = listSci;
  }

  public void setListSsci(Integer listSsci) {
    this.listSsci = listSsci;
  }

  public void setListEi(Integer listEi) {
    this.listEi = listEi;
  }

  public void setListIstp(Integer listIstp) {
    this.listIstp = listIstp;
  }

  public void setQueryPubId(Long queryPubId) {
    this.queryPubId = queryPubId;
  }

  /**
   * @return the fromYear
   */
  public Integer getFromYear() {
    return fromYear;
  }

  public String getStrDeptId() {
    return strDeptId;
  }

  public void setStrDeptId(String strDeptId) {
    this.strDeptId = strDeptId;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public List<ConstDictionary> getPubStatusList() {
    return pubStatusList;
  }

  public void setPubStatusList(List<ConstDictionary> pubStatusList) {
    this.pubStatusList = pubStatusList;
  }

  /**
   * @param fromYear the fromYear to set
   */
  public void setFromYear(Integer fromYear) {
    this.fromYear = fromYear;
  }

  public List<Long> getAuthoerIdsList() {
    return authoerIdsList;
  }

  public Integer getIsSearch() {
    return isSearch;
  }

  public void setIsSearch(Integer isSearch) {
    this.isSearch = isSearch;
  }

  public void setAuthoerIdsList(List<Long> authoerIdsList) {
    this.authoerIdsList = authoerIdsList;
  }

  public String getUnitJson() {
    return unitJson;
  }

  public void setUnitJson(String unitJson) {
    this.unitJson = unitJson;
  }

  public String getIsSearchShow() {
    return isSearchShow;
  }

  public void setIsSearchShow(String isSearchShow) {
    this.isSearchShow = isSearchShow;
  }

  /**
   * @return the toYear
   */
  public Integer getToYear() {
    return toYear;
  }

  /**
   * @param toYear the toYear to set
   */
  public void setToYear(Integer toYear) {
    this.toYear = toYear;
  }

  /**
   * @return the typeId
   */
  public Integer getTypeId() {
    return typeId;
  }

  public String getOrderCtFirst() {
    return orderCtFirst;
  }

  public void setOrderCtFirst(String orderCtFirst) {
    this.orderCtFirst = orderCtFirst;
  }

  public String getOrderFirst() {
    return orderFirst;
  }

  public void setOrderFirst(String orderFirst) {
    this.orderFirst = orderFirst;
  }

  public String getOrderCtSecond() {
    return orderCtSecond;
  }

  public void setOrderCtSecond(String orderCtSecond) {
    this.orderCtSecond = orderCtSecond;
  }

  public Integer getKpiValid() {
    return kpiValid;
  }

  public void setKpiValid(Integer kpiValid) {
    this.kpiValid = kpiValid;
  }

  public String getOrderSecond() {
    return orderSecond;
  }

  public void setOrderSecond(String orderSecond) {
    this.orderSecond = orderSecond;
  }

  /**
   * @param typeId the typeId to set
   */
  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  /**
   * @return the orderBy
   */
  public String getOrderBy() {
    return orderBy;
  }

  /**
   * @param orderBy the orderBy to set
   */
  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public List<ConstPubType> getPubTypes() {
    return pubTypes;
  }

  public void setPubTypes(List<ConstPubType> pubTypes) {
    this.pubTypes = pubTypes;
  }

  public String getPubIds() {
    return pubIds;
  }

  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public String getActionMsg() {
    return actionMsg;
  }

  public void setActionMsg(String actionMsg) {
    this.actionMsg = actionMsg;
  }

  /**
   * @return the deptId
   */
  public Long getDeptId() {
    return deptId;
  }

  /**
   * @param deptId the deptId to set
   */
  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  public String getStrSuperDeptId() {
    return strSuperDeptId;
  }

  public void setStrSuperDeptId(String strSuperDeptId) {
    this.strSuperDeptId = strSuperDeptId;
  }

  /**
   * @return the psnId
   */
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the pubTitle
   */
  public String getPubTitle() {
    return pubTitle;
  }

  /**
   * @param pubTitle the pubTitle to set
   */
  public void setPubTitle(String pubTitle) {
    this.pubTitle = pubTitle;
  }

  /**
   * @return the pubLang
   */
  public String getPubLang() {
    return pubLang;
  }

  /**
   * @param pubLang the pubLang to set
   */
  public void setPubLang(String pubLang) {
    this.pubLang = pubLang;
  }

  /**
   * @return the onlyFirstAuthor
   */
  public String getOnlyFirstAuthor() {
    return onlyFirstAuthor;
  }

  /**
   * @param onlyFirstAuthor the onlyFirstAuthor to set
   */
  public void setOnlyFirstAuthor(String onlyFirstAuthor) {
    this.onlyFirstAuthor = onlyFirstAuthor;
  }

  /**
   * @return the onlyInvalidPub
   */
  public boolean isOnlyInvalidPub() {
    return onlyInvalidPub;
  }

  /**
   * @param onlyInvalidPub the onlyInvalidPub to set
   */
  public void setOnlyInvalidPub(boolean onlyInvalidPub) {
    this.onlyInvalidPub = onlyInvalidPub;
  }

  /**
   * @return the authorState
   */
  public Integer getAuthorState() {
    return authorState;
  }

  /**
   * @param authorState the authorState to set
   */
  public void setAuthorState(Integer authorState) {
    this.authorState = authorState;
  }

  /**
   * @return the pubStatus
   */
  public Integer getPubStatus() {
    return pubStatus;
  }

  /**
   * @param pubStatus the pubStatus to set
   */
  public void setPubStatus(Integer pubStatus) {
    this.pubStatus = pubStatus;
  }

  /**
   * @return the authorName
   */
  public String getAuthorName() {
    return authorName;
  }

  /**
   * @param authorName the authorName to set
   */
  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  /**
   * @return the authorIds
   */
  public String getAuthorIds() {
    return authorIds;
  }

  /**
   * @param authorIds the authorIds to set
   */
  public void setAuthorIds(String authorIds) {
    this.authorIds = authorIds;
  }

  /**
   * @return the listHasDupPubs
   */
  public boolean isListHasDupPubs() {
    return listHasDupPubs;
  }

  /**
   * @param listHasDupPubs the listHasDupPubs to set
   */
  public void setListHasDupPubs(boolean listHasDupPubs) {
    this.listHasDupPubs = listHasDupPubs;
  }

  public List<ConstDictionary> getAuthorStatusList() {
    return authorStatusList;
  }

  public Long getAuthorStatus() {
    return authorStatus;
  }

  public void setAuthorStatusList(List<ConstDictionary> authorStatusList) {
    this.authorStatusList = authorStatusList;
  }

  public void setAuthorStatus(Long authorStatus) {
    this.authorStatus = authorStatus;
  }

  /**
   * @return the onlyImportsPub
   */
  public boolean isOnlyImportsPub() {
    return onlyImportsPub;
  }

  public Integer getFromMonth() {
    return fromMonth;
  }

  public Integer getToMonth() {
    return toMonth;
  }

  public void setFromMonth(Integer fromMonth) {
    this.fromMonth = fromMonth;
  }

  public void setToMonth(Integer toMonth) {
    this.toMonth = toMonth;
  }

  public Integer getQueryYear() {
    return queryYear;
  }

  public void setQueryYear(Integer queryYear) {
    this.queryYear = queryYear;
  }

  /**
   * @param onlyImportsPub the onlyImportsPub to set
   */
  public void setOnlyImportsPub(boolean onlyImportsPub) {
    this.onlyImportsPub = onlyImportsPub;
  }

  public Integer getOpenStatus() {
    return openStatus;
  }

  public void setOpenStatus(Integer openStatus) {
    this.openStatus = openStatus;
  }

  public String getOrderCategory() {
    return orderCategory;
  }

  public void setOrderCategory(String orderCategory) {
    this.orderCategory = orderCategory;
  }

  public String getExportType() {
    return exportType;
  }

  public void setExportType(String exportType) {
    this.exportType = exportType;
  }

  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public Integer getAuthorRelation() {
    return authorRelation;
  }

  public String getUnitAuthor() {
    return unitAuthor;
  }

  public boolean isFirstAuthor() {
    return firstAuthor;
  }

  public Integer getRelationType() {
    return relationType;
  }

  public void setAuthorRelation(Integer authorRelation) {
    this.authorRelation = authorRelation;
  }

  public void setUnitAuthor(String unitAuthor) {
    this.unitAuthor = unitAuthor;
  }

  public void setFirstAuthor(boolean firstAuthor) {
    this.firstAuthor = firstAuthor;
  }

  public void setRelationType(Integer relationType) {
    this.relationType = relationType;
  }

  public boolean isAuthorRelationSearch() {
    return authorRelationSearch;
  }

  public void setAuthorRelationSearch(boolean authorRelationSearch) {
    this.authorRelationSearch = authorRelationSearch;
  }

  public String getUnitAuthorDefault() {
    return unitAuthorDefault;
  }

  public void setUnitAuthorDefault(String unitAuthorDefault) {
    this.unitAuthorDefault = unitAuthorDefault;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public String getDes3DeptId() {
    return des3DeptId;
  }

  public void setDes3DeptId(String des3DeptId) {
    this.des3DeptId = des3DeptId;
  }

  public boolean isShowSelectUnit() {
    return showSelectUnit;
  }

  public void setShowSelectUnit(boolean showSelectUnit) {
    this.showSelectUnit = showSelectUnit;
  }

  public boolean isShowListInfo() {
    return showListInfo;
  }

  public void setShowListInfo(boolean showListInfo) {
    this.showListInfo = showListInfo;
  }

  public boolean isShowImpactFactors() {
    return showImpactFactors;
  }

  public void setShowImpactFactors(boolean showImpactFactors) {
    this.showImpactFactors = showImpactFactors;
  }

  public String getSimpleSearchContent() {
    if (StringUtils.isNotBlank(simpleSearchContent)) {
      return StringUtils.trim(simpleSearchContent);
    }
    return simpleSearchContent;
  }

  public void setSimpleSearchContent(String simpleSearchContent) {
    this.simpleSearchContent = simpleSearchContent;
  }

  public String getCitedXml() {
    return citedXml;
  }

  public void setCitedXml(String citedXml) {
    this.citedXml = citedXml;
  }

  public boolean isOnlySearch() {
    return onlySearch;
  }

  public void setOnlySearch(boolean onlySearch) {
    this.onlySearch = onlySearch;
  }

  public String getUnrelatedType() {
    return unrelatedType;
  }

  public void setUnrelatedType(String unrelatedType) {
    this.unrelatedType = unrelatedType;
  }

  public Map getYearsMap() {
    return yearsMap;
  }

  public void setYearsMap(Map yearsMap) {
    this.yearsMap = yearsMap;
  }

  public String getSearchId() {
    return ServiceUtil.decodeFromDes3(searchId);
  }

  public void setSearchId(String searchId) {
    this.searchId = searchId;
  }

  public String getSearchName() {
    return searchName;
  }

  public void setSearchName(String searchName) {
    this.searchName = searchName;
  }

  public List<ConstOAType> getOaTypes() {
    return oaTypes;
  }

  public void setOaTypes(List<ConstOAType> oaTypes) {
    this.oaTypes = oaTypes;
  }

  public Long getOaTypeId() {
    return oaTypeId;
  }

  public void setOaTypeId(Long oaTypeId) {
    this.oaTypeId = oaTypeId;
  }

  public String getIsAllPub() {
    return isAllPub;
  }

  public void setIsAllPub(String isAllPub) {
    this.isAllPub = isAllPub;
  }

  public List<Map<String, Object>> getSigList() {
    return sigList;
  }

  public void setSigList(List<Map<String, Object>> sigList) {
    this.sigList = sigList;
  }

}
