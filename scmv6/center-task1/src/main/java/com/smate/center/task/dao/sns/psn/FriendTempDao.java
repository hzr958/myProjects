package com.smate.center.task.dao.sns.psn;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.FriendTemp;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class FriendTempDao extends SnsHibernateDao<FriendTemp, Long> {

  public Long getReqFriendCount(Long psnId) {
    String hql = "select count(*) from FriendTemp t where t.tempPsnId=:psnId"
        + " and t.psnId !=:psnId and not exists(select 1 from Friend f where f.psnId=:psnId and f.friendPsnId = t.psnId)";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

}
