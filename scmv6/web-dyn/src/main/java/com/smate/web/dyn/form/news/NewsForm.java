package com.smate.web.dyn.form.news;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.dyn.model.news.NewsBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 新闻 表单
 *
 * @author aijiangbin
 * @create 2019-05-15 16:32
 **/
public class NewsForm {

  private List<NewsShowInfo> list = new ArrayList<>();
  private Long psnId;
  private Page page = new Page();
  private String orderBy = "";
  private Long newsId;
  private String des3NewsId;
  private Integer operate; // 赞操作 0:取消赞 1:点赞
  private Integer awardTimes;// 点赞数
  private Boolean manager = false; // 是否是管理员
  private String from = ""; // 是否是管理员
  private NewsShowInfo newsShowInfo;
  private Integer pageSize; // 每页显示记录数
  private Integer pageNum;
  public List<Long> newsIds = new ArrayList<>();
  private Boolean isManager = false; // 是否是管理员2
  private Boolean isLogin;// 是否登录
  private String content = ""; // 内容
  private Integer platform; // 分享平台 见 SharePlatformEnum
  private Long beSharedId; // 被分享的主键， 例如 好友，群组
  private String seoTitle;// seo标题
  private String seoDescription;// seo描述
  private final String DESCRIPTION_ZH =
      "科研之友为用户提供基于科研社交网络平台的科技管理，成果推广和技术转移服务，使命是连接人与人，人与知识，人与服务，分享与发现知识 ，让科研更成功，让创新更高效";
  private final String DESCRIPTION_EN =
      "Smate.com is a research social network service for research management, research marketing and technology transfer.  Our mission is to connect people to share and discover knowledge, and to research and innovate smart.";
  private NewsBase priorNews;// 详情页面 上一篇新闻
  private NewsBase nextNews;// 详情页面 下一篇新闻
  private String form; // 平台，pc或移动端，移动端(mobile)
  private String origin =""; // 链接来源

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public Long getBeSharedId() {
    return beSharedId;
  }

  public void setBeSharedId(Long beSharedId) {
    this.beSharedId = beSharedId;
  }

  public Integer getPlatform() {
    return platform;
  }

  public void setPlatform(Integer platform) {
    this.platform = platform;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public Boolean getManager() {
    return manager;
  }

  public void setManager(Boolean manager) {
    this.manager = manager && this.from.equalsIgnoreCase("management");
  }


  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    if (StringUtils.isNotBlank(orderBy)) {
      page.setOrderBy(orderBy);
    }
    this.orderBy = orderBy;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public List<NewsShowInfo> getList() {
    return list;
  }

  public void setList(List<NewsShowInfo> list) {
    this.list = list;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3NewsId() {
    return des3NewsId;
  }

  public void setDes3NewsId(String des3NewsId) {
    this.des3NewsId = des3NewsId;
  }

  public Long getNewsId() {
    if (newsId == null && StringUtils.isNotBlank(des3NewsId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3NewsId);
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

  public Integer getOperate() {
    return operate;
  }

  public void setOperate(Integer operate) {
    this.operate = operate;
  }

  public Integer getAwardTimes() {
    return awardTimes;
  }

  public void setAwardTimes(Integer awardTimes) {
    this.awardTimes = awardTimes;
  }

  public NewsShowInfo getNewsShowInfo() {
    return newsShowInfo;
  }

  public void setNewsShowInfo(NewsShowInfo newsShowInfo) {
    this.newsShowInfo = newsShowInfo;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public List<Long> getNewsIds() {
    return newsIds;
  }

  public void setNewsIds(List<Long> newsIds) {
    this.newsIds = newsIds;
  }

  public Boolean getIsManager() {
    return isManager;
  }

  public void setIsManager(Boolean isManager) {
    this.isManager = isManager;
  }

  public Boolean getIsLogin() {
    return isLogin;
  }

  public void setIsLogin(Boolean isLogin) {
    this.isLogin = isLogin;
  }

  public String getSeoTitle() {
    if (StringUtils.isNotBlank(seoTitle)) {
      Locale locale = LocaleContextHolder.getLocale();
      if ("en_US".equals(locale.toString())) {// 为空用默认的
        return seoTitle + " | SMate";
      } else {
        return seoTitle + " | 科研之友";
      }
    }
    return seoTitle;
  }

  public void setSeoTitle(String seoTitle) {
    if (StringUtils.isNotBlank(seoTitle)) {
      seoTitle = HtmlUtils.htmlUnescape(seoTitle);
      seoTitle = seoTitle.replaceAll("<[^>]*>", "");
      this.seoTitle = seoTitle;
    }
  }

  public String getSeoDescription() {
    if (StringUtils.isBlank(seoDescription)) {
      Locale locale = LocaleContextHolder.getLocale();
      if ("en_US".equals(locale.toString())) {// 为空用默认的
        return DESCRIPTION_EN;
      } else {
        return DESCRIPTION_ZH;
      }
    }
    return seoDescription;
  }

  public void setSeoDescription(String seoDescription) {
    if (StringUtils.isNotBlank(seoDescription)) {
      seoDescription = HtmlUtils.htmlUnescape(seoDescription);
      seoDescription = seoDescription.replaceAll("<[^>]*>", "");
      seoDescription = HtmlUtils.htmlEscape(seoDescription);
    }
    this.seoDescription = seoDescription;
  }

  public NewsBase getPriorNews() {
    return priorNews;
  }

  public void setPriorNews(NewsBase priorNews) {
    this.priorNews = priorNews;
  }

  public NewsBase getNextNews() {
    return nextNews;
  }

  public void setNextNews(NewsBase nextNews) {
    this.nextNews = nextNews;
  }

  public String getForm() {
    return form;
  }

  public void setForm(String form) {
    this.form = form;
  }

}
