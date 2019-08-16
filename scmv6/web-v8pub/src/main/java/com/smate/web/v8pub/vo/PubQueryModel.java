package com.smate.web.v8pub.vo;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.po.PubPO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

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

  public Long searchPsnId;

  public Long cnfId;

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
  public String orderBy = "";
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
  /**
   * 编辑成果标识
   */
  public boolean editPubFlag = false;

  public boolean isEditPubFlag() {
    return editPubFlag;
  }

  public void setEditPubFlag(boolean editPubFlag) {
    this.editPubFlag = editPubFlag;
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

}
