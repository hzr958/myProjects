package com.smate.web.dyn.dao.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.psn.FriendTemp;

/**
 * 好友分组数据层.
 * 
 * @author lhd
 */
@Repository
public class FriendTempDao extends SnsHibernateDao<FriendTemp, Long> {
  /**
   * 获取好友请求数量
   * 
   * @param psnId
   * @return
   */
  public Long getTempPsnCount(Long psnId) {
    String hql = "select count(distinct psnId) from FriendTemp t where t.tempPsnId=:psnId"
        + " and t.psnId !=:psnId and not exists(select 1 from Friend f where f.psnId=:psnId and f.friendPsnId = t.psnId)"
        + " and exists(select 1 from Person p where p.personId = t.psnId)";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (count == null) {
      return 0L;
    } else {
      return count;
    }

  }

}
