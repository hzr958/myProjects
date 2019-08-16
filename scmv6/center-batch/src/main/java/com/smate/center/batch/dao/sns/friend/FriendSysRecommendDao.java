package com.smate.center.batch.dao.sns.friend;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.friend.FriendSysRecommend;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * @author lcw
 * 
 */
@Repository
public class FriendSysRecommendDao extends SnsHibernateDao<FriendSysRecommend, Long> {

  public FriendSysRecommend getFriendSysRecommend(Long psnId, Long tempPsnId) {
    String hql = "from FriendSysRecommend where psnId=? and tempPsnId=?";
    return findUnique(hql, psnId, tempPsnId);
  }

  public boolean isMax(Long psnId) throws Exception {
    String hql = "select count(id) from FriendSysRecommend where psnId=?";
    return (Long) findUnique(hql, psnId) >= 100 ? true : false;
  }

  @SuppressWarnings("rawtypes")
  public FriendSysRecommend getRecommendMin(Long psnId) throws Exception {
    String hql = "from FriendSysRecommend where psnId=? order by score asc";
    Query query = createQuery(hql, psnId);
    query.setMaxResults(1);
    List list = query.list();
    return CollectionUtils.isNotEmpty(list) ? (FriendSysRecommend) list.get(0) : null;
  }
}
