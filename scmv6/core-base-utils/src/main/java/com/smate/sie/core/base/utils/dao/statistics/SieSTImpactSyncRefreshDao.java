package com.smate.sie.core.base.utils.dao.statistics;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.SieSTImpactSyncRefresh;

@Repository
public class SieSTImpactSyncRefreshDao extends SieHibernateDao<SieSTImpactSyncRefresh, Long> {

  /**
   * 获取待处理单位
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieSTImpactSyncRefresh> LoadNeedDealRecords(int maxSize) {
    String hql = "from SieSTImpactSyncRefresh where status = 0 ";
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();

  }
}
