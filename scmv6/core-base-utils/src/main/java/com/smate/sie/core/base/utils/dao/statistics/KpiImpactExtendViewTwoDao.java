package com.smate.sie.core.base.utils.dao.statistics;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendViewTwo;

/**
 * 单位分析，阅读数拓展表2 Dao
 * 
 * @author hd
 *
 */
@Repository
public class KpiImpactExtendViewTwoDao extends SieHibernateDao<KpiImpactExtendViewTwo, Long> {

  public void delTwoMonBeforeData(Long insId, String time) {
    String hql = "delete from KpiImpactExtendViewTwo t where t.insId=? and to_number(t.time)<=to_number(?)";
    Object[] objects = new Object[] {insId, time};
    super.createQuery(hql, objects).executeUpdate();
  }

  public Long getCountByDateTime(Long insId, String time) {
    String hql = "select count(t.id) from KpiImpactExtendViewTwo t where t.insId=:insId and t.time = :time";
    Query query = super.createQuery(hql).setParameter("insId", insId).setParameter("time", time);
    return (Long) query.uniqueResult();
  }
}
