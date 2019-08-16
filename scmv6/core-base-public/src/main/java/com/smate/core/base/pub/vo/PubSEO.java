package com.smate.core.base.pub.vo;

import java.util.Locale;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.util.HtmlUtils;

public class PubSEO {
  private final String DESCRIPTION_ZH =
      "科研之友为用户提供基于科研社交网络平台的科技管理，成果推广和技术转移服务，使命是连接人与人，人与知识，人与服务，分享与发现知识 ，让科研更成功，让创新更高效";
  private final String DESCRIPTION_EN =
      "Smate.com is a research social network service for research management, research marketing and technology transfer.  Our mission is to connect people to share and discover knowledge, and to research and innovate smart.";
  private String title = "";
  private String description = "";
  private String keywords = "";

  public String getTitle() {
    if (StringUtils.isNotBlank(title)) {
      Locale locale = LocaleContextHolder.getLocale();
      if ("en_US".equals(locale.toString())) {// 为空用默认的
        return title + " | SMate";
      } else {
        return title + " | 科研之友";
      }
    }
    return title;
  }

  public void setTitle(String title) {
    title = HtmlUtils.htmlUnescape(title);
    title = title.replaceAll("<[^>]*>", "");
    this.title = title;
  }

  public String getDescription() {
    if (StringUtils.isBlank(description)) {
      Locale locale = LocaleContextHolder.getLocale();
      if ("en_US".equals(locale.toString())) {// 为空用默认的
        return DESCRIPTION_EN;
      } else {
        return DESCRIPTION_ZH;
      }
    }
    return description;
  }

  public void setDescription(String description) {
    description = HtmlUtils.htmlUnescape(description);
    description = description.replaceAll("<[^>]*>", "");
    description = HtmlUtils.htmlEscape(description);
    this.description = description;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    keywords = HtmlUtils.htmlUnescape(keywords);
    keywords = keywords.replaceAll("<[^>]*>", "");
    this.keywords = keywords;
  }

}
