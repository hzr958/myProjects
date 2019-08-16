package com.smate.sie.core.base.utils.dao.statistics;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.SieSTPubSyncRefresh;

@Repository
public class SieSTPubSyncRefreshDao extends SieHibernateDao<SieSTPubSyncRefresh, Long> {

  /**
   * 获取待处理单位
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieSTPubSyncRefresh> LoadNeedDealRecords(int maxSize) {
    String hql = "from SieSTPubSyncRefresh where status = 0 ";
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();

  }

}
