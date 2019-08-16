package com.smate.web.fund.find.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.model.common.CategoryScm;
import com.smate.web.fund.model.common.ConstFundAgency;
import com.smate.web.fund.model.common.ConstFundCategoryInfo;
import com.smate.web.prj.model.wechat.FundWeChat;

@SuppressWarnings("rawtypes")
public class FundFindForm {
  private Map<String, Object> categoryMap; // 用科技领域构建成的map
  private String regionCodesSelect;
  private String scienceCodesSelect;
  private String seniorityCodeSelect;
  private List<ConstFundCategoryInfo> fundInfoList; // 基金信息List
  private Integer pageNum; // 页数
  private Integer pageSize; // 每页显示记录数
  private Integer totalPages; // 总页数
  private Integer totalCount = 0; // 查询到的结果总数
  private String fromAPP = "false";
  private String keywords;// 检索关键词
  private String regions;// 选择的地区，用于给移动端调用
  private String des3FundAgencyIds;
  private String locale;
  private String Des3FundAgencyId; // 资助机构des3Id
  private Long fundAgencyId;// 资助机构Id
  private String fundAgencyName;// 资助机构名称
  private String logoUrl;// 机构图片
  private ConstFundAgency fund;// 资助机构
  private Long disId;// 科技领域id
  private String insType; // 单位要求
  private Page page = new Page(10);// 分页对象
  private List<CategoryScm> disciplineList;// 科技领域list
  private String disIdStr;// 一个或多个科技领域的Json字符串
  private List<ConstFundAgency> fundAgencyList;// 机构列表
  private String regionAgency;// 页面检索接受的参数
  private List<ConstRegion> regionList;// 资助机构地区列表
  private List<Long> regionAgencyIds;// 多个机构Id
  private String selectAgencyCodes;// 多个资助机构用逗号分隔
  private Integer maxSelectFund;// 最多选择资助机构数量
  private List<Map<String, Object>> fundAgencyMapList; // 用资助机构构建成的map
  private List<FundAgencyInterest> selectFundAgencyList;// 选中的资助机构
  private Integer optType; // 操作类型
  private Long currentPsnId; // 当前登录人员ID
  private String des3ReceiverIds; // 加密的接受者ID,多个用逗号隔开
  private String des3GroupId; // 加密的群组ID
  private String comments; // 分享留言
  private Integer shareToPlatform; // 分享到哪个平台
  private String errorMsg; // 错误信息
  private List<Long> psnIds; // 接收分享的人员ID
  private Long groupId; // 群组ID
  private String des3AgencyIds; // 加密的资助机构ID,多个用逗号隔开
  private String searchKey;// 搜索资助机构
  private Integer pageNo;
  private String agencyNames; // 分享的资助机构名称，多个用逗号隔开
  private String resInfoJson; // 分享资助机构时资助机构信息
  private Long psnId;
  private Boolean agencyNotExit;// 资助机构不存在
  private Integer hasLogin; // 是否已登录
  private List<FundWeChat> resultList;
  private String searchRegion;

  public String getDes3FundAgencyId() {
    return Des3FundAgencyId;
  }

  public void setDes3FundAgencyId(String des3FundAgencyId) {
    Des3FundAgencyId = des3FundAgencyId;
  }

  public Long getFundAgencyId() {
    if (fundAgencyId == null && StringUtils.isNotBlank(Des3FundAgencyId)) {
      String des3Str = Des3Utils.decodeFromDes3(Des3FundAgencyId);
      if (StringUtils.isNotBlank(des3Str)) {
        fundAgencyId = Long.valueOf(des3Str);
      } else {
        fundAgencyId = 0L;
      }
    }
    return fundAgencyId;
  }

  public void setFundAgencyId(Long fundAgencyId) {
    this.fundAgencyId = fundAgencyId;
  }

  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  public String getInsType() {
    return insType;
  }

