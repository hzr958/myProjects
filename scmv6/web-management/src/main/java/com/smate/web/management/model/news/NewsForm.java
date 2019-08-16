package com.smate.web.management.model.news;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.util.URLDecoderUtil;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 新闻 表单
 *
 * @author aijiangbin
 * @create 2019-05-15 16:32
 **/
public class NewsForm {

  private Long psnId;
  private Page page = new Page();
  private Long newsId;
  private String des3NewsId;
  private NewsShowInfo newsShowInfo;
  private String des3NewsIds;
  private String nextDes3NewsId;
  private String origin =""; // 链接来源


  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public NewsShowInfo getNewsShowInfo() {
    return newsShowInfo;
  }

  public void setNewsShowInfo(NewsShowInfo newsShowInfo) {
    this.newsShowInfo = newsShowInfo;
  }

  public String getDes3NewsId() {
    return des3NewsId;
  }

  public void setDes3NewsId(String des3NewsId) {
    this.des3NewsId = des3NewsId;
  }

  public Long getNewsId() {
    if (newsId == null && StringUtils.isNotBlank(des3NewsId)) {
      String des3Str = Des3Utils.decodeFromDes3(URLDecoderUtil.decode(des3NewsId, "utf-8"));
      if (des3Str == null) {
        return newsId;
      } else {
        return Long.valueOf(des3Str);
      }

    }
    return newsId;
  }

  public void setNewsId(Long newsId) {
    this.newsId = newsId;
  }



  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }


  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3NewsIds() {
    return des3NewsIds;
  }

  public void setDes3NewsIds(String des3NewsIds) {
    this.des3NewsIds = des3NewsIds;
  }

  public String getNextDes3NewsId() {
    return nextDes3NewsId;
  }

  public void setNextDes3NewsId(String nextDes3NewsId) {
    this.nextDes3NewsId = nextDes3NewsId;
  }

}
