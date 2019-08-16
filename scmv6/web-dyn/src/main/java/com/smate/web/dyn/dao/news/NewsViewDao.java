package com.smate.web.dyn.dao.news;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.news.NewsView;

/**
 * 新闻 访问实体dao
 * 
 * @author ajb
 *
 */

@Repository
public class NewsViewDao extends SnsHibernateDao<NewsView, Long> {

  @SuppressWarnings("unchecked")
  public NewsView findNewsView(Long newsId, Long psnId, Long formateDate, String ip) {
    String hql = "from NewsView t where t.viewPsnId = ? and t.newsId = ? and t.formateDate = ? and ";
    List<NewsView> list = null;
    if (ip != null) {
      hql += " t.ip = ? ";
      list = super.createQuery(hql, psnId, newsId, formateDate, ip).list();
    } else {
      hql += " t.ip is null ";
      list = super.createQuery(hql, psnId, newsId, formateDate).list();
    }

    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
