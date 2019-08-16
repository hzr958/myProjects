package com.smate.sie.core.base.utils.dao.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseIndex;

/**
 * 单位访问基表Dao
 * 
 * @author hd
 *
 */
@Repository
public class KpiImpactBaseIndexDao extends SieHibernateDao<KpiImpactBaseIndex, Long> {

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getCountByItems(Date day, Long insId, String item) {
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append("select new Map(nvl( t.").append(item).append(",'其他') as ").append(item);
    hql.append(",count(t.id) as count) ");
    hql.append(" from KpiImpactBaseIndex t where t.insId=? and trunc(t.timeRecords)=trunc(?) ");
    params.add(insId);
    params.add(day);
    hql.append(" group by nvl(t.").append(item).append(",'其他')");
    return this.createQuery(hql.toString(), params.toArray()).list();
  }

  public Long getCountByDate(Date day, Long insId) {
    StringBuilder hql = new StringBuilder();
    hql.append("select count(t.id) from KpiImpactBaseIndex t where t.insId=? and trunc(t.timeRecords)=trunc(?) ");
    Object[] objects = new Object[] {insId, day};
    return super.findUnique(hql.toString(), objects);
  }

  public Long getCountByDateTime(Long insId, Date startDate, Date endDate) {
    String hql =
        "select count(t.id) from KpiImpactBaseIndex t where t.insId= :insId and t.timeRecords >= :startDate and t.timeRecords < :endDate";
    Query query = super.createQuery(hql).setParameter("insId", insId).setParameter("startDate", startDate)
        .setParameter("endDate", endDate);
    return (Long) query.uniqueResult();
  }
}
