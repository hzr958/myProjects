package com.smate.web.fund.recommend.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.model.common.CategoryScm;
import com.smate.web.fund.model.common.ConstFundCategoryInfo;

/**
 * 基金推荐form
 * 
 * @author WSN
 *
 *         2017年8月18日 上午10:07:56
 *
 */
public class FundRecommendForm {

  private String encryptedPsnId; // 加密的人员ID
  private Long psnId; // 人员ID
  private Page<Long> page = new Page<Long>(); // 基金分页查询用
  private List<ConstFundCategoryInfo> fundInfoList; // 基金信息List
  private String errorMsg; // 错误信息
  private Integer awardOperation; // 0：赞操作， 1：取消赞操作
  private Long fundId; // 基金ID
  private String encryptedFundId; // 加密的基金ID
  private Integer collectOperate; // 收藏操作： 0：收藏， 1：取消收藏
  private String seniority; // 申请资格， 1：企业，2：科研机构, code1,code2形式
  private String scienceAreaIds; // 科技领域 code1,code2,code3形式
  // 关注的地区,json字符串,{"seqNo":"1","regionName":"XXX", "regionId":"12"}
  private String interestRegion;
  private List<FundInterestRegion> regionInfo; // 关注的地区，取值方便
  private List<FundScienceArea> fundScienceArea; // 基金的科技领域
  private String regionNames; // 地区名称
  private Integer timeFlag; // 时间， 1：一周之内，2：一个月之内， 3：一年之内
  private Integer pageNum; // 页数
  private Integer pageSize; // 每页显示记录数
  private Integer totalPages; // 总页数
  private String scienceAreaNames; // 科技领域名称
  private Integer awardCount = 0; // 赞统计数
  private Integer shareCount = 0; // 分享统计数
  private Integer readCount = 0;// 阅读数
  private boolean sameOperate = false; // 是否是重复的操作，比如已赞过该基金又发送赞请求
  private String module; // 模块，基金推荐：recommend, 我的基金：collected 资助机构 fundAgency
  private Map<String, Object> fundAgenceMap; // 用资助机构构建成的map
  private ConstFundCategoryInfo constFundCategoryInfo;
  private String fundIds; // 基金ID字符串
  private String des3FundIds; // 加密的基金ID字符串
  private String collectedFundIds; // 已收藏的基金IDs
  private String des3FundId; // 加密的基金ID
  private boolean hasCollected = false; // 是否已收藏过
  private boolean hasAward = false; // 是否已赞过
  private Integer totalCount = 0; // 查询到的结果总数
  private boolean fromMobile = false; // 是否是移动端的
  private boolean firstIn = false; // 是否第一次加载，true：第一次加载
  private boolean fundsUnlimit = false;// false：根据用户的默认地区、领域..条件能查出数据。true:不要地区、领域..条件能查出数据
  private boolean isSelf; // 是否是本人
  private String des3FundAgencyIds;// 加密的资助机构ID字符串
  // private Integer showMore = 0; // 显示更多(个人主页基金推荐模块), 1: 显示更多
  private String regionCode;// 地区id
  private String regionCodes;// 地区id集合
  private String areaCode;// 科技领域id
  private String areaCodes;// 多个科技领域id用逗号分隔
  private List<FundRecommendRegion> fundRegionList;// 用于页面显示基金推荐条件关注地区的显示
  private List<FundRecommendArea> fundAreaList;// 用于显示推荐条件的科技领域

  private String regionCodeSelect;
  private String scienceCodeSelect;
  private String seniorityCodeSelect;
  private String timeCodeSelect;
  private Integer seniorityCode;// 单位性质
  private String searchKey;
  private List<CategoryScm> categoryScmList;
  private String fromAPP = "false";
  private List<Object[]> queryList;
  private boolean isStaleDated = false;// 判断基金是否过期

  private List<FundAgencyInterest> fundAgencyInterestList;// 基金推荐关注的资助机构
  private String agencyId;// 资助机构id
  private String agencyIdSelect;// 选中的资助机构id
  private String saveAgencyIds;// 保存的资助机构id
  private List<DynamicRemdForm> dynRemdResList;
  private List<Long> fundIdList;
  private String platform; // 平台，pc或移动端，移动端(mobile)
  private String des3PsnId;
  private String psnIds;// 多个用逗号分隔
  private String seoTitle;// title不能用js拼接

  public FundRecommendForm() {
    super();
  }

  public String getEncryptedPsnId() {
    return encryptedPsnId;
  }

