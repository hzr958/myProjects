package com.smate.center.task.model.search;

import java.io.Serializable;

import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 人员检索结果.
 * 
 * @author chenxiangrong
 * 
 */
public class UserSearchResultForm implements Serializable {

  private static final long serialVersionUID = -5645826634387144138L;
  private Long psnId;// 人员Id
  private String des3PsnId;
  private int psnNode;// 人员信息所在节点
  private String zhInfo;// 人员姓名
  private String enInfo;// 人员英文名
  private String avatars;// 头像
  private String title;// 头衔
  private String insName;// 首要单位
  private String region;// 地区
  private String hindex;// hIndex
  private String pubNum;// 成果数
  private String citeNum;// 引用次数
  private String keyWords;// 关键词
  private String brief;// 个人简介
  private String publicHome;// 公开主页
  private boolean isFriend;// 是否好友
  private boolean isKnow;// 可能认识的人
  private int scoreNum;// 信息完整度评分
  private String regionShow;// 人员地区的显示内容.

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    if (psnId != null) {
      des3PsnId = ServiceUtil.encodeToDes3(psnId + "");
    }
    return des3PsnId;
  }

  public int getPsnNode() {
    return psnNode;
  }

  public void setPsnNode(int psnNode) {
    this.psnNode = psnNode;
  }

  public String getZhInfo() {
    return zhInfo;
  }

  public void setZhInfo(String zhInfo) {
    this.zhInfo = zhInfo;
  }

  public String getEnInfo() {
    return enInfo;
  }

  public void setEnInfo(String enInfo) {
    this.enInfo = enInfo;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getHindex() {
    return hindex;
  }

  public void setHindex(String hindex) {
    this.hindex = hindex;
  }

  public String getPubNum() {
    return pubNum;
  }

  public void setPubNum(String pubNum) {
    this.pubNum = pubNum;
  }

  public String getCiteNum() {
    return citeNum;
  }

  public void setCiteNum(String citeNum) {
    this.citeNum = citeNum;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public String getBrief() {
    return brief;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  public String getPublicHome() {
    return publicHome;
  }

  public void setPublicHome(String publicHome) {
    this.publicHome = publicHome;
  }

  public boolean isFriend() {
    return isFriend;
  }

  public void setFriend(boolean isFriend) {
    this.isFriend = isFriend;
  }

  public boolean isKnow() {
    return isKnow;
  }

  public void setKnow(boolean isKnow) {
    this.isKnow = isKnow;
  }

  public int getScoreNum() {
    return scoreNum;
  }

  public void setScoreNum(int scoreNum) {
    this.scoreNum = scoreNum;
  }

  public String getRegionShow() {
    return regionShow;
  }

  public void setRegionShow(String regionShow) {
    this.regionShow = regionShow;
  }

}
