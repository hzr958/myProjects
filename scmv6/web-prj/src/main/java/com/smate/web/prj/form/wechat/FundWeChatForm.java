package com.smate.web.prj.form.wechat;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.model.Page;
import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.recommend.model.FundRecommendArea;
import com.smate.web.fund.recommend.model.FundRecommendRegion;
import com.smate.web.prj.model.wechat.FundWeChat;

/*
 * @author zjh 手机端基金表单
 */
public class FundWeChatForm {
  private Long scmOpenId;// 科研之友openid
  private Long psnId;// 人员id
  private String nextId;// 下一个查询id,用于更多
  private List<Long> fundIdList;// 基金id列表
  private List<FundWeChat> resultList;// 页面显示封装列表
  private Integer size = 10;// 列表最大结果数
  private Integer flag = 0;// 0:url请求，1:ajax请求
  private String des3FundId;// des3项目id
  private String encryptedFundId;
  private Integer totalCount;// 总数
  private Integer totalPages; // 总页数
  private Integer pageNum;// 当前页
  private Page page = new Page();
  private Integer hasLogin = 0; // 是否已登录， 1：是，0：未登录
  private String loginTargetUrl; // 未登录时，跳转登录页面时作为登录后跳转的页面地址
  private String des3FundAgencyIds;// 加密的资助机构ID字符串
  private String searchAreaCodes;// 选择的科技领域code
  private String searchRegionCodes;// 选择的地区code
  private String searchTimeCodes;// 选择的时间code
  private String searchseniority; // 申请资格， 1：企业，2：科研机构, code1,code2形式
  private List<FundRecommendRegion> fundRegionList;// 用于页面显示基金推荐条件关注地区的显示
  private List<FundRecommendArea> fundAreaList;// 用于显示推荐条件的科技领域
  private Map<Integer, String> seniority;// 单位性质key为code，value为名称
  private Map<Integer, String> allSeniority;// 企业，科研机构，其他的单位性质key为code，value为名称
  private Integer seniorityCode;// 单位性质
  private String defultArea; // 用户设置的科技领域
  private List<Map<String, Object>> allScienceAreaList; // 全部的学科
  private List<ConstRegion> allProvinceList;
  private Long superRegionId;
  private List<ConstRegion> areaNextList;
  private String defaultRegion;// 用户设置的关注地区的code
  private String searchKey;
  private List<ConstRegion> searchRegionList;
  private String regionCode;
  private String areaIds; // 地区的id,逗号拼接的多个地区ID
  private String areaId; // 单个地区ID

  private List<FundAgencyInterest> fundAgencyInterestList;// 基金推荐关注的资助机构
  private String searchAgencyId;// 选中的资助机构id
  private String defaultAgencyId;// 用户关注的资助机构
  private List<Map<String, Object>> fundAgencyMapList; // 用资助机构构建成的list
  private String psnAgencyIds; // 个人设置的资助机构id用逗号分隔
  private String saveAgencyIds;// 保存的资助机构id

  public Long getScmOpenId() {
    return scmOpenId;
  }

