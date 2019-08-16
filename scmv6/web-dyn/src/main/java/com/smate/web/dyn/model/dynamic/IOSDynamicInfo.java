package com.smate.web.dyn.model.dynamic;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 
 * 用于科研之友IOS客户端个人动态内容显示,转化为Json
 */

public class IOSDynamicInfo {
  private Long dynId;// 动态ID
  private String des3DynId;// 动态ID加密
  // psninfo
  private String personAvatars;// 发表人的头像
  private String personNameZh;
  private String personNameEn;
  private String des3ProducerPsnId; // 发表人Id 加密
  // insinfo
  private String personInsInfoZh;// 发表人的机构单位
  private String personInsInfoEn;

  // pubinfo
  private String hasDes3PubId;
  private String des3ResId;
  private String linkTitleZh;
  private String linkTitleEn;
  private String pubDescrZh;
  private String pubDescrEn;
  private String pubAuthors;
  private String des3PubOwnerPsnId;
  private String pubPublishYear;
  private String fullTextImage;
  private String linkImage;

  private String collectPubStatus;
  // 基准库
  private Integer databaseType = 0;// 默认为0， 1: 个人库 2 基准库
  private Integer dbId;

  // 基金信息
  private String fundTitleZh;
  private String fundTitleEn;
  private String fundDescZh;
  private String fundDescEn;
  private String fundLogoUrl;
  private Long fundId;
  private String encodeFundId;
  private String collectFundStatus;

  // ATEMP
  private String userAddContent;// 发表人的动态内容
  private Long parentDynId;
  private String des3ParentDynId;
  private Long resId;// 发表人的 资源 Id
  private Integer resType;// 资源类型 1：成果 ;
  private String dynType;

  // B1TEMP{包含：DYN_ID,PARENT_DYN_ID}
  // B1TEMP包含+next4props
  private String des3PsnAId;// 操作（包括，点赞，分享评论）人的Id
  // B2TEMP{包含：RES_ID,DES3_RES_ID,DYN_ID,PARENT_DYN_ID,RES_TYPE,DYN_TYPE}
  private String operatorTypeEn; // VAL_EN = { "liked this.",
  // "commented on this.", "shared this." };
  private String operatorTypeZh;// VAL = { "评论了", "赞了", "分享了" };
  private Integer operateStatus;// 操作的行为1：评论了，2：赞了 ，3：分享了
  private String operatorComment;// 操作（包括，点赞，分享评论）人的评论内容
  private String awardStatus;// 是否被赞
  private Integer awardCount = 0;// 赞计数
  private Integer commentCount = 0;// 评论计数
  private Integer shareCount = 0;// 分享计数
  private String publishDate;// 发布时间

  private String srcPsnNameZh;// 源动态发表人中文名
  private String srcPsnNameEn;//
  private String srcPsnInsInfoZh;// 源动态发表人的机构单位中文
  private String srcPsnInsInfoEn;//
  private String srcpersonAvatars;// 源动态发表人的头像
  private String srcdes3ProducerPsnId;// 源动态发表人的加密ID
  private String reContent;// 第二次分享输入的分享内容
  private Integer resDelete;// 0、资源已经删除，1、资源未删除
  private Integer collectStatus;// 收录情况
  private Integer fulltextPermission;// 0、公开，其他隐私
  // B3TEMP{包含：RES_ID,DES3_RES_ID,DYN_ID,PARENT_DYN_ID,RES_TYPE,DYN_TYPE}

  /**
   * OPERATOR_TYPE_ZH ="添加了成果" OPERATOR_TYPE_EN= "uploaded publications."
   */

  public IOSDynamicInfo() {
    super();
  }

  public String getCollectFundStatus() {
    return collectFundStatus;
  }

  public void setCollectFundStatus(String collectFundStatus) {
    this.collectFundStatus = collectFundStatus;
  }

