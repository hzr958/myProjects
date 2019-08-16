package com.smate.web.psn.model.friend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员列表Form
 *
 * @author wsn
 *
 */
public class PsnListViewForm {

  // 用户ID
  private Long psnId;
  // 加密的用户ID
  private String des3PsnId;
  // 他人ID
  private Long tempPsnId;
  // 加密的他人ID
  private String des3TempPsnId;
  // 研究领域ID
  private Long discId;
  // 人员列表
  private List<Person> psnList;
  // 页面显示封装列表
  private List<PsnInfo> psnInfoList = new ArrayList<PsnInfo>();
  // 每次请求的人员数量
  private Integer size;
  // 服务类型
  private String serviceType;

  private List<Integer> saIdListPsn = null;// 本人的科技领域Id
  private List<Long> regionIdListPsn = null;// 本人的地区Id
  private List<Long> insIdListPsn = null;// 本人的机构Id

  private String fromPage = "0";// 0=homePage 个人主页
  private String scienceAreaId = null; // 科技领域ID
  private List<Integer> saIdList = null;
  private List<Long> regionIdList = null;
  private List<Long> insIdList = null;
  private Page page = new Page();
  private List<Long> psnIds; // 人员ID list
  private List<Map<String, Object>> regionList;// 地区分组统计信息
  private List<Map<String, Object>> scienceAreaList;// 研究领域统计信息
  private String searchKey;// 检索条件
  private String regionId = null;// 检索条件-地区
  private String insId = null;// 检索条件-机构
  private String orderBy;// 排序字段
  private String des3ReqPsnIds; // 请求加好友的人员Id
  private Integer currentLoad;// (操作后的)页面加载条数
  private String module;// 返回到哪个模块 "rec":好友推荐 "(默认) myf":我的好友
  private String psnTypeForChat;// friend=好友；all=全站
  private Integer searchType;

  private boolean mobile = false;// 是否来自手机端

  private String allFilterValues;// 获取本人机构,地区以及研究领域Id
  private Boolean hasSendReqList = false;// 是否有发送的且未被处理的好友请求列表 true:有 false:没有

  private Integer isAll = 0;// 个人主页-查看全部 1:是 0:否
  private Integer beforehand = 0;// 预加载 1=是 0=否
  private Integer reqCount = 0;// 好友请求总数
  private Integer sendCount = 0;// 发送的好友请求总数
  private Integer isOthers = 1; // 是否是其他人，1：是，0：是本人

  private String invitCode;// 邀请码

  public PsnListViewForm() {
    super();
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    if (psnId != null && psnId > 0L && StringUtils.isBlank(des3PsnId)) {
      des3PsnId = ServiceUtil.encodeToDes3(psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getDiscId() {
    return discId;
  }

  public void setDiscId(Long discId) {
    this.discId = discId;
  }

  public List<Person> getPsnList() {
    return psnList;
  }

  public void setPsnList(List<Person> psnList) {
    this.psnList = psnList;
  }

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public List<Integer> getSaIdList() {
    return saIdList;
  }

  public void setSaIdList(List<Integer> saIdList) {
    this.saIdList = saIdList;
  }

  public List<Long> getRegionIdList() {
    return regionIdList;
  }

  public void setRegionIdList(List<Long> regionIdList) {
    this.regionIdList = regionIdList;
  }

  public List<Long> getInsIdList() {
    return insIdList;
  }

  public void setInsIdList(List<Long> insIdList) {
    this.insIdList = insIdList;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public String getFromPage() {
    return fromPage;
  }

  public void setFromPage(String fromPage) {
    this.fromPage = fromPage;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public List<Long> getPsnIds() {
    return psnIds;
  }

  public void setPsnIds(List<Long> psnIds) {
    this.psnIds = psnIds;
  }

  public List<Map<String, Object>> getRegionList() {
    return regionList;
  }

  public void setRegionList(List<Map<String, Object>> regionList) {
    this.regionList = regionList;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public String getInsId() {
    return insId;
  }

  public void setInsId(String insId) {
    this.insId = insId;
  }

  public String getDes3ReqPsnIds() {
    return des3ReqPsnIds;
  }

  public void setDes3ReqPsnIds(String des3ReqPsnIds) {
    this.des3ReqPsnIds = des3ReqPsnIds;
  }

  public Long getTempPsnId() {
    if (tempPsnId == null && StringUtils.isNotBlank(des3TempPsnId)) {
      tempPsnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3TempPsnId));
    }
    return tempPsnId;
  }

  public void setTempPsnId(Long tempPsnId) {
    this.tempPsnId = tempPsnId;
  }

  public String getDes3TempPsnId() {
    return des3TempPsnId;
  }

  public void setDes3TempPsnId(String des3TempPsnId) {
    this.des3TempPsnId = des3TempPsnId;
  }

  public Integer getCurrentLoad() {
    return currentLoad;
  }

  public void setCurrentLoad(Integer currentLoad) {
    this.currentLoad = currentLoad;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(String scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  public List<Map<String, Object>> getScienceAreaList() {
    return scienceAreaList;
  }

  public void setScienceAreaList(List<Map<String, Object>> scienceAreaList) {
    this.scienceAreaList = scienceAreaList;
  }

  public List<Integer> getSaIdListPsn() {
    return saIdListPsn;
  }

  public void setSaIdListPsn(List<Integer> saIdListPsn) {
    this.saIdListPsn = saIdListPsn;
  }

  public List<Long> getRegionIdListPsn() {
    return regionIdListPsn;
  }

  public void setRegionIdListPsn(List<Long> regionIdListPsn) {
    this.regionIdListPsn = regionIdListPsn;
  }

  public List<Long> getInsIdListPsn() {
    return insIdListPsn;
  }

  public void setInsIdListPsn(List<Long> insIdListPsn) {
    this.insIdListPsn = insIdListPsn;
  }

  public String getPsnTypeForChat() {
    return psnTypeForChat;
  }

  public void setPsnTypeForChat(String psnTypeForChat) {
    this.psnTypeForChat = psnTypeForChat;
  }

  public String getAllFilterValues() {
    return allFilterValues;
  }

  public void setAllFilterValues(String allFilterValues) {
    this.allFilterValues = allFilterValues;
  }

  public Boolean getHasSendReqList() {
    return hasSendReqList;
  }

  public void setHasSendReqList(Boolean hasSendReqList) {
    this.hasSendReqList = hasSendReqList;
  }

  public Integer getSearchType() {
    return searchType;
  }

  public void setSearchType(Integer searchType) {
    this.searchType = searchType;
  }

  public boolean getMobile() {
    return mobile;
  }

  public void setMobile(boolean mobile) {
    this.mobile = mobile;
  }

  public Integer getIsAll() {
    return isAll;
  }

  public void setIsAll(Integer isAll) {
    this.isAll = isAll;
  }

  public Integer getReqCount() {
    return reqCount;
  }

  public void setReqCount(Integer reqCount) {
    this.reqCount = reqCount;
  }

  public Integer getSendCount() {
    return sendCount;
  }

  public void setSendCount(Integer sendCount) {
    this.sendCount = sendCount;
  }

  public Integer getBeforehand() {
    return beforehand;
  }

  public void setBeforehand(Integer beforehand) {
    this.beforehand = beforehand;
  }

  public Integer getIsOthers() {
    return isOthers;
  }

  public void setIsOthers(Integer isOthers) {
    this.isOthers = isOthers;
  }

  public String getInvitCode() {
    return invitCode;
  }

  public void setInvitCode(String invitCode) {
    this.invitCode = invitCode;
  }

}
