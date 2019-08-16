package com.smate.web.fund.agency.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.fund.model.common.CategoryScm;
import com.smate.web.fund.model.common.ConstFundAgency;

@SuppressWarnings("rawtypes")
public class FundAgencyForm {
  private String Des3FundAgencyId; // 资助机构des3Id
  private String des3FundIds;// 资助机构下所有的基金id
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
  private String receiverMails;// 接收者的邮件,由逗号分开
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
  private String des3PsnId;
  private Boolean agencyNotExit;// 资助机构不存在
  private Integer hasLogin; // 是否已登录
  private Long resType; // 资源类型
  private Long insId;// 单位id
  private String agencyIdsStr;// 资助机构id多个用逗号分隔

  public Long getResType() {
    return resType;
  }

  public void setResType(Long resType) {
    this.resType = resType;
  }

  private String des3FundId;// 加密的基金id

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

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getAgencyIdsStr() {
    return agencyIdsStr;
  }

  public void setAgencyIdsStr(String agencyIdsStr) {
    this.agencyIdsStr = agencyIdsStr;
  }

  public String getDes3FundId() {
    return des3FundId;
  }

  public void setDes3FundId(String des3FundId) {
    this.des3FundId = des3FundId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getReceiverMails() {
    return receiverMails;
  }

  public void setReceiverMails(String receiverMails) {
    this.receiverMails = receiverMails;
  }

  public String getDes3FundIds() {
    return des3FundIds;
  }

  public void setDes3FundIds(String des3FundIds) {
    this.des3FundIds = des3FundIds;
  }

}
