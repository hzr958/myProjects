package com.smate.sie.core.base.utils.dao.statistics;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.SiePsnRefresh;

/**
 * 人员统计日志Dao
 * 
 * @author hd
 *
 */
@Repository
public class SiePsnRefreshDao extends SieHibernateDao<SiePsnRefresh, Long> {

  /**
   * 获取需要统计的记录
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePsnRefresh> loadNeedCountPsnId(int maxSize) {
    String hql = "from SiePsnRefresh where status=0 order by priorCode desc";
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);

    return queryResult.list();
  }

  public void updateStatus() {
    super.createQuery("update SiePsnRefresh t set t.status = 0").executeUpdate();
  }

  public Long countNeedCountPsnId() {
    String hql = "select count(psnId) from SiePsnRefresh where status=0";
    return findUnique(hql);
  }

  public void deleteAll() {
    super.createQuery("delete from SiePsnRefresh t").executeUpdate();
  }
}
