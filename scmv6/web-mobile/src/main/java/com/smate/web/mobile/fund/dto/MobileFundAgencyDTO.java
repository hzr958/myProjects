package com.smate.web.mobile.fund.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 移动端资助机构操作DTO
 * 
 * @author wsn
 * @date Nov 22, 2018
 */
public class MobileFundAgencyDTO {
  private String des3FundAgencyId; // 资助机构des3Id
  private Long fundAgencyId;// 资助机构Id
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

  public String getDes3FundAgencyId() {
    return des3FundAgencyId;
  }

  public void setDes3FundAgencyId(String des3FundAgencyId) {
    this.des3FundAgencyId = des3FundAgencyId;
  }

  public Long getFundAgencyId() {
    if (fundAgencyId == null && StringUtils.isNotBlank(des3FundAgencyId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3FundAgencyId);
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

}
