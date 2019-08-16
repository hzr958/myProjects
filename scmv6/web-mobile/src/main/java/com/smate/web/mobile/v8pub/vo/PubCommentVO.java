package com.smate.web.mobile.v8pub.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;

public class PubCommentVO {
  private Long pubId;// 成果ID
  private Long psnId;// 评论人ID
  private String des3PsnId;
  private String des3PubId;
  private String commentsContent;// 评论内容
  private Date createDate;// 创建日期
  private String psnName; // 评论人名字
  private String psnAvatars;// 评论人的头像
  private String dateTimes;// 用于页面的一些格式化时间
  private Long commentId;
  private boolean isLogin;
  private Integer pageNo = 1;
  private Integer maxresult; // 每次最多查询的评论数量
  List<PubCommentVO> commentlist = new ArrayList<>();
  private Page<?> page = new Page<Object>();

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public Page<?> getPage() {
    return page;
  }

  public void setPage(Page<?> page) {
    this.page = page;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setParamPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPubId() {
    if (pubId == null && StringUtils.isNotBlank(des3PubId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3PubId);
      if (des3Str == null) {
        return pubId;
      } else {
        return Long.valueOf(des3Str);
      }

    }
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getCommentsContent() {
    return commentsContent;
  }

  public void setCommentsContent(String commentsContent) {
    this.commentsContent = commentsContent;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getPsnAvatars() {
    return psnAvatars;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  public String getDateTimes() {
    return dateTimes;
  }

  public void setDateTimes(String dateTimes) {
    this.dateTimes = dateTimes;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Long getCommentId() {
    return commentId;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }

  public boolean getIsLogin() {
    return isLogin;
  }

  public void setIsLogin(boolean isLogin) {
    this.isLogin = isLogin;
  }

  public List<PubCommentVO> getCommentlist() {
    return commentlist;
  }

  public void setCommentlist(List<PubCommentVO> commentlist) {
    this.commentlist = commentlist;
  }

  public Integer getMaxresult() {
    return maxresult;
  }

  public void setMaxresult(Integer maxresult) {
    this.maxresult = maxresult;
  }



}