  public void setScmOpenId(Long scmOpenId) {
    this.scmOpenId = scmOpenId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getNextId() {
    return nextId;
  }

  public void setNextId(String nextId) {
    this.nextId = nextId;
  }

  public List<Long> getFundIdList() {
    return fundIdList;
  }

  public void setFundIdList(List<Long> fundIdList) {
    this.fundIdList = fundIdList;
  }

  public List<FundWeChat> getResultList() {
    return resultList;
  }

  public void setResultList(List<FundWeChat> resultList) {
    this.resultList = resultList;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public String getDes3FundId() {
    if (StringUtils.isBlank(des3FundId) && StringUtils.isNotBlank(encryptedFundId)) {
      des3FundId = encryptedFundId;
    }
    return des3FundId;
  }

  public void setDes3FundId(String des3FundId) {
    this.des3FundId = des3FundId;
  }

  public String getEncryptedFundId() {
    return encryptedFundId;
  }

  public void setEncryptedFundId(String encryptedFundId) {
    this.encryptedFundId = encryptedFundId;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public Integer getHasLogin() {
    return hasLogin;
  }

  public void setHasLogin(Integer hasLogin) {
    this.hasLogin = hasLogin;
  }

  public String getLoginTargetUrl() {
    return loginTargetUrl;
  }

  public void setLoginTargetUrl(String loginTargetUrl) {
    this.loginTargetUrl = loginTargetUrl;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public String getDes3FundAgencyIds() {
    return des3FundAgencyIds;
  }

  public void setDes3FundAgencyIds(String des3FundAgencyIds) {
    this.des3FundAgencyIds = des3FundAgencyIds;
  }

  public List<FundRecommendRegion> getFundRegionList() {
    return fundRegionList;
  }

  public void setFundRegionList(List<FundRecommendRegion> fundRegionList) {
    this.fundRegionList = fundRegionList;
  }

  public List<FundRecommendArea> getFundAreaList() {
    return fundAreaList;
  }

  public void setFundAreaList(List<FundRecommendArea> fundAreaList) {
    this.fundAreaList = fundAreaList;
  }

  public Integer getSeniorityCode() {
    return seniorityCode;
  }

  public void setSeniorityCode(Integer seniorityCode) {
    this.seniorityCode = seniorityCode;
  }

  public String getSearchAreaCodes() {
    return searchAreaCodes;
  }

  public void setSearchAreaCodes(String searchAreaCodes) {
    this.searchAreaCodes = searchAreaCodes;
  }

  public String getSearchRegionCodes() {
    return searchRegionCodes;
  }

  public void setSearchRegionCodes(String searchRegionCodes) {
    this.searchRegionCodes = searchRegionCodes;
  }

  public String getSearchTimeCodes() {
    return searchTimeCodes;
  }

  public void setSearchTimeCodes(String searchTimeCodes) {
    this.searchTimeCodes = searchTimeCodes;
  }

  public String getSearchseniority() {
    return searchseniority;
  }

  public void setSearchseniority(String searchseniority) {
    this.searchseniority = searchseniority;
  }

  public Map<Integer, String> getSeniority() {
    return seniority;
  }

  public void setSeniority(Map<Integer, String> seniority) {
    this.seniority = seniority;
  }

  public Map<Integer, String> getAllSeniority() {
    return allSeniority;
  }

  public void setAllSeniority(Map<Integer, String> allSeniority) {
    this.allSeniority = allSeniority;
  }

  public String getDefultArea() {
    return defultArea;
  }

  public void setDefultArea(String defultArea) {
    this.defultArea = defultArea;
  }

  public List<Map<String, Object>> getAllScienceAreaList() {
    return allScienceAreaList;
  }

  public void setAllScienceAreaList(List<Map<String, Object>> allScienceAreaList) {
    this.allScienceAreaList = allScienceAreaList;
  }

  public List<ConstRegion> getAllProvinceList() {
    return allProvinceList;
  }

  public void setAllProvinceList(List<ConstRegion> allProvinceList) {
    this.allProvinceList = allProvinceList;
  }

  public Long getSuperRegionId() {
    return superRegionId;
  }

  public void setSuperRegionId(Long superRegionId) {
    this.superRegionId = superRegionId;
  }

  public List<ConstRegion> getAreaNextList() {
    return areaNextList;
  }

  public void setAreaNextList(List<ConstRegion> areaNextList) {
    this.areaNextList = areaNextList;
  }

  public String getDefaultRegion() {
    return defaultRegion;
  }

  public void setDefaultRegion(String defaultRegion) {
    this.defaultRegion = defaultRegion;
  }

  public String getRegionCode() {
    return regionCode;
  }

  public void setRegionCode(String regionCode) {
    this.regionCode = regionCode;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public List<ConstRegion> getSearchRegionList() {
    return searchRegionList;
  }

  public void setSearchRegionList(List<ConstRegion> searchRegionList) {
    this.searchRegionList = searchRegionList;
  }

  public String getAreaIds() {
    return areaIds;
  }

  public void setAreaIds(String areaIds) {
    this.areaIds = areaIds;
  }

  public String getAreaId() {
    return areaId;
  }

  public void setAreaId(String areaId) {
    this.areaId = areaId;
  }

  public List<FundAgencyInterest> getFundAgencyInterestList() {
    return fundAgencyInterestList;
  }

  public void setFundAgencyInterestList(List<FundAgencyInterest> fundAgencyInterestList) {
    this.fundAgencyInterestList = fundAgencyInterestList;
  }

  public String getSearchAgencyId() {
    return searchAgencyId;
  }

  public void setSearchAgencyId(String searchAgencyId) {
    this.searchAgencyId = searchAgencyId;
  }

  public String getDefaultAgencyId() {
    return defaultAgencyId;
  }

  public void setDefaultAgencyId(String defaultAgencyId) {
    this.defaultAgencyId = defaultAgencyId;
  }

  public List<Map<String, Object>> getFundAgencyMapList() {
    return fundAgencyMapList;
  }

  public void setFundAgencyMapList(List<Map<String, Object>> fundAgencyMapList) {
    this.fundAgencyMapList = fundAgencyMapList;
  }

  public String getPsnAgencyIds() {
    return psnAgencyIds;
  }

  public void setPsnAgencyIds(String psnAgencyIds) {
    this.psnAgencyIds = psnAgencyIds;
  }

  public String getSaveAgencyIds() {
    return saveAgencyIds;
  }

  public void setSaveAgencyIds(String saveAgencyIds) {
    this.saveAgencyIds = saveAgencyIds;
  }

}
