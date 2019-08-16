package com.smate.sie.core.base.utils.dao.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseShare;

/**
 * 
 * @author hd
 *
 */
@Repository
public class KpiImpactBaseShareDao extends SieHibernateDao<KpiImpactBaseShare, Long> {

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getCountByItems(Date day, Integer keyType, Long insId, String item) {
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append("select new Map(nvl( t.").append(item).append(",'其他') as ").append(item);
    hql.append(",count(t.id) as count) ");
    hql.append(" from KpiImpactBaseShare t where t.insId=? and t.keyType=? and trunc(t.timeRecords)=trunc(?) ");
    params.add(insId);
    params.add(keyType);
    params.add(day);
    hql.append(" group by nvl(t.").append(item).append(",'其他')");
    return this.createQuery(hql.toString(), params.toArray()).list();
  }

  public Long getCountByDate(Date day, Integer keyType, Long insId) {
    StringBuilder hql = new StringBuilder();
    hql.append(
        "select count(t.id) from KpiImpactBaseShare t where t.insId=? and t.keyType=? and trunc(t.timeRecords)=trunc(?) ");
    Object[] objects = new Object[] {insId, keyType, day};
    return super.findUnique(hql.toString(), objects);
  }

  public Long getCountByDateTime(Long insId, Date startDate, Date endDate) {
    String hql =
        "select count(t.id) from KpiImpactBaseShare t where t.insId=:insId and t.timeRecords >= :startDate and t.timeRecords < :endDate";
    Query query = super.createQuery(hql).setParameter("insId", insId).setParameter("startDate", startDate)
        .setParameter("endDate", endDate);
    return (Long) query.uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getCountByDataAndMon(Long insId, Integer keyType, Date beginTime, Date endTime) {
    StringBuilder hql = new StringBuilder();
    hql.append("select new Map(t.keyCode as item ,count(t.id) as count) ");
    hql.append(
        " from KpiImpactBaseShare t where t.insId=? and t.keyType=? and  t.timeRecords>=? and t.timeRecords<? group by t.keyCode ");
    Object[] objects = new Object[] {insId, keyType, beginTime, endTime};
    return this.createQuery(hql.toString(), objects).list();
  }

  @SuppressWarnings("unchecked")
  public KpiImpactBaseShare getByKeyCodeAndType(Long insId, Integer keyType, Long keyCode, Date beginTime,
      Date endTime) {
    StringBuilder hql = new StringBuilder();
    hql.append(
        " from KpiImpactBaseShare t where t.insId=? and t.keyType=? and  t.timeRecords>=? and t.timeRecords<? and t.keyCode=?");
    Object[] objects = new Object[] {insId, keyType, beginTime, endTime, keyCode};
    List<KpiImpactBaseShare> list = this.createQuery(hql.toString(), objects).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
