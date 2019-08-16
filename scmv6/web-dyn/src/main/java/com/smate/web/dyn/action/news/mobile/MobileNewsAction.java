package com.smate.web.dyn.action.news.mobile;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.model.news.NewsBase;
import com.smate.web.dyn.service.news.NewsBaseService;
import com.smate.web.dyn.service.news.NewsOptService;

/**
 * 新闻相关Action
 * 
 * @author ajb
 *
 */
@Results({@Result(name = "mobile_news_main", location = "/WEB-INF/jsp/mobile/news/mobile_news_main.jsp"),
    @Result(name = "mobile_news_main_list", location = "/WEB-INF/jsp/mobile/news/mobile_news_main_list.jsp"),
    @Result(name = "mobile_news_detail", location = "/WEB-INF/jsp/mobile/news/mobile_news_detail.jsp"),
    @Result(name = "mobile_news_not_exists", location = "/WEB-INF/jsp/mobile/news/mobile_news_not_exists.jsp")})
public class MobileNewsAction extends ActionSupport implements Preparable, ModelDriven<NewsForm> {

  private static final long serialVersionUID = -9090557623490845340L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private NewsForm form;
  @Autowired
  private NewsBaseService newsBaseService;
  @Autowired
  private NewsOptService newsOptService;



  /**
   * 获取新闻列表
   *
   * @return
   */
  @Actions({@Action("/dynweb/news/mobile/main")})
  public String showMobileNewsMain() {
    try {

    } catch (Exception e) {
      logger.error("进入新闻主页异常,userId=" + form.getPsnId(), e);
    }
    return "mobile_news_main";
  }

  /**
   * 获取新闻列表
   *
   * @return
   */
  @Actions({@Action("/dynweb/news/mobile/ajaxnewslist")})
  public String ajaxMobileNewsList() {
    try {
      newsBaseService.findNewsList(form);
    } catch (Exception e) {
      logger.error("查找新闻列表,userId=" + form.getPsnId(), e);
    }
    return "mobile_news_main_list";
  }

  /**
   * 获取新闻消息
   */
  @Action("/dynweb/news/mobile/forshare")
  public void forShare() {
    Map<String, String> result = new HashMap<String, String>();
    if (NumberUtils.isNotNullOrZero(form.getNewsId())) {
      try {
        NewsBase newsBase = newsBaseService.get(form.getNewsId());
        if (newsBase != null) {
          result.put("title", newsBase.getTitle());
          result.put("image", newsBase.getImage());
          result.put("brief", newsBase.getBrief());
          result.put("status", "success");
          result.put("msg", "get data success");
        } else {
          result.put("status", "error");
          result.put("msg", "resource is not exists");
        }
      } catch (Exception e) {
        logger.error("获取新闻消息异常,userId=" + form.getPsnId(), e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
  }

  /**
   * 新闻详情页面
   *
   * @return
   */
  @Actions({@Action("/dynweb/mobile/news/details")})
  public String viewMobileNewsDetail() {
    try {
      Long newsId = form.getNewsId();
      form.setNewsId(newsId);
      if (!newsOptService.checkNews(newsId)) {
        return "mobile_news_not_exists";
      }
      // 增加访问记录
      newsOptService.addNewsView(form);
      newsBaseService.viewNewsDetails(form);
    } catch (Exception e) {
      logger.error("进入新闻详情异常,userId=" + form.getPsnId(), e);
    }
    return "mobile_news_detail";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new NewsForm();
    }
    form.setPsnId(SecurityUtils.getCurrentUserId());
    if (form.getPsnId() == 0L || form.getPsnId() == null) {
      form.setIsLogin(false);
    } else {
      form.setIsLogin(true);
    }
  }

  @Override
  public NewsForm getModel() {
    return form;
  }

}
