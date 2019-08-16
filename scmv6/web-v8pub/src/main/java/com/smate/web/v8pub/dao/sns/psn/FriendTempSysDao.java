package com.smate.web.v8pub.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.psn.FriendTempSys;

/**
 * 系统推荐好友Dao.
 * 
 * zk
 */
@Repository
public class FriendTempSysDao extends SnsHibernateDao<FriendTempSys, Long> {

  /**
   * 获取指定psnId所对应操作过（已经忽略）人员Ids.
   */
  @SuppressWarnings("unchecked")
  public List<Long> findFriendTempSys(Long psnId) {
    String hql = "select distinct tempPsnId from FriendTempSys where psnId=:psnId and tempPsnId is not null";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
