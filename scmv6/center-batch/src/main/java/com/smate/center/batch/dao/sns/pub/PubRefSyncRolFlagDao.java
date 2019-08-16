package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PubRefSyncRolFlag;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 持久化同步文献ROL标记.
 * 
 * @author WeiLong Peng
 *
 */
@Repository
public class PubRefSyncRolFlagDao extends SnsHibernateDao<PubRefSyncRolFlag, Long> {

  /**
   * 查询需要同步到ROL的期刊文献.
   * 
   * @param startId
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubRefSyncRolFlag> queryNeedSyncPubRef(Long startId, int size) throws DaoException {
    String hql = "from PubRefSyncRolFlag t where t.refId > ? and t.flag = 0 order by t.refId asc";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }
}
