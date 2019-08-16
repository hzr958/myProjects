package com.smate.web.dyn.action.news;

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
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.service.news.NewsBaseService;
import com.smate.web.dyn.service.news.NewsOptService;

/**
 * 新闻相关Action
 * 
 * @author ajb
 *
 */
@Results({@Result(name = "news_main", location = "/WEB-INF/jsp/news/news_main.jsp"),
    @Result(name = "news_main_list", location = "/WEB-INF/jsp/news/news_main_list.jsp"),
    @Result(name = "news_detail", location = "/WEB-INF/jsp/news/news_detail.jsp"),
    @Result(name = "news_not_exists", location = "/WEB-INF/jsp/news/news_not_exists.jsp"),
    @Result(name = "dyn_res_recommend", location = "/WEB-INF/jsp/news/dyn_res_recommend.jsp"),
    @Result(name = "mobile_dyn_res_recommend", location = "/WEB-INF/jsp/mobile/news/mobile_dyn_res_recommend.jsp")})
public class NewsAction extends ActionSupport implements Preparable, ModelDriven<NewsForm> {

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
  @Actions({@Action("/dynweb/news/main")})
  public String showNewsMain() {
    try {

    } catch (Exception e) {
      logger.error("进入新闻主页异常,userId=" + form.getPsnId(), e);
    }
    return "news_main";
  }

  /**
   * 获取新闻列表
   *
   * @return
   */
  @Actions({@Action("/dynweb/news/ajaxnewslist")})
  public String ajaxNewsList() {
    try {
      newsBaseService.findNewsList(form);
    } catch (Exception e) {
      logger.error("查找新闻列表,userId=" + form.getPsnId(), e);
    }
    return "news_main_list";
  }

  /**
   * 新闻详情页面
   * 
   * @return
   */
  @Actions({@Action("/dynweb/news/details")})
  public String viewNewsDetail() {
    try {
      Long newsId = form.getNewsId();
      form.setNewsId(newsId);
      if (!newsOptService.checkNews(newsId)) {
        return "news_not_exists";
      }
      // 增加访问记录
      newsOptService.addNewsView(form);
      newsBaseService.viewNewsDetails(form);
    } catch (Exception e) {
      logger.error("进入新闻详情异常,userId=" + form.getPsnId(), e);
    }
    return "news_detail";
  }

  @Actions({@Action("/dynweb/news/ajaxnewsrecommendshowindyn")})
  public String getNewsRecommendShowInDyn() {
    try {
      form.setPageSize(1);
      if (form.getPsnId() > 0) {
        newsBaseService.findNewsRcmd(form);
      }
    } catch (Exception e) {
      logger.error("获取首页动态基金推荐出错,psnId=" + form.getPsnId(), e);
    }
    if ("mobile".equals(form.getForm())) {
      return "mobile_dyn_res_recommend";
    }
    return "dyn_res_recommend";
  }

  /**
   * 首页新闻推荐 不感兴趣操作
   * 
   * @param pubOperateVO
   * @return
   */
  @Action("/dynweb/news/ajaxuninterestedcmd")
  public String notViewFundRecommend() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long newsId = form.getNewsId();
    Long psnId = form.getPsnId();
    try {
      if (psnId > 0) {
        newsBaseService.insertNewsRecmRecord(psnId, newsId);
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("首页动态推荐新闻不感兴趣操作出错,psnId=" + psnId + ",newsId=" + newsId, e);
    }
    Struts2Utils.renderJson(map, "Encoding:UTF-8");
    return null;
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
