package com.smate.sie.center.task.dao;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieDataSrvPubTmpRefresh;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 成果
 * 
 * @author 叶星源
 * 
 */
@Repository
public class SieDataSrvPubTmpRefreshDao extends SieHibernateDao<SieDataSrvPubTmpRefresh, Long> {

  @SuppressWarnings("unchecked")
  public List<SieDataSrvPubTmpRefresh> queryNeedRefresh(int maxSize) {
    return super.createQuery("from SieDataSrvPubTmpRefresh t where t.status=0").setMaxResults(maxSize).list();
  }

}
