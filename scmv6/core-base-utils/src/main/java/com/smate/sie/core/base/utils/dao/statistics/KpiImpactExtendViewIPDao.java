package com.smate.sie.core.base.utils.dao.statistics;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendViewIP;

/**
 * 单位分析 访客数 Dao
 * 
 * @author ztg
 *
 */
@Repository
public class KpiImpactExtendViewIPDao extends SieHibernateDao<KpiImpactExtendViewIP, Long> {


  public void delTwoMonBeforeData(Long insId, Date beginTime) {
    String hql = "delete from KpiImpactExtendViewIP t where t.insId=:insId and t.stMonth<:beginTime";
    super.createQuery(hql).setParameter("insId", insId).setParameter("beginTime", beginTime).executeUpdate();
  }

  public void delTwoMonBeforeData(Long insId, String time) {
    String hql = "delete from KpiImpactExtendViewIP t where t.insId=? and to_number(t.stMonth)<=to_number(?)";
    Object[] objects = new Object[] {insId, time};
    super.createQuery(hql, objects).executeUpdate();
  }

  public Long getCountByDateTime(Long insId, String stMonth) {
    String hql =
        "select count(t.id) from KpiImpactExtendViewIP t where t.insId=:insId and t.stMonth = :stMonth and stBatch = 1";
    Query query = super.createQuery(hql).setParameter("insId", insId).setParameter("stMonth", stMonth);
    return (Long) query.uniqueResult();
  }
}
