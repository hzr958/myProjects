package com.smate.sie.core.base.utils.dao.statistics;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.statistics.KpiPayBehave;

/**
 * 刷新社交行为付费表 Dao
 * 
 * @author hd
 *
 */
@Repository
public class KpiPayBehaveDao extends SieHibernateDao<KpiPayBehave, Long> {

  /**
   * 统计需要处理的单位总数
   * 
   * @return
   */
  public Long cntNeedDeal(Date nowDate) {
    String hql = "select count(insId) from KpiPayBehave t where t.status = 1 and t.endDate >= ?";
    Object[] objects = new Object[] {nowDate};
    return findUnique(hql, objects);
  }

  @SuppressWarnings("unchecked")
  public List<KpiPayBehave> LoadNeedDealRecords(int maxSize, Date nowDate) {
    String hql = "from KpiPayBehave t where t.status = 1 and t.endDate >= :nowDate";
    Query queryResult = super.createQuery(hql).setParameter("nowDate", nowDate);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();

  }

  @SuppressWarnings("unchecked")
  public Page<KpiPayBehave> LoadNeedDealRecords(Date nowDate, Page<KpiPayBehave> page) {
    String hql = "from KpiPayBehave t where t.status = 1 and t.endDate >= ? order by insId asc";
    Object[] objects = new Object[] {nowDate};
    Query q = createQuery(hql, objects);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<KpiPayBehave> result = q.list();
    page.setResult(result);
    return page;
  }
}
