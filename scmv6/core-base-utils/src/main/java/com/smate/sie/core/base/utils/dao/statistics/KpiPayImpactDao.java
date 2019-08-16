package com.smate.sie.core.base.utils.dao.statistics;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.statistics.KpiPayImpact;

/**
 * 影响力付费表Dao
 * 
 * @author hd
 *
 */
@Repository
public class KpiPayImpactDao extends SieHibernateDao<KpiPayImpact, Long> {

  /**
   * 统计需要处理的单位总数
   * 
   * @return
   */
  public Long cntNeedDeal(Date nowDate) {
    String hql = "select count(insId) from KpiPayImpact t where t.status = 1 and t.endDate >= ?";
    Object[] objects = new Object[] {nowDate};
    return findUnique(hql, objects);
  }

  @SuppressWarnings("unchecked")
  public Page<KpiPayImpact> LoadNeedDealRecords(Date nowDate, Page<KpiPayImpact> page) {
    String hql = "from KpiPayImpact t where t.status = 1 and t.endDate >= ? order by insId asc";
    Object[] objects = new Object[] {nowDate};
    Query q = createQuery(hql, objects);
    if (page.isAutoCount()) {// 默认进行总量统计
      long totalCount = countHqlResult(hql, objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<KpiPayImpact> result = q.list();
    page.setResult(result);
    return page;
  }
}
