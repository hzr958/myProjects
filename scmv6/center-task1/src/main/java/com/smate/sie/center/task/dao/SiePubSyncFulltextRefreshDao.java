package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SiePubSyncFulltextRefresh;

/**
 * 
 * @author yxy
 *
 */
@Repository
public class SiePubSyncFulltextRefreshDao extends SieHibernateDao<SiePubSyncFulltextRefresh, Long> {

  /**
   * 根据sns的pubId获取 同步刷新对象
   */
  @SuppressWarnings("unchecked")
  public SiePubSyncFulltextRefresh getBySnsPubId(Long snsPubId) {
    SiePubSyncFulltextRefresh tempResult = null;
    String hql = "from SiePubSyncFulltextRefresh t where t.snsPubId=:snsPubId";
    List<SiePubSyncFulltextRefresh> result = super.createQuery(hql).setParameter("snsPubId", snsPubId).list();
    if (result.size() > 0) {
      tempResult = result.get(0);
    }
    return tempResult;
  }

  @SuppressWarnings("unchecked")
  public List<SiePubSyncFulltextRefresh> getByTypeOnSize(int first, int batchSize) {
    String hql = "from SiePubSyncFulltextRefresh t ";
    return super.createQuery(hql).setFirstResult(first * batchSize).setMaxResults(batchSize).list();
  }

}
