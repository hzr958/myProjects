package com.smate.web.mobile.v8pub.vo.pdwh;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

public class PdwhPubOperateVO {
  private Long psnId;
  private Long pdwhPubId;
  private String des3PdwhPubId;
  private Integer operate; // 赞操作 0:取消赞 1:点赞
  private String content;// 评论内容
  private String comment;// 分享成果时的评论
  private Integer platform;// 分享平台 微信、微博等 参照SharePlatformEnum
  private Long grpId; // 群组id
  private Long readPsnId;// 被阅读人id
  private String des3ReadPsnId;
  private boolean isLogin; // 用户是否已登录
  private boolean needLikeStatus = true; // 是否要查出赞状态（已赞过还是未赞过）
  private boolean needCollectStatus = true; // 是否要查出收藏状态（已收藏过还是未收藏过）
  private String des3PsnId; // 加密的人员ID

  public Long getPsnId() {
    if (this.des3PsnId == null && StringUtils.isNotBlank(this.des3PsnId)) {
      String des3Str = Des3Utils.decodeFromDes3(this.des3PsnId);
      if (des3Str != null) {
        psnId = Long.valueOf(des3Str);
      }
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getOperate() {
    return operate;
  }

  public void setOperate(Integer operate) {
    this.operate = operate;
  }

  public Long getPdwhPubId() {
    if (pdwhPubId == null && StringUtils.isNotBlank(des3PdwhPubId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3PdwhPubId);
      if (des3Str == null) {
        return pdwhPubId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Integer getPlatform() {
    return platform;
  }

  public void setPlatform(Integer platform) {
    this.platform = platform;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3PdwhPubId() {
    return des3PdwhPubId;
  }

  public void setDes3PdwhPubId(String des3PdwhPubId) {
    this.des3PdwhPubId = des3PdwhPubId;
  }

  public Long getReadPsnId() {
    if (readPsnId == null && StringUtils.isNotBlank(des3ReadPsnId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3ReadPsnId);
      if (des3Str == null) {
        return readPsnId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return readPsnId;
  }

  public void setReadPsnId(Long readPsnId) {
    this.readPsnId = readPsnId;
  }

  public String getDes3ReadPsnId() {
    return des3ReadPsnId;
  }

  public void setDes3ReadPsnId(String des3ReadPsnId) {
    this.des3ReadPsnId = des3ReadPsnId;
  }

  public boolean isLogin() {
    return isLogin;
  }

  public void setLogin(boolean isLogin) {
    this.isLogin = isLogin;
  }

  public boolean getNeedLikeStatus() {
    return needLikeStatus;
  }

  public void setNeedLikeStatus(boolean needLikeStatus) {
    this.needLikeStatus = needLikeStatus;
  }

  public boolean getNeedCollectStatus() {
    return needCollectStatus;
  }

  public void setNeedCollectStatus(boolean needCollectStatus) {
    this.needCollectStatus = needCollectStatus;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

}
