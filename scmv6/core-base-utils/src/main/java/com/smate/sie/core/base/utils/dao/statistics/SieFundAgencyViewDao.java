package com.smate.sie.core.base.utils.dao.statistics;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.SieConstFundAgencyView;

/**
 * 
 * @author lijianming
 * @descript 资助机构dao
 * 
 */
@Repository
public class SieFundAgencyViewDao extends SieHibernateDao<SieConstFundAgencyView, Long> {

  // 统计视图表所有的基金机构
  public Long countAllAgency() {
    String hql = "select count(t.agencyId) from SieConstFundAgencyView t";
    Query query = super.createQuery(hql);
    return (Long) query.uniqueResult();
  }

  public SieConstFundAgencyView getAgencyById(Long agencyId) {
    String hql = "from SieConstFundAgencyView t where t.agencyId = :agencyId";
    Query query = super.createQuery(hql).setParameter("agencyId", agencyId);
    return (SieConstFundAgencyView) query.uniqueResult();
  }
}