  public Integer getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(Integer databaseType) {
    this.databaseType = databaseType;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public String getHasDes3PubId() {
    return hasDes3PubId;
  }

  public String getFundTitleZh() {
    return fundTitleZh;
  }

  public void setFundTitleZh(String fundTitleZh) {
    this.fundTitleZh = fundTitleZh;
  }

  public String getFundTitleEn() {
    return fundTitleEn;
  }

  public void setFundTitleEn(String fundTitleEn) {
    this.fundTitleEn = fundTitleEn;
  }

  public String getFundDescZh() {
    return fundDescZh;
  }

  public void setFundDescZh(String fundDescZh) {
    this.fundDescZh = fundDescZh;
  }

  public String getFundDescEn() {
    return fundDescEn;
  }

  public void setFundDescEn(String fundDescEn) {
    this.fundDescEn = fundDescEn;
  }

  public String getFundLogoUrl() {
    return fundLogoUrl;
  }

  public void setFundLogoUrl(String fundLogoUrl) {
    this.fundLogoUrl = fundLogoUrl;
  }

  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  public String getEncodeFundId() {
    return encodeFundId;
  }

  public void setEncodeFundId(String encodeFundId) {
    this.encodeFundId = encodeFundId;
  }

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public String getPersonAvatars() {
    return personAvatars;
  }

  public void setPersonAvatars(String personAvatars) {
    this.personAvatars = personAvatars;
  }

  public String getPersonNameZh() {
    return personNameZh;
  }

  public void setPersonNameZh(String personNameZh) {
    this.personNameZh = personNameZh;
  }

  public String getPersonNameEn() {
    return personNameEn;
  }

  public void setPersonNameEn(String personNameEn) {
    this.personNameEn = personNameEn;
  }

  public String getDes3ProducerPsnId() {
    return des3ProducerPsnId;
  }

  public void setDes3ProducerPsnId(String des3ProducerPsnId) {
    this.des3ProducerPsnId = des3ProducerPsnId;
  }

  public String getPersonInsInfoZh() {
    return personInsInfoZh;
  }

  public void setPersonInsInfoZh(String personInsInfoZh) {
    this.personInsInfoZh = personInsInfoZh;
  }

  public String getPersonInsInfoEn() {
    return personInsInfoEn;
  }

  public void setPersonInsInfoEn(String personInsInfoEn) {
    this.personInsInfoEn = personInsInfoEn;
  }

  public String isHasDes3PubId() {
    return hasDes3PubId;
  }

  public void setHasDes3PubId(String hasDes3PubId) {
    this.hasDes3PubId = hasDes3PubId;
  }

  public String getDes3ResId() {
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }

  public String getLinkTitleZh() {
    return linkTitleZh;
  }

  public void setLinkTitleZh(String linkTitleZh) {
    this.linkTitleZh = linkTitleZh;
  }

  public String getLinkTitleEn() {
    return linkTitleEn;
  }

  public void setLinkTitleEn(String linkTitleEn) {
    this.linkTitleEn = linkTitleEn;
  }

  public String getPubDescrZh() {
    return pubDescrZh;
  }

  public void setPubDescrZh(String pubDescrZh) {
    this.pubDescrZh = pubDescrZh;
  }

  public String getPubDescrEn() {
    return pubDescrEn;
  }

  public void setPubDescrEn(String pubDescrEn) {
    this.pubDescrEn = pubDescrEn;
  }

  public String getPubAuthors() {
    return pubAuthors;
  }

  public void setPubAuthors(String pubAuthors) {
    this.pubAuthors = pubAuthors;
  }

  public String getPubPublishYear() {
    return pubPublishYear;
  }

  public void setPubPublishYear(String pubPublishYear) {
    this.pubPublishYear = pubPublishYear;
  }

  public String getFullTextImage() {
    return fullTextImage;
  }

  public void setFullTextImage(String fullTextImage) {
    this.fullTextImage = fullTextImage;
  }

  public String getLinkImage() {
    return linkImage;
  }

  public void setLinkImage(String linkImage) {
    this.linkImage = linkImage;
  }

  public String getUserAddContent() {
    return userAddContent;
  }

  public void setUserAddContent(String userAddContent) {
    this.userAddContent = userAddContent;
  }

  public Long getParentDynId() {
    return parentDynId;
  }

  public void setParentDynId(Long parentDynId) {
    this.parentDynId = parentDynId;
  }

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public Integer getResType() {
    return resType;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

  public String getDynType() {
    return dynType;
  }

  public void setDynType(String dynType) {
    this.dynType = dynType;
  }

  public String getDes3PsnAId() {
    return des3PsnAId;
  }

  public void setDes3PsnAId(String des3PsnAId) {
    this.des3PsnAId = des3PsnAId;
  }

  public String getOperatorTypeEn() {
    return operatorTypeEn;
  }

  public void setOperatorTypeEn(String operatorTypeEn) {
    this.operatorTypeEn = operatorTypeEn;
  }

  public String getOperatorTypeZh() {
    return operatorTypeZh;
  }

  public void setOperatorTypeZh(String operatorTypeZh) {
    this.operatorTypeZh = operatorTypeZh;
  }

  public Integer getOperateStatus() {
    return operateStatus;
  }

  public void setOperateStatus(Integer operateStatus) {
    this.operateStatus = operateStatus;
  }

  public String getOperatorComment() {
    return operatorComment;
  }

  public void setOperatorComment(String operatorComment) {
    this.operatorComment = operatorComment;
  }

  public String getAwardStatus() {
    return awardStatus;
  }

  public void setAwardStatus(String awardStatus) {
    this.awardStatus = awardStatus;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public String getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }

  public String getSrcPsnNameZh() {
    return srcPsnNameZh;
  }

  public void setSrcPsnNameZh(String srcPsnNameZh) {
    this.srcPsnNameZh = srcPsnNameZh;
  }

  public String getSrcPsnNameEn() {
    return srcPsnNameEn;
  }

  public void setSrcPsnNameEn(String srcPsnNameEn) {
    this.srcPsnNameEn = srcPsnNameEn;
  }

  public String getSrcPsnInsInfoZh() {
    return srcPsnInsInfoZh;
  }

  public void setSrcPsnInsInfoZh(String srcPsnInsInfoZh) {
    this.srcPsnInsInfoZh = srcPsnInsInfoZh;
  }

  public String getSrcPsnInsInfoEn() {
    return srcPsnInsInfoEn;
  }

  public void setSrcPsnInsInfoEn(String srcPsnInsInfoEn) {
    this.srcPsnInsInfoEn = srcPsnInsInfoEn;
  }

  public String getSrcpersonAvatars() {
    return srcpersonAvatars;
  }

  public void setSrcpersonAvatars(String srcpersonAvatars) {
    this.srcpersonAvatars = srcpersonAvatars;
  }

  public String getSrcdes3ProducerPsnId() {
    return srcdes3ProducerPsnId;
  }

  public void setSrcdes3ProducerPsnId(String srcdes3ProducerPsnId) {
    this.srcdes3ProducerPsnId = srcdes3ProducerPsnId;
  }

  public String getDes3DynId() {
    if (StringUtils.isBlank(des3DynId) && dynId > 0L) {
      des3DynId = Des3Utils.encodeToDes3(dynId.toString());
    }
    return des3DynId;
  }

  public void setDes3DynId(String des3DynId) {
    this.des3DynId = des3DynId;
  }

  public String getDes3ParentDynId() {
    if (StringUtils.isBlank(des3ParentDynId) && parentDynId != null && parentDynId > 0L) {
      des3ParentDynId = Des3Utils.encodeToDes3(parentDynId.toString());
    }
    return des3ParentDynId;
  }

  public void setDes3ParentDynId(String des3ParentDynId) {
    this.des3ParentDynId = des3ParentDynId;
  }

  public String getCollectPubStatus() {
    return collectPubStatus;
  }

  public void setCollectPubStatus(String collectPubStatus) {
    this.collectPubStatus = collectPubStatus;
  }

  public String getReContent() {
    return reContent;
  }

  public void setReContent(String reContent) {
    this.reContent = reContent;
  }

  public String getDes3PubOwnerPsnId() {
    return des3PubOwnerPsnId;
  }

  public void setDes3PubOwnerPsnId(String des3PubOwnerPsnId) {
    this.des3PubOwnerPsnId = des3PubOwnerPsnId;
  }

  public Integer getResDelete() {
    return resDelete;
  }

  public void setResDelete(Integer resDelete) {
    this.resDelete = resDelete;
  }

  public Integer getCollectStatus() {
    return collectStatus;
  }

  public void setCollectStatus(Integer collectStatus) {
    this.collectStatus = collectStatus;
  }

  public Integer getFulltextPermission() {
    return fulltextPermission;
  }

  public void setFulltextPermission(Integer fulltextPermission) {
    this.fulltextPermission = fulltextPermission;
  }



}
