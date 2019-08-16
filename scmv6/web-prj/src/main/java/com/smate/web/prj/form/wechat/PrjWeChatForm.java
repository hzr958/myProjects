package com.smate.web.prj.form.wechat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.prj.model.common.PrjInfo;

/*
 * @author zjh 手机端 项目表单
 */
public class PrjWeChatForm {
  private Page<PrjInfo> page = new Page<PrjInfo>(); // 分页查询用
  private Long scmOpenId;// 科研之友openid
  private Long psnId;// 人员id
  private String des3PsnId;
  private String searchDes3PsnId;
  private Long searchPsnId = 0L;
  private String nextId;// 下一个查询id,用于更多
  private String des3NextId;// 下一个编码查询id,用于更多，
  private List<Project> prjList;// 项目列表给来循环构建pubinfo
  private List<PrjInfo> resultList;// 页面显示封装列表
  private PrjInfo prjInfo;// 详情用
  /*
   * private String timeOrder="desc";//排序用，默认desc private String titleOrder;// 排序用，
   */
  private String orderType = "0"; // 排序类型 (1 资助金额排序 , 0 标题排序 )
  private String orderRule = "asc"; // 排序 规则 默认desc
  private Map<String, Object> prjTopMap;// 用于列表头部
  private String prjXml;// 项目详情
  private String selectStatus;// 查询状态信息 (已结束 ,在研中)
  private String selectFundingAgencie;// 查询资助机构信息 （国家级 ,省级, 第3方, 其他）
  private String[] selectFundingAgencieArry;
  private Long prjId;// des3项目id
  private String des3PrjId;// des3项目id
  private Integer size = 10;// 每次请求的项目5条数
  private Integer flag = 0;// 0:url请求,1:ajax请求.
  private Integer count = 0;// 刷新次数

  private String other; // 是否他人项目列表
  private String des3OwnerPsnId; // 项目所有人ID,加密的
  private boolean isMyPrj; // 是否是自己的项目
  private Long cnfId; // 人员配置ID
  private Integer hasLogin = 0; // 是否已登录，1：是，0：否
  private boolean hasPrivatePrj = false; // 是否有隐私的项目
  private String loginTargetUrl; // 超时登录，作为目标url跳回来的url(加密)

  private HashMap<String, Object> resultMap; // 结果map
  private String comment;// 评论项目的内容

  private Long prjCount;// 成果数量
  private Long totalPrjCount;
  private Long currentPsnId;// 当前人员id
  private String searchKey;
  private Integer fundingYear;// 项目年度
  private String agencyNames;// 多个资助机构用逗号分隔
  private String orderBy;// 排序
  private Integer currentYear;// 当前年份
  private List<Map<String, Object>> agencyNameList;// 资助机构名称统计信息
  private String des3GrpId; // 加密的群组ID

  public Long getTotalPrjCount() {
    return totalPrjCount;
  }

  public void setTotalPrjCount(Long totalPrjCount) {
    this.totalPrjCount = totalPrjCount;
  }

  public Long getPrjCount() {
    return prjCount;
  }

  public void setPrjCount(Long prjCount) {
    this.prjCount = prjCount;
  }

  public void setMyPrj(boolean isMyPrj) {
    this.isMyPrj = isMyPrj;
  }

  public Long getScmOpenId() {
    return scmOpenId;
  }

  public void setScmOpenId(Long scmOpenId) {
    this.scmOpenId = scmOpenId;
  }

