package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.PubFullTextRefresh;

@Repository
public class PubFullTextRefreshDao extends SieHibernateDao<PubFullTextRefresh, Long> {

  /**
   * 获取需要统计的记录
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubFullTextRefresh> loadNeedCountUnitId(int maxSize) {
    String hql = "from PubFullTextRefresh t where t.status=0";
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();
  }
}
