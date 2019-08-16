package com.smate.web.dyn.dao.news;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.news.NewsLike;

/**
 * 新闻赞实体dao
 * 
 * @author ajb
 *
 */

@Repository
public class NewsLikeDao extends SnsHibernateDao<NewsLike, Long> {

  /**
   * 用户赞/取消赞记录
   * 
   */
  public int isLike(Long newsId, Long likePsnId, Integer status) {
    String hql =
        "select count(t.id) from NewsLike t where t.newsId=:newsId and t.likePsnId=:likePsnId and t.status=:status";
    Long count = (Long) super.createQuery(hql).setParameter("newsId", newsId).setParameter("likePsnId", likePsnId)
        .setParameter("status", status).uniqueResult();
    return count.intValue();
  }

  public NewsLike findByNewsIdAndPsnId(Long newsId, Long psnId) {
    String hql = "from NewsLike t where t.newsId =:newsId and t.likePsnId =:likePsnId";
    Object object =
        this.createQuery(hql).setParameter("newsId", newsId).setParameter("likePsnId", psnId).uniqueResult();
    if (object != null) {
      return (NewsLike) object;
    }
    return null;
  }


  public long getLikeRecord(Long newsId, Long likePsnId) {
    String hql = "select count(*) from NewsLike t where t.newsId=:newsId and t.likePsnId=:likePsnId and t.status=1";
    return (Long) super.createQuery(hql).setParameter("newsId", newsId).setParameter("likePsnId", likePsnId)
        .uniqueResult();
  }
}