  public void setInsType(String insType) {
    this.insType = insType;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public List<CategoryScm> getDisciplineList() {
    return disciplineList;
  }

  public void setDisciplineList(List<CategoryScm> disciplineList) {
    this.disciplineList = disciplineList;
  }

  public String getDisIdStr() {
    return disIdStr;
  }

  public void setDisIdStr(String disIdStr) {
    this.disIdStr = disIdStr;
  }

  public String getFundAgencyName() {
    return fundAgencyName;
  }

  public void setFundAgencyName(String fundAgencyName) {
    this.fundAgencyName = fundAgencyName;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  private Long regionId;

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public List<ConstFundAgency> getFundAgencyList() {
    return fundAgencyList;
  }

  public void setFundAgencyList(List<ConstFundAgency> fundAgencyList) {
    this.fundAgencyList = fundAgencyList;
  }

  public String getRegionAgency() {
    return regionAgency;
  }

  public void setRegionAgency(String regionAgency) {
    this.regionAgency = regionAgency;
  }

  public List<Long> getRegionAgencyIds() {
    return regionAgencyIds;
  }

  public void setRegionAgencyIds(List<Long> regionAgencyIds) {
    this.regionAgencyIds = regionAgencyIds;
  }

  public ConstFundAgency getFund() {
    return fund;
  }

  public void setFund(ConstFundAgency fund) {
    this.fund = fund;
  }

  public List<ConstRegion> getRegionList() {
    return regionList;
  }

  public void setRegionList(List<ConstRegion> regionList) {
    this.regionList = regionList;
  }

  public String getSelectAgencyCodes() {
    return selectAgencyCodes;
  }

  public void setSelectAgencyCodes(String selectAgencyCodes) {
    this.selectAgencyCodes = selectAgencyCodes;
  }

  public Integer getMaxSelectFund() {
    return maxSelectFund;
  }

  public void setMaxSelectFund(Integer maxSelectFund) {
    this.maxSelectFund = maxSelectFund;
  }

  public List<Map<String, Object>> getFundAgencyMapList() {
    return fundAgencyMapList;
  }

  public void setFundAgencyMapList(List<Map<String, Object>> fundAgencyMapList) {
    this.fundAgencyMapList = fundAgencyMapList;
  }

  public List<FundAgencyInterest> getSelectFundAgencyList() {
    return selectFundAgencyList;
  }

  public void setSelectFundAgencyList(List<FundAgencyInterest> selectFundAgencyList) {
    this.selectFundAgencyList = selectFundAgencyList;
  }

  public Integer getOptType() {
    return optType;
  }

  public void setOptType(Integer optType) {
    this.optType = optType;
  }

  public Long getCurrentPsnId() {
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public String getDes3ReceiverIds() {
    return des3ReceiverIds;
  }

  public void setDes3ReceiverIds(String des3ReceiverIds) {
    this.des3ReceiverIds = des3ReceiverIds;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Integer getShareToPlatform() {
    return shareToPlatform;
  }

  public void setShareToPlatform(Integer shareToPlatform) {
    this.shareToPlatform = shareToPlatform;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public List<Long> getPsnIds() {
    return psnIds;
  }

  public void setPsnIds(List<Long> psnIds) {
    this.psnIds = psnIds;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getDes3AgencyIds() {
    return des3AgencyIds;
  }

  public void setDes3AgencyIds(String des3AgencyIds) {
    this.des3AgencyIds = des3AgencyIds;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public String getAgencyNames() {
    return agencyNames;
  }

  public void setAgencyNames(String agencyNames) {
    this.agencyNames = agencyNames;
  }

  public String getResInfoJson() {
    return resInfoJson;
  }

  public void setResInfoJson(String resInfoJson) {
    this.resInfoJson = resInfoJson;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Boolean getAgencyNotExit() {
    return agencyNotExit;
  }

  public void setAgencyNotExit(Boolean agencyNotExit) {
    this.agencyNotExit = agencyNotExit;
  }

  public Integer getHasLogin() {
    return hasLogin;
  }

  public void setHasLogin(Integer hasLogin) {
    this.hasLogin = hasLogin;
  }

  public Map<String, Object> getCategoryMap() {
    return categoryMap;
  }

  public void setCategoryMap(Map<String, Object> categoryMap) {
    this.categoryMap = categoryMap;
  }


  public String getRegionCodesSelect() {
    return regionCodesSelect;
  }

  public void setRegionCodesSelect(String regionCodesSelect) {
    this.regionCodesSelect = regionCodesSelect;
  }

  public String getScienceCodesSelect() {
    return scienceCodesSelect;
  }

  public void setScienceCodesSelect(String scienceCodesSelect) {
    this.scienceCodesSelect = scienceCodesSelect;
  }

  public String getSeniorityCodeSelect() {
    return seniorityCodeSelect;
  }

  public void setSeniorityCodeSelect(String seniorityCodeSelect) {
    this.seniorityCodeSelect = seniorityCodeSelect;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public List<ConstFundCategoryInfo> getFundInfoList() {
    return fundInfoList;
  }

  public void setFundInfoList(List<ConstFundCategoryInfo> fundInfoList) {
    this.fundInfoList = fundInfoList;
  }

  public String getFromAPP() {
    return fromAPP;
  }

  public void setFromAPP(String fromAPP) {
    this.fromAPP = fromAPP;
  }

  public String getRegions() {
    return regions;
  }

  public void setRegions(String regions) {
    this.regions = regions;
  }

  public String getDes3FundAgencyIds() {
    return des3FundAgencyIds;
  }

  public void setDes3FundAgencyIds(String des3FundAgencyIds) {
    this.des3FundAgencyIds = des3FundAgencyIds;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public List<FundWeChat> getResultList() {
    return resultList;
  }

  public void setResultList(List<FundWeChat> resultList) {
    this.resultList = resultList;
  }

  public String getSearchRegion() {
    return searchRegion;
  }

  public void setSearchRegion(String searchRegion) {
    this.searchRegion = searchRegion;
  }


}
