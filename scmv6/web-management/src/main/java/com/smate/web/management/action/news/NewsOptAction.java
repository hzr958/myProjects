package com.smate.web.management.action.news;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.news.NewsForm;
import com.smate.web.management.service.news.NewsOptService;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 新闻操作相关Action
 * 
 * @author ajb
 *
 */
@Results({@Result(name = "news_main", location = "/WEB-INF/jsp/news/news_main.jsp"),
    @Result(name = "news_edit", location = "/WEB-INF/jsp/news/news_edit.jsp"),})
public class NewsOptAction extends ActionSupport implements Preparable, ModelDriven<NewsForm> {

  private static final long serialVersionUID = -9090557623490845340L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private NewsForm form;
  @Autowired
  private NewsOptService newsOptService;


  /**
   * 获取动态主页
   * 
   * @return
   */
  @Actions({@Action("/scmmanagement/news/main")})
  public String showNewsMain() {

    return "news_main";
  }

  /**
   * 编辑新闻
   *
   * @return
   */
  @Actions({@Action("/scmmanagement/news/edit")})
  public String editNews() {
    try {
      if (NumberUtils.isNotNullOrZero(form.getNewsId())) {
        newsOptService.edit(form);
      }
    } catch (Exception e) {
      logger.error("进入新闻编辑页面异常,userId=" + form.getPsnId(), e);
    }
    return "news_edit";
  }
  /**
   * 编辑新闻
   *
   * @return
   */
  @Actions({@Action("/scmmanagement/news/save")})
  public String saveNews() {
    Map map = new HashMap<>();
    try {
        newsOptService.save(form);
        map.put("result","success");
        map.put("des3NewsId",form.getDes3NewsId());
    } catch (Exception e) {
      logger.error("保存新闻消息异常异常,userId=" + form.getPsnId(), e);
      map.put("result","error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 编辑新闻
   *
   * @return
   */
  @Actions({@Action("/scmmanagement/news/publish")})
  public String publish() {
    Map map = new HashMap<>();
    try {
      if(NumberUtils.isNotNullOrZero(form.getNewsId())){
        newsOptService.publish(form);
        map.put("result","success");
      }else{
        map.put("result","exist");
      }

    } catch (Exception e) {
      logger.error("发布新闻异常异常,userId=" + form.getPsnId(), e);
      map.put("result","error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }


  /**
   * 新闻删除
   *
   * @return
   */
  @Actions({@Action("/scmmanagement/news/ajaxdeletenews")})
  public String deleteNews() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = form.getPsnId();
    try {
      if (psnId > 0 && StringUtils.isNotBlank(form.getDes3NewsIds())) {
        String[] tmpNewsIds = StringUtils.split(form.getDes3NewsIds(), ",");
        int deleteCount = 0;
        for (String des3NewsId : tmpNewsIds) {
          Long newsId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3NewsId));
          form.setNewsId(newsId);
          if (!newsOptService.checkNews(newsId)) {
            map.put("result", "notexist");
          } else {
            newsOptService.deleteNews(newsId);
            deleteCount += 1;
          }
        }
        if (deleteCount > 0) {
          map.put("result", "success");
          map.put("count", deleteCount);
        } else {
          map.put("result", "error");
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("新闻删除操作出错,psnId=" + psnId + ",newsId=" + form.getNewsId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  /**
   * 新闻 上移/下移
   *
   * @return
   */
  @Actions({@Action("/scmmanagement/news/ajaxchangeseqno")})
  public String changeSeqNo() {
    Map<String, Object> map = new HashMap<String, Object>();

    String des3NewsId = form.getDes3NewsId();
    String nextDes3NewsId = form.getNextDes3NewsId();
    if (StringUtils.isNotBlank(des3NewsId) && StringUtils.isNotBlank(nextDes3NewsId)) {
      Long newsId = form.getNewsId();
      Long nextNewsId = Long.valueOf(Des3Utils.decodeFromDes3(nextDes3NewsId));
      if (newsOptService.checkNews(newsId) && newsOptService.checkNews(nextNewsId)) {
        try {
          newsOptService.changeNewsSeqno(newsId, nextNewsId);
          map.put("result", "success");
        } catch (Exception e) {
          map.put("result", "error");
          logger.error("新闻上移/下移操作出错,psnId=" + form.getPsnId() + ",newsId=" + form.getNewsId(), e);
        }
      } else {
        map.put("result", "error");
      }
    } else {
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
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
