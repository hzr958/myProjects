package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.GroupPsnRecommend;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组待处理成果Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupPsnRecommendDao extends SnsHibernateDao<GroupPsnRecommend, Long> {

  public boolean ifInfoExists(Long groupId) {
    String hql = "select count(1) from GroupPsnRecommend t where t.groupId =:groupId";
    Long count = (Long) super.createQuery(hql).setParameter("groupId", groupId).uniqueResult();
    if (count == 0 || count == null) {
      return false;
    } else {
      return true;
    }
  }

  public void deleteInfo(Long groupId) {
    String hql = "delete GroupPsnRecommend t where t.groupId =:groupId ";
    super.createQuery(hql).setParameter("groupId", groupId).executeUpdate();
  }
}
