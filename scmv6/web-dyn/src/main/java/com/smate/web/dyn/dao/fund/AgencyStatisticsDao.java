package com.smate.web.dyn.dao.fund;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.fund.AgencyStatistics;

/**
 * 资助机构操作统计DAO
 * 
 * @author wsn
 * @date Nov 13, 2018
 */
@Repository
public class AgencyStatisticsDao extends SnsHibernateDao<AgencyStatistics, Long> {

  /**
   * 查询资助机构的统计数信息
   * 
   * @param agencyId
   * @return
   */
  @SuppressWarnings("unchecked")
  public AgencyStatistics findAgencyStatistics(Long agencyId) {
    String hql = " from AgencyStatistics t where t.agencyId = :agencyId)";
    return (AgencyStatistics) super.createQuery(hql).setParameter("agencyId", agencyId).uniqueResult();
  }

}