  public Long getPsnId() {
    if ((psnId == null || psnId == 0L) && StringUtils.isNotBlank(des3PsnId)) {
      psnId = Long.parseLong(Des3Utils.decodeFromDes3(des3PsnId));
    }
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

  public List<Project> getPrjList() {
    return prjList;
  }

  public void setPrjList(List<Project> prjList) {
    this.prjList = prjList;
  }

  public List<PrjInfo> getResultList() {
    return resultList;
  }

  public void setResultList(List<PrjInfo> resultList) {
    this.resultList = resultList;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getPrjXml() {
    return prjXml;
  }

  public void setPrjXml(String prjXml) {
    this.prjXml = prjXml;
  }

  public String getDes3NextId() {
    return des3NextId;
  }

  public void setDes3NextId(String des3NextId) {
    this.des3NextId = des3NextId;
  }

  public String getDes3PrjId() {
    return des3PrjId;
  }

  public void setDes3PrjId(String des3PrjId) {
    this.des3PrjId = des3PrjId;
  }

  public Map<String, Object> getPrjTopMap() {
    return prjTopMap;
  }

  public void setPrjTopMap(Map<String, Object> result) {
    this.prjTopMap = result;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getOrderRule() {
    return orderRule;
  }

  public void setOrderRule(String orderRule) {
    this.orderRule = orderRule;
  }

  public String getSelectStatus() {
    return selectStatus;
  }

  public void setSelectStatus(String selectStatus) {
    this.selectStatus = selectStatus;
  }

  public String getSelectFundingAgencie() {
    return selectFundingAgencie;
  }

  public void setSelectFundingAgencie(String selectFundingAgencie) {
    this.selectFundingAgencie = selectFundingAgencie;
  }

  public String[] getSelectFundingAgencieArry() {
    return selectFundingAgencieArry;
  }

  public void setSelectFundingAgencieArry(String[] selectFundingAgencieArry) {
    this.selectFundingAgencieArry = selectFundingAgencieArry;
  }

  public String getOther() {
    return other;
  }

  public void setOther(String other) {
    this.other = other;
  }

  public String getDes3PsnId() {
    if (StringUtils.isBlank(des3PsnId) && this.psnId != null) {
      des3PsnId = Des3Utils.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getDes3OwnerPsnId() {
    return des3OwnerPsnId;
  }

  public void setDes3OwnerPsnId(String des3OwnerPsnId) {
    this.des3OwnerPsnId = des3OwnerPsnId;
  }

  public boolean getIsMyPrj() {
    return isMyPrj;
  }

  public void setIsMyPrj(boolean isMyPrj) {
    this.isMyPrj = isMyPrj;
  }

  public Long getCnfId() {
    return cnfId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
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

  public HashMap<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(HashMap<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Page<PrjInfo> getPage() {
    return page;
  }

  public void setPage(Page<PrjInfo> page) {
    this.page = page;
  }

  public Long getPrjId() {
    if (prjId == null && des3PrjId != null) {
      String prjIdStr = Des3Utils.decodeFromDes3(des3PrjId);
      if (prjIdStr != null) {
        prjId = NumberUtils.parseLong(prjIdStr);
      }
    }
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public boolean isHasPrivatePrj() {
    return hasPrivatePrj;
  }

  public void setHasPrivatePrj(boolean hasPrivatePrj) {
    this.hasPrivatePrj = hasPrivatePrj;
  }

  public Long getCurrentPsnId() {
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public PrjInfo getPrjInfo() {
    return prjInfo;
  }

  public void setPrjInfo(PrjInfo prjInfo) {
    this.prjInfo = prjInfo;
  }

  public String getSearchDes3PsnId() {
    return searchDes3PsnId;
  }

  public void setSearchDes3PsnId(String searchDes3PsnId) {
    this.searchDes3PsnId = searchDes3PsnId;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Integer getFundingYear() {
    return fundingYear;
  }

  public void setFundingYear(Integer fundingYear) {
    this.fundingYear = fundingYear;
  }

  public Long getSearchPsnId() {
    if ((searchPsnId == null || searchPsnId == 0L) && StringUtils.isNotBlank(searchDes3PsnId)) {
      searchPsnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(searchDes3PsnId));
    }
    return searchPsnId;
  }

  public void setSearchPsnId(Long searchPsnId) {
    this.searchPsnId = searchPsnId;
  }

  public String getAgencyNames() {
    return agencyNames;
  }

  public void setAgencyNames(String agencyNames) {
    this.agencyNames = agencyNames;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public Integer getCurrentYear() {
    return currentYear;
  }

  public void setCurrentYear(Integer currentYear) {
    this.currentYear = currentYear;
  }

  public List<Map<String, Object>> getAgencyNameList() {
    return agencyNameList;
  }

  public void setAgencyNameList(List<Map<String, Object>> agencyNameList) {
    this.agencyNameList = agencyNameList;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

}
