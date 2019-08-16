package com.smate.web.fund.agency.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 单位信息,检索机构页面显示信息用
 * 
 * @author wsn
 *
 */
public class InsInfo implements Serializable {

  private static final long serialVersionUID = 746542788075523182L;

  private String insZhName; // 单位中文名称
  private String insEnName; // 单位英文名称
  private Integer psnSum = 0; // 单位人员数量
  private Integer pubSum = 0; // 单位成果数量
  private Integer prjSum = 0; // 单位项目数量
  private Integer shareSum = 0; // 单位被分享的数量
  private Integer likeSum = 0; // 单位被赞的数量
  private String regionName; // 单位所在地区
  private Long regionId; // 单位所在地区ID
  private String showName; // 页面显示的名称
  private Long insId; // 机构ID
  private String desInsId; // 加密机构ID
  private String description;//
  private Integer followSum = 0;// 关注数
  private String domain;//
  private String logoUrl;//
  public Integer isAward = 0;// 0未点赞1已点赞
  public Integer isFollow = 0;// 0未关注1已关注
  private String domainUrl;
  private Long characterId; // 机构类型ID
  private String characterName; // 机构类型名

  public InsInfo(String insZhName, String insEnName, Integer psnSum, Integer pubSum, Integer prjSum, Integer shareSum,
      Integer likeSum, String regionName, Long regionId, String showName) {
    super();
    this.insZhName = insZhName;
    this.insEnName = insEnName;
    this.psnSum = psnSum;
    this.pubSum = pubSum;
    this.prjSum = prjSum;
    this.shareSum = shareSum;
    this.likeSum = likeSum;
    this.regionName = regionName;
    this.regionId = regionId;
    this.showName = showName;
  }

  public InsInfo() {
    super();
  }

  public String getInsZhName() {
    return insZhName;
  }

  public void setInsZhName(String insZhName) {
    this.insZhName = insZhName;
  }

  public String getInsEnName() {
    return insEnName;
  }

  public void setInsEnName(String insEnName) {
    this.insEnName = insEnName;
  }

  public Integer getPsnSum() {
    return psnSum;
  }

  public void setPsnSum(Integer psnSum) {
    this.psnSum = psnSum;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public Integer getShareSum() {
    return shareSum;
  }

  public void setShareSum(Integer shareSum) {
    this.shareSum = shareSum;
  }

  public Integer getLikeSum() {
    return likeSum;
  }

  public void setLikeSum(Integer likeSum) {
    this.likeSum = likeSum;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getDesInsId() {
    if (this.insId != null && StringUtils.isBlank(this.desInsId)) {
      return Des3Utils.encodeToDes3(insId.toString());
    }
    return desInsId;
  }

  public void setDesInsId(String desInsId) {
    this.desInsId = desInsId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getFollowSum() {
    return followSum;
  }

  public void setFollowSum(Integer followSum) {
    this.followSum = followSum;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public Integer getIsAward() {
    return isAward;
  }

  public void setIsAward(Integer isAward) {
    this.isAward = isAward;
  }

  public Integer getIsFollow() {
    return isFollow;
  }

  public void setIsFollow(Integer isFollow) {
    this.isFollow = isFollow;
  }

  public String getDomainUrl() {
    return domainUrl;
  }

  public void setDomainUrl(String domainUrl) {
    this.domainUrl = domainUrl;
  }

  public Long getCharacterId() {
    return characterId;
  }

  public void setCharacterId(Long characterId) {
    this.characterId = characterId;
  }

  public String getCharacterName() {
    return characterName;
  }

  public void setCharacterName(String characterName) {
    this.characterName = characterName;
  }

}
