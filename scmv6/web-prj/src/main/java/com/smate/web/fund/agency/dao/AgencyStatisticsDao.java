package com.smate.web.fund.agency.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.agency.model.AgencyStatistics;

/**
 * 资助机构操作统计DAO
 * 
 * @author wsn
 * @date Nov 13, 2018
 */
@Repository
public class AgencyStatisticsDao extends SnsHibernateDao<AgencyStatistics, Long> {

  /**
   * 批量查询资助机构的统计数信息
   * 
   * @param agencyIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<AgencyStatistics> findAgencyStatistics(List<Long> agencyIds) {
    String hql = " from AgencyStatistics t where t.agencyId in (:agencyIds)";
    return super.createQuery(hql).setParameterList("agencyIds", agencyIds).list();
  }

  /**
   * 减少资助机构关注数
   * 
   * @param agencyIds
   * @return
   */
  public void reduceAgencyListStatistics(List<Long> agencyIds) {
    String hql =
        " update AgencyStatistics t set interestSum = CASE WHEN (NVL(interestSum ,0)-1)>0 THEN (NVL(interestSum ,0)-1)  ELSE 0 END   where t.agencyId in (:agencyIds)";
    super.createQuery(hql).setParameterList("agencyIds", agencyIds).executeUpdate();
  }

  /**
   * 减少资助机构关注数
   * 
   * @param agencyIds
   * @return
   */
  public void reduceAgencyStatistics(Long agencyId) {
    String hql =
        " update AgencyStatistics t set interestSum=CASE WHEN (NVL(interestSum ,0)-1)>0 THEN (NVL(interestSum ,0)-1)  ELSE 0 END  where t.agencyId=:agencyId";
    super.createQuery(hql).setParameter("agencyId", agencyId).executeUpdate();
  }

  /**
   * 增加资助机构关注数
   * 
   * @param agencyIds
   * @return
   */
  public void addAgencyStatistics(Long agencyId) {
    String hql = " from AgencyStatistics t  where t.agencyId=:agencyId";
    AgencyStatistics angency =
        (AgencyStatistics) super.createQuery(hql).setParameter("agencyId", agencyId).uniqueResult();
    if (angency == null) {
      angency = new AgencyStatistics();
      angency.setAgencyId(agencyId);
      angency.setAwardSum(0L);
      angency.setShareSum(0L);
      angency.setInterestSum(1L);
      super.save(angency);
    } else {
      String hqlUdpate = " update AgencyStatistics t set interestSum=NVL(interestSum ,0)+1 where t.agencyId=:agencyId";
      super.createQuery(hqlUdpate).setParameter("agencyId", agencyId).executeUpdate();
    }
  }
}
