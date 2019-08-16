package com.smate.web.management.dao.news;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.news.NewsBase;
import org.springframework.stereotype.Repository;

/**
 * 新闻实体dao
 * 
 * @author ajb
 *
 */

@Repository
public class NewsBaseDao extends SnsHibernateDao<NewsBase, Long> {

  public Long getId(){
    String sql = "select  SEQ_V_NEWS_BASE.nextVal  from dual " ;
    return super.queryForLong(sql);
  }

}
