package com.smate.center.open.dao.news;

import com.smate.center.open.model.news.NewsBase;
import com.smate.core.base.utils.data.SnsHibernateDao;
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
