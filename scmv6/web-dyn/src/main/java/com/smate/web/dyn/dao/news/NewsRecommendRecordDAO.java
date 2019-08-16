package com.smate.web.dyn.dao.news;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.news.NewsRecommendRecord;

/**
 * 新闻推荐操作记录dao
 * 
 * @author yhx
 *
 */
@Repository
public class NewsRecommendRecordDAO extends SnsHibernateDao<NewsRecommendRecord, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getNewsIdsByPsnId(Long psnId, Integer status) {
    String hql = "select t.newsId from NewsRecommendRecord t where t.psnId = :psnId and t.status = :status";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

  public NewsRecommendRecord findRecordByNewsIdAndPsnId(Long psnId, Long newsId) {
    String hql = "from NewsRecommendRecord t where t.psnId = :psnId and t.newsId = :newsId";
    Object object = this.createQuery(hql).setParameter("newsId", newsId).setParameter("psnId", psnId).uniqueResult();
    if (object != null) {
      return (NewsRecommendRecord) object;
    }
    return null;
  }
}
