package com.smate.web.dyn.dao.pub.rol;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.web.dyn.model.pub.rol.SnsSyncPubStatistics;

/**
 * SNS同步成果相关统计.
 * 
 * @author xys
 * 
 */
@Repository
public class SnsSyncPubStatisticsDao extends RolHibernateDao<SnsSyncPubStatistics, Long> {


  /**
   * 查找记录
   * 
   * @param rolPubId
   * @param scmPubId
   * @return
   */
  public SnsSyncPubStatistics findPubStatistics(Long rolPubId, Long scmPubId) {
    StringBuilder hql = new StringBuilder();
    hql.append("from SnsSyncPubStatistics t where t.siePubId = ? and t.snsPubId = ?");
    return (SnsSyncPubStatistics) this.createQuery(hql.toString(), rolPubId, scmPubId).uniqueResult();
  }


  /**
   * 获取指定统计类型的统计数.
   * 
   * @param siePubId
   * @param fieldName
   * @return
   */
  public Long getSnsSyncPubStatistic(Long siePubId, String fieldName) {
    StringBuilder hql = new StringBuilder();
    hql.append("select ");
    hql.append(" sum(t.");
    hql.append(fieldName);
    hql.append(")");
    hql.append(" from SnsSyncPubStatistics t where t.siePubId=:siePubId");
    return (Long) super.createQuery(hql.toString()).setParameter("siePubId", siePubId).uniqueResult();
  }
}
