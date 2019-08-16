package com.smate.web.mobile.v8pub.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.po.PubPO;

/**
 * 成果查询的接受参数的model
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
public class PubQueryModel {

  public Page<PubInfo> page = new Page<>();

  /**
   * 查询的成员psnId
   */
  public String des3SearchPsnId;
  public String showName;// 他人成果列表，人员名
  public Long searchPsnId;

  public Long cnfId;

  public Integer pubSum;

  /**
   * 查询的群组ID
   */
  public String des3SearchGrpId;
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
  public boolean self;

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
  public String orderBy = "DEFAULT";
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
   * 数据库查询的成果数据
   */
  public List<PubPO> pubList;
  /**
   * 成果ids
   */
  public List<Long> pubIds = new ArrayList<>();

  /**
   * 人主页-成果认领-查看全部 1:是 0:否
   */
  public Integer isAll = 0;
  /**
   * 成果认领 操作了的成果数
   */
  public Integer confirmCount = 0;
  /**
   * 是否查找统计数，默认为true
   */
  public boolean searchCount = true;
  private String nextId;// 下一个查询id,用于更多
  private String des3NextId;// 下一个编码查询id,用于更多，
  private Integer pubIndex;// 成果在当前面的行数，从0开始
  private Integer count = 0;// 下拉标记
  private String des3AreaId; // 加密的科技领域ID
  private String areaName;// 科技领域名
  private boolean isDoi; // 是否是doi
  private int currentYear; // 当前年份
  private String searchArea;// 查询的科技领域
  private Integer searchPubYear;// 查询的出版年份
  private String searchPubType;// 查询的成果类型
  private String searchString;// 检索字符串
  private String fromPage = "psn"; // 来自于哪个页面
  private String dynText; // 发表动态的时候的文字内容
  private String des3SearchPubId; // 加密的成果ID
  private Long searchPubId; // 成果ID
  private String des3FileId;
  private String pubDB; // 成果所属库，个人库（SNS）, 基准库（PDWH）
  private String searchFor; // 检索类型，人员（psn）, 论文（paper）,专利（patent）
  private String searchLanguage;// 语言

  private String representDes3PubIds;// 添加的代表成果id用逗号分隔


  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getPubIndex() {
    return pubIndex;
  }

  public void setPubIndex(Integer pubIndex) {
    this.pubIndex = pubIndex;
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
    } else {
      searchPsnId = SecurityUtils.getCurrentUserId();
    }
    return searchPsnId;
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
    if ((searchGrpId == null || searchGrpId == 0L) || StringUtils.isNotBlank(des3SearchGrpId)) {
      String des3 = Des3Utils.decodeFromDes3(des3SearchGrpId);
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
    return pubList;
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

  public Page<PubInfo> getPage() {
    return page;
  }

  public void setPage(Page<PubInfo> page) {
    this.page = page;
  }

  public Long getCnfId() {
    return cnfId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public String getNextId() {
    return nextId;
  }

  public void setNextId(String nextId) {
    this.nextId = nextId;
  }

  public String getDes3NextId() {
    return des3NextId;
  }

  public void setDes3NextId(String des3NextId) {
    this.des3NextId = des3NextId;
  }

  public String getDes3AreaId() {
    return des3AreaId;
  }

  public void setDes3AreaId(String des3AreaId) {
    this.des3AreaId = des3AreaId;
  }

  public boolean getIsDoi() {
    return isDoi;
  }

  public void setIsDoi(boolean isDoi) {
    this.isDoi = isDoi;
  }

  public int getCurrentYear() {
    return currentYear;
  }

  public void setCurrentYear(int currentYear) {
    this.currentYear = currentYear;
  }

  public String getSearchArea() {
    return searchArea;
  }

  public void setSearchArea(String searchArea) {
    this.searchArea = searchArea;
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

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public String getFromPage() {
    return fromPage;
  }

  public void setFromPage(String fromPage) {
    this.fromPage = fromPage;
  }

  public String getDynText() {
    return dynText;
  }

  public void setDynText(String dynText) {
    this.dynText = dynText;
  }

  public String getDes3SearchPubId() {
    return des3SearchPubId;
  }

  public void setDes3SearchPubId(String des3SearchPubId) {
    this.des3SearchPubId = des3SearchPubId;
  }

  public Long getSearchPubId() {
    if (StringUtils.isNotBlank(this.des3SearchPubId) && this.searchPubId == null) {
      searchPubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3SearchPubId));
    }
    return searchPubId;
  }

  public void setSearchPubId(Long searchPubId) {
    this.searchPubId = searchPubId;
  }

  public String getPubDB() {
    return pubDB;
  }

  public void setPubDB(String pubDB) {
    this.pubDB = pubDB;
  }

  public String getSearchFor() {
    return searchFor;
  }

  public void setSearchFor(String searchFor) {
    this.searchFor = searchFor;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public String getDes3FileId() {
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }



  public String getSearchLanguage() {
    return searchLanguage;
  }

  public void setSearchLanguage(String searchLanguage) {
    this.searchLanguage = searchLanguage;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }


  public String getRepresentDes3PubIds() {
    return representDes3PubIds;
  }

  public void setRepresentDes3PubIds(String representDes3PubIds) {
    this.representDes3PubIds = representDes3PubIds;
  }

}
