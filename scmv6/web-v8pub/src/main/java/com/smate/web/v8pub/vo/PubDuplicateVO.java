package com.smate.web.v8pub.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;

public class PubDuplicateVO {
  // 群组成果加密grpId
  private String des3GrpId;
  // 人员加密的psnId
  private String des3PsnId;
  // 成果加密的pubId
  private String des3PubId;
  // 人员的psnId
  private Long psnId;
  // 成果发布日期
  private String publishDate;
  // 成果标题
  private String title;
  // 成果类别
  private Integer pubType;
  // 成果dbId
  private Integer srcDbId;
  // 成果doi
  private String doi;
  // 成果的sourceId
  private String sourceId;
  // 专利申请号
  private String applicationNo;
  // 专利公开（公告）号
  private String publicationOpenNo;

  private String standardNo;
  private String registerNo;

  // 返回的结果
  private Map<String, Object> resultMap = new HashMap<>();;
  // 重复成果的列表
  private List<RepeatPubInfo> repeatPubInfoList = new ArrayList<>();

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (!NumberUtils.isNullOrZero(psnId)) {
      return DisposeDes3IdUtils.disposeDes3Id(psnId, des3PsnId);
    } else {
      return SecurityUtils.getCurrentUserId();
    }
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public String getApplicationNo() {
    return applicationNo;
  }

  public void setApplicationNo(String applicationNo) {
    this.applicationNo = applicationNo;
  }

  public String getPublicationOpenNo() {
    return publicationOpenNo;
  }

  public void setPublicationOpenNo(String publicationOpenNo) {
    this.publicationOpenNo = publicationOpenNo;
  }

  public List<RepeatPubInfo> getRepeatPubInfoList() {
    return repeatPubInfoList;
  }

  public void setRepeatPubInfoList(List<RepeatPubInfo> repeatPubInfoList) {
    this.repeatPubInfoList = repeatPubInfoList;
  }

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Integer getSrcDbId() {
    return srcDbId;
  }

  public void setSrcDbId(Integer srcDbId) {
    this.srcDbId = srcDbId;
  }

  public String getStandardNo() {
    return standardNo;
  }

  public void setStandardNo(String standardNo) {
    this.standardNo = standardNo;
  }

  public String getRegisterNo() {
    return registerNo;
  }

  public void setRegisterNo(String registerNo) {
    this.registerNo = registerNo;
  }
}
