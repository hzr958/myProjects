package com.smate.center.merge.dao.pub;

import com.smate.center.merge.model.sns.pub.PsnPub;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 个人成果 dao.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Repository
public class PsnPubDao extends SnsHibernateDao<PsnPub, Long> {
  /**
   * 获取成果 通过拥有者ID.
   * 
   * @param ownerPsnId not null
   * @return list
   */
  @SuppressWarnings("unchecked")
  public List<PsnPub> getPubsByOwnerPsnId(Long ownerPsnId) throws Exception {
    String hql = " from PsnPub t where  t.ownerPsnId=:ownerPsnId and t.status=0";
    return super.createQuery(hql).setParameter("ownerPsnId", ownerPsnId).list();
  }
}
