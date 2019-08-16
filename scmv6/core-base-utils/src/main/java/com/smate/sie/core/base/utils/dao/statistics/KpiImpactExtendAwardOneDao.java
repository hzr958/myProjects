package com.smate.sie.core.base.utils.dao.statistics;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendAwardOne;

/**
 * 单位分析，赞数拓展表1 Dao
 * 
 * @author hd
 *
 */
@Repository
public class KpiImpactExtendAwardOneDao extends SieHibernateDao<KpiImpactExtendAwardOne, Long> {


  public void delTwoMonBeforeData(Long insId, Date beginTime) {
    String hql = "delete from KpiImpactExtendAwardOne t where t.insId=:insId and t.stDate<:beginTime";
    super.createQuery(hql).setParameter("insId", insId).setParameter("beginTime", beginTime).executeUpdate();
  }

  public Long getCountByDateTime(Long insId, Date startDate, Date endDate) {
    String hql =
        "select count(t.id) from KpiImpactExtendAwardOne t where t.insId=:insId and t.stDate >= :startDate and t.stDate < :endDate";
    Query query = super.createQuery(hql).setParameter("insId", insId).setParameter("startDate", startDate)
        .setParameter("endDate", endDate);
    return (Long) query.uniqueResult();
  }
}
