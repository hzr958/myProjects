package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SiePatFullTextRefresh;

@Repository
public class SiePatFullTextRefreshDao extends SieHibernateDao<SiePatFullTextRefresh, Long> {

  /**
   * 获取需要统计的记录
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePatFullTextRefresh> loadNeedCountUnitId(int maxSize) {
    String hql = "from SiePatFullTextRefresh t where t.status=0";
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();
  }
}
