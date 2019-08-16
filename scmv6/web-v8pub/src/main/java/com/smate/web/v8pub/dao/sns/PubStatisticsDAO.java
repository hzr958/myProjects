package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubStatisticsPO;

/**
 * 个人库成果操作统计Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PubStatisticsDAO extends SnsHibernateDao<PubStatisticsPO, Long> {
  /**
   * 获取成果的阅读总数
   * 
   * @param pubIds
   * @return
   */
  public Long findPubReadCounts(List<Long> pubIds) {
    String hql = "select sum(t.readCount) from PubStatisticsPO t where t.pubId in (:pubIds)";
    return (Long) super.createQuery(hql).setParameterList("pubIds", pubIds).uniqueResult();
  }

  public PubStatisticsPO getPubStatisticsById(Long pubId) {
    String hql = " from PubStatisticsPO t where t.pubId = :pubId";
    return (PubStatisticsPO) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public Long countPubTotalCitations(List<Long> pubIdList) {
    String hql = "select sum(t.refCount) from PubStatisticsPO t where t.pubId in(:pubIdList)";
    return (Long) super.createQuery(hql).setParameterList("pubIdList", pubIdList).uniqueResult();
  }

}
