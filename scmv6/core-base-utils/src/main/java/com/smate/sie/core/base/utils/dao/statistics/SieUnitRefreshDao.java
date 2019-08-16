package com.smate.sie.core.base.utils.dao.statistics;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.SieUnitRefresh;

/**
 * 部门统计日志Dao
 * 
 * @author hd
 *
 */
@Repository
public class SieUnitRefreshDao extends SieHibernateDao<SieUnitRefresh, Long> {

  /**
   * 获取需要统计的记录
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieUnitRefresh> loadNeedCountUnitId(int maxSize) {
    String hql = "from SieUnitRefresh where status=0 order by priorCode desc";
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);

    return queryResult.list();
  }

  public void updateStatus() {
    super.createQuery("update SieUnitRefresh t set t.status = 0").executeUpdate();
  }

  public Long countNeedCountUnitId() {
    String hql = "select count(unitId) from SieUnitRefresh where status=0";
    return findUnique(hql);
  }

  public void deleteAll() {
    super.createQuery("delete from SieUnitRefresh t").executeUpdate();
  }
}
