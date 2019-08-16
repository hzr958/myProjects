package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.psn.FriendSysRecommendScore;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * @author lcw
 * 
 */
@Repository
public class FriendSysRecommendScoreDao extends SnsHibernateDao<FriendSysRecommendScore, Long> {

  public boolean isRecommendScore(Long psnId, Long recommendPsnId, Integer type) throws DaoException {
    String hql = "select count(id) from FriendSysRecommendScore where psnId=? and recommendPsnId=? and recommendType=?";
    return (Long) findUnique(hql, psnId, recommendPsnId, type) > 0 ? true : false;
  }

  @SuppressWarnings("unchecked")
  public Page<Long> findRecommendScoreByPsnId(Page<Long> page) {
    String hql = "select distinct psnId from FriendSysRecommendScore order by psnId asc";
    Query q = createQuery(hql);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql);
      page.setTotalCount(totalCount);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    page.setResult(q.list());
    return page;
  }

  public Page<FriendSysRecommendScore> findRecommendScore(Long psnId, Page<FriendSysRecommendScore> page) {
    String hql =
        "from FriendSysRecommendScore where psnId=? and recommendPsnId not in(select t2.friendPsnId from Friend t2 where t2.psnId=?) order by psnId asc";
    return findPage(page, hql, psnId, psnId);
  }

  public FriendSysRecommendScore findRecommendScore(Long psnId, Long recommendPsnId, Integer type) {
    String hql = "from FriendSysRecommendScore where psnId=? and recommendPsnId=? and recommendType=?";
    return findUnique(hql, new Object[] {psnId, recommendPsnId, type});
  }

  public List<FriendSysRecommendScore> findScore(Long psnId, Long recommendPsnId, Integer type) {
    String hql = "from FriendSysRecommendScore where psnId=? and recommendPsnId=? and recommendType not in(?)";
    return find(hql, new Object[] {psnId, recommendPsnId, type});
  }

  public void deleteAll() throws DaoException {
    super.update("truncate table psn_friend_recommend_score");
  }

  public void deleteByPsnId(Long psnId) throws DaoException {
    super.createQuery("delete from FriendSysRecommendScore  where psnId=?", psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<FriendSysRecommendScore> getFriendSysRecommendScore(Long psnId) throws DaoException {
    return super.createQuery(" from FriendSysRecommendScore where psnId=? or recommendPsnId=?", psnId, psnId).list();
  }

}
