package com.smate.sie.center.task.dao;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieDataSrvPatTmpRefresh;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专利
 * 
 * @author 叶星源
 * 
 */
@Repository
public class SieDataSrvPatTmpRefreshDao extends SieHibernateDao<SieDataSrvPatTmpRefresh, Long> {

  @SuppressWarnings("unchecked")
  public List<SieDataSrvPatTmpRefresh> queryNeedRefresh(int maxSize) {
    return super.createQuery("from SieDataSrvPatTmpRefresh t where t.status=0").setMaxResults(maxSize).list();
  }

}
