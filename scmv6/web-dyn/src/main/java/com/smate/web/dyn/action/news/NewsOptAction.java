package com.smate.web.dyn.action.news;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.service.news.NewsOptService;

/**
 * 新闻操作Action
 * 
 * @author YHX
 *
 */
public class NewsOptAction extends ActionSupport implements Preparable, ModelDriven<NewsForm> {

  private static final long serialVersionUID = -9090557623490845340L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private NewsForm form;
  @Autowired
  private NewsOptService newsOptService;


  /**
   * 新闻 赞/取消赞
   * 
   * @return
   */
  @Actions({@Action("/dynweb/news/ajaxupdatenewsaward")})
  public String updateNewsAward() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = form.getPsnId();
    try {
      Long newsId = form.getNewsId();
      if (psnId > 0 && newsOptService.checkNews(newsId)) {
        newsOptService.updateNewsAward(form);
        map.put("result", "success");
        map.put("awardTimes", form.getAwardTimes());
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("新闻赞/取消赞操作出错,psnId=" + psnId + ",newsId=" + form.getNewsId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  /**
   * 检查新闻是否删除
   */
  @Action("/dynweb/news/ajaxCheckNews")
  public String ajaxCheckNews() throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    if (form.getNewsId() != null) {
      try {
        Boolean flag = newsOptService.checkNews(form.getNewsId());
        if (!flag) {
          result.put("status", "isDel");
        } else {
          result.put("status", "success");
        }
      } catch (Exception e) {
        result.put("status", "error");
        logger.error("检查新闻是否被删除异常,newsId=" + form.getNewsId(), e);
      }
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");

    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new NewsForm();
    }
    form.setPsnId(SecurityUtils.getCurrentUserId());
  }

  @Override
  public NewsForm getModel() {
    return form;
  }

}
