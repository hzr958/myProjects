package com.smate.web.fund.model.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 基金信息
 * 
 * @author WSN
 *
 *         2017年8月18日 下午2:08:05
 *
 */
public class ConstFundCategoryInfo implements Serializable {

  private static final long serialVersionUID = -4640938272493405077L;
  private String fundTitle; // 基金名称
  private Long fundAgencyId; // 资助机构ID
  private String fundAgencyName; // 资助机构名称
  private Long regionId; // 所属地区ID
  private String regionName; // 所属地区名称
  private String scienceAreas;// 所属科技领域
  private List<Integer> scienceAreaIds; // 所属科技领域ID
  private Integer awardCount; // 赞统计数
  private boolean hasCollected = false; // 是否已收藏
  private String logoUrl; // logo图片url
  private Integer shareCount; // 分享统计数
  private Long fundId; // 基金ID
  private String startDate; // 开始时间
  private String endDate; // 结束时间
  private String score; // 推荐分数
  private String encryptedFundId; // 加密的基金ID
  private boolean hasAward = false; // 已赞过
  private String showDate; // 显示的时间
  private Date start; // 开始时间
  private Date end; // 结束时间
  private boolean isStaleDated = true;
  private String zhTitle; // 中文标题
  private String enTitle; // 英文标题
  private String zhAgencyName; // 中文资助机构名称
  private String enAgencyName; // 英文资助机构名称
  private String zhScienceArea; // 中文科技领域
  private String enScienceArea; // 英文科技领域
  private String zhShowDesc; // 中文的描述（资助机构名称，科技领域，申请时间）
  private String enShowDesc; // 英文的描述
  private String zhShowDescBr; // 中文的描述（资助机构名称，科技领域，申请时间）用br换行分隔
  private String enShowDescBr; // 英文的描述
  private String showYear;// 基金年度
  private String strength;// 预计资助金额
  private String description;// 类别描述
  private String showIsMatch;// 是否配套 0:不配套 1:配套
  private String percentage;// 比例
  private List<ConstFundCategoryFile> fundFileList;// 附件
  private String guideUrl;// 申报指南网址
  private String declareUrl;// 申报网址
  private String detailsUrl; // 详情地址
  private String fundRcmdUrl;// 基金推荐列表
  // 基金的领域id
  private Long disId;
  // 基金领域名称包括父领域的
  private String disAllName;
  // 单位要求
  private String insType;

  public ConstFundCategoryInfo() {
    super();
  }

  public String getFundTitle() {
    return fundTitle;
  }

  public void setFundTitle(String fundTitle) {
    this.fundTitle = fundTitle;
  }

  public Long getFundAgencyId() {
    return fundAgencyId;
  }

  public void setFundAgencyId(Long fundAgencyId) {
    this.fundAgencyId = fundAgencyId;
  }

  public String getFundAgencyName() {
    return fundAgencyName;
  }

  public void setFundAgencyName(String fundAgencyName) {
    this.fundAgencyName = fundAgencyName;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public String getScienceAreas() {
    return scienceAreas;
  }

  public void setScienceAreas(String scienceAreas) {
    this.scienceAreas = scienceAreas;
  }

  public List<Integer> getScienceAreaIds() {
    return scienceAreaIds;
  }

  public void setScienceAreaIds(List<Integer> scienceAreaIds) {
    this.scienceAreaIds = scienceAreaIds;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public boolean getHasCollected() {
    return hasCollected;
  }

  public void setHasCollected(boolean hasCollected) {
    this.hasCollected = hasCollected;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Long getFundId() {
    if (fundId == null && encryptedFundId != null) {
      fundId = NumberUtils.toLong(Des3Utils.decodeFromDes3(encryptedFundId));
    }
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }

  public String getEncryptedFundId() {
    return encryptedFundId;
  }

  public void setEncryptedFundId(String encryptedFundId) {
    this.encryptedFundId = encryptedFundId;
  }

  public boolean getHasAward() {
    return hasAward;
  }

  public void setHasAward(boolean hasAward) {
    this.hasAward = hasAward;
  }

  public String getShowDate() {
    return showDate;
  }

  public void setShowDate(String showDate) {
    this.showDate = showDate;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getZhAgencyName() {
    return zhAgencyName;
  }

  public void setZhAgencyName(String zhAgencyName) {
    this.zhAgencyName = zhAgencyName;
  }

  public String getEnAgencyName() {
    return enAgencyName;
  }

  public void setEnAgencyName(String enAgencyName) {
    this.enAgencyName = enAgencyName;
  }

  public String getZhScienceArea() {
    return zhScienceArea;
  }

  public void setZhScienceArea(String zhScienceArea) {
    this.zhScienceArea = zhScienceArea;
  }

  public String getEnScienceArea() {
    return enScienceArea;
  }

  public void setEnScienceArea(String enScienceArea) {
    this.enScienceArea = enScienceArea;
  }

  public String getZhShowDesc() {
    return zhShowDesc;
  }

  public void setZhShowDesc(String zhShowDesc) {
    this.zhShowDesc = zhShowDesc;
  }

  public String getEnShowDesc() {
    return enShowDesc;
  }

  public void setEnShowDesc(String enShowDesc) {
    this.enShowDesc = enShowDesc;
  }

  public String getStrength() {
    return strength;
  }

  public void setStrength(String strength) {
    this.strength = strength;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPercentage() {
    return percentage;
  }

  public void setPercentage(String percentage) {
    this.percentage = percentage;
  }

  public List<ConstFundCategoryFile> getFundFileList() {
    return fundFileList;
  }

  public void setFundFileList(List<ConstFundCategoryFile> fundFileList) {
    this.fundFileList = fundFileList;
  }

  public String getGuideUrl() {
    return guideUrl;
  }

  public void setGuideUrl(String guideUrl) {
    this.guideUrl = guideUrl;
  }

  public String getDeclareUrl() {
    return declareUrl;
  }

  public void setDeclareUrl(String declareUrl) {
    this.declareUrl = declareUrl;
  }

  public String getDetailsUrl() {
    return detailsUrl;
  }

  public void setDetailsUrl(String detailsUrl) {
    this.detailsUrl = detailsUrl;
  }

  public String getFundRcmdUrl() {
    return fundRcmdUrl;
  }

  public void setFundRcmdUrl(String fundRcmdUrl) {
    this.fundRcmdUrl = fundRcmdUrl;
  }

  public String getShowYear() {
    return showYear;
  }

  public void setShowYear(String showYear) {
    this.showYear = showYear;
  }

  public String getShowIsMatch() {
    return showIsMatch;
  }

  public void setShowIsMatch(String showIsMatch) {
    this.showIsMatch = showIsMatch;
  }

  public String getZhShowDescBr() {
    return zhShowDescBr;
  }

  public void setZhShowDescBr(String zhShowDescBr) {
    this.zhShowDescBr = zhShowDescBr;
  }

  public String getEnShowDescBr() {
    return enShowDescBr;
  }

  public void setEnShowDescBr(String enShowDescBr) {
    this.enShowDescBr = enShowDescBr;
  }

  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  public String getDisAllName() {
    return disAllName;
  }

  public void setDisAllName(String disAllName) {
    this.disAllName = disAllName;
  }

  public String getInsType() {
    return insType;
  }

  public void setInsType(String insType) {
    this.insType = insType;
  }

  public boolean getIsStaleDated() {
    return isStaleDated;
  }

  public void setIsStaleDated(boolean isStaleDated) {
    this.isStaleDated = isStaleDated;
  }

}