  public void setEncryptedPsnId(String encryptedPsnId) {
    this.encryptedPsnId = encryptedPsnId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Page<Long> getPage() {
    return page;
  }

  public void setPage(Page<Long> page) {
    this.page = page;
  }

  public List<ConstFundCategoryInfo> getFundInfoList() {
    return fundInfoList;
  }

  public void setFundInfoList(List<ConstFundCategoryInfo> fundInfoList) {
    this.fundInfoList = fundInfoList;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Integer getAwardOperation() {
    return awardOperation;
  }

  public void setAwardOperation(Integer awardOperation) {
    this.awardOperation = awardOperation;
  }

  public Long getFundId() {
    if (fundId == null && StringUtils.isNotBlank(des3FundId)) {
      fundId = Long.parseLong(Des3Utils.decodeFromDes3(des3FundId));
    }
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  public String getEncryptedFundId() {
    return encryptedFundId;
  }

  public void setEncryptedFundId(String encryptedFundId) {
    this.encryptedFundId = encryptedFundId;
  }

  public Integer getCollectOperate() {
    return collectOperate;
  }

  public void setCollectOperate(Integer collectOperate) {
    this.collectOperate = collectOperate;
  }

  public String getSeniority() {
    return seniority;
  }

  public void setSeniority(String seniority) {
    this.seniority = seniority;
  }

  public String getScienceAreaIds() {
    return scienceAreaIds;
  }

  public void setScienceAreaIds(String scienceAreaIds) {
    this.scienceAreaIds = scienceAreaIds;
  }

  public String getInterestRegion() {
    return interestRegion;
  }

  public void setInterestRegion(String interestRegion) {
    this.interestRegion = interestRegion;
  }

  public List<FundInterestRegion> getRegionInfo() {
    return regionInfo;
  }

  public void setRegionInfo(List<FundInterestRegion> regionInfo) {
    this.regionInfo = regionInfo;
  }

  public List<FundScienceArea> getFundScienceArea() {
    return fundScienceArea;
  }

  public void setFundScienceArea(List<FundScienceArea> fundScienceArea) {
    this.fundScienceArea = fundScienceArea;
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

  public String getScienceAreaNames() {
    return scienceAreaNames;
  }

  public void setScienceAreaNames(String scienceAreaNames) {
    this.scienceAreaNames = scienceAreaNames;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }


  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getReadCount() {
    return readCount;
  }

  public void setReadCount(Integer readCount) {
    this.readCount = readCount;
  }

  public boolean getSameOperate() {
    return sameOperate;
  }

  public void setSameOperate(boolean sameOperate) {
    this.sameOperate = sameOperate;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public Map<String, Object> getFundAgenceMap() {
    return fundAgenceMap;
  }

  public void setFundAgenceMap(Map<String, Object> fundAgenceMap) {
    this.fundAgenceMap = fundAgenceMap;
  }

  public ConstFundCategoryInfo getConstFundCategoryInfo() {
    if (constFundCategoryInfo == null) {
      constFundCategoryInfo = new ConstFundCategoryInfo();
    }
    return constFundCategoryInfo;
  }

  public void setConstFundCategoryInfo(ConstFundCategoryInfo constFundCategoryInfo) {
    this.constFundCategoryInfo = constFundCategoryInfo;
  }

  public String getFundIds() {
    return fundIds;
  }

  public void setFundIds(String fundIds) {
    this.fundIds = fundIds;
  }

  public String getDes3FundIds() {
    return des3FundIds;
  }

  public void setDes3FundIds(String des3FundIds) {
    this.des3FundIds = des3FundIds;
  }

  public String getCollectedFundIds() {
    return collectedFundIds;
  }

  public void setCollectedFundIds(String collectedFundIds) {
    this.collectedFundIds = collectedFundIds;
  }

  public String getDes3FundId() {
    return des3FundId;
  }

  public void setDes3FundId(String des3FundId) {
    this.des3FundId = des3FundId;
  }

  public boolean getHasCollected() {
    return hasCollected;
  }

  public void setHasCollected(boolean hasCollected) {
    this.hasCollected = hasCollected;
  }

  public boolean getHasAward() {
    return hasAward;
  }

  public void setHasAward(boolean hasAward) {
    this.hasAward = hasAward;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public boolean getFromMobile() {
    return fromMobile;
  }

  public void setFromMobile(boolean fromMobile) {
    this.fromMobile = fromMobile;
  }

  public boolean getFirstIn() {
    return firstIn;
  }

  public void setFirstIn(boolean firstIn) {
    this.firstIn = firstIn;
  }

  public boolean isFundsUnlimit() {
    return fundsUnlimit;
  }

  public void setFundsUnlimit(boolean fundsUnlimit) {
    this.fundsUnlimit = fundsUnlimit;
  }

  public boolean getIsSelf() {
    return isSelf;
  }

  public void setIsSelf(boolean isSelf) {
    this.isSelf = isSelf;
  }

  public String getDes3FundAgencyIds() {
    return des3FundAgencyIds;
  }

  public void setDes3FundAgencyIds(String des3FundAgencyIds) {
    this.des3FundAgencyIds = des3FundAgencyIds;
  }

  public String getRegionCode() {
    return regionCode;
  }

  public void setRegionCode(String regionCode) {
    this.regionCode = regionCode;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
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

  public String getRegionCodeSelect() {
    return regionCodeSelect;
  }

  public void setRegionCodeSelect(String regionCodeSelect) {
    this.regionCodeSelect = regionCodeSelect;
  }

  public String getScienceCodeSelect() {
    return scienceCodeSelect;
  }

  public void setScienceCodeSelect(String scienceCodeSelect) {
    this.scienceCodeSelect = scienceCodeSelect;
  }

  public String getSeniorityCodeSelect() {
    return seniorityCodeSelect;
  }

  public void setSeniorityCodeSelect(String seniorityCodeSelect) {
    this.seniorityCodeSelect = seniorityCodeSelect;
  }

  public String getTimeCodeSelect() {
    return timeCodeSelect;
  }

  public void setTimeCodeSelect(String timeCodeSelect) {
    this.timeCodeSelect = timeCodeSelect;
  }

  public Integer getSeniorityCode() {
    return seniorityCode;
  }

  public void setSeniorityCode(Integer seniorityCode) {
    this.seniorityCode = seniorityCode;
  }

  public String getRegionCodes() {
    return regionCodes;
  }

  public void setRegionCodes(String regionCodes) {
    this.regionCodes = regionCodes;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public List<CategoryScm> getCategoryScmList() {
    return categoryScmList;
  }

  public void setCategoryScmList(List<CategoryScm> categoryScmList) {
    this.categoryScmList = categoryScmList;
  }

  public String getAreaCodes() {
    return areaCodes;
  }

  public void setAreaCodes(String areaCodes) {
    this.areaCodes = areaCodes;
  }

  public String getFromAPP() {
    return fromAPP;
  }

  public void setFromAPP(String fromAPP) {
    this.fromAPP = fromAPP;
  }

  public List<Object[]> getQueryList() {
    return queryList;
  }

  public void setQueryList(List<Object[]> queryList) {
    this.queryList = queryList;
  }

  public boolean getIsStaleDated() {
    return isStaleDated;
  }

  public void setIsStaleDated(boolean isStaleDated) {
    this.isStaleDated = isStaleDated;
  }

  public List<FundAgencyInterest> getFundAgencyInterestList() {
    return fundAgencyInterestList;
  }

  public void setFundAgencyInterestList(List<FundAgencyInterest> fundAgencyInterestList) {
    this.fundAgencyInterestList = fundAgencyInterestList;
  }

  public String getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(String agencyId) {
    this.agencyId = agencyId;
  }

  public String getAgencyIdSelect() {
    return agencyIdSelect;
  }

  public void setAgencyIdSelect(String agencyIdSelect) {
    this.agencyIdSelect = agencyIdSelect;
  }

  public String getSaveAgencyIds() {
    return saveAgencyIds;
  }

  public void setSaveAgencyIds(String saveAgencyIds) {
    this.saveAgencyIds = saveAgencyIds;
  }

  public List<DynamicRemdForm> getDynRemdResList() {
    return dynRemdResList;
  }

  public void setDynRemdResList(List<DynamicRemdForm> dynRemdResList) {
    this.dynRemdResList = dynRemdResList;
  }

  public List<Long> getFundIdList() {
    return fundIdList;
  }

  public void setFundIdList(List<Long> fundIdList) {
    this.fundIdList = fundIdList;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getPsnIds() {
    return psnIds;
  }

  public void setPsnIds(String psnIds) {
    this.psnIds = psnIds;
  }

  public String getSeoTitle() {
    return seoTitle;
  }

  public void setSeoTitle(String seoTitle) {
    this.seoTitle = seoTitle;
  }



  // public Integer getShowMore() {
  // return showMore;
  // }
  //
  // public void setShowMore(Integer showMore) {
  // this.showMore = showMore;
  // }

}
