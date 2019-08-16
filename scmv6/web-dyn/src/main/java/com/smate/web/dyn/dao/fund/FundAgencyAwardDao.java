package com.smate.web.dyn.dao.fund;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.fund.FundAgencyAward;

/**
 * 资助机构赞DAO
 * 
 * @author wsn
 * @date Nov 13, 2018
 */
@Repository
public class FundAgencyAwardDao extends SnsHibernateDao<FundAgencyAward, Long> {

  /**
   * 通过人员ID和资助机构ID获取赞记录
   * 
   * @param psnId
   * @param agencyId
   * @return
   */
  public FundAgencyAward findAgencyAwardByPsnIdAndAgencyId(Long psnId, Long agencyId) {
    String hql = " from FundAgencyAward t where t.psnId = :psnId and t.agencyId = :agencyId";
    return (FundAgencyAward) super.createQuery(hql).setParameter("psnId", psnId).setParameter("agencyId", agencyId)
        .uniqueResult();
  }

  /**
   * 获取已经赞过的资助机构ID
   * 
   * @param psnId
   * @param agencyIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findHasAwardAgencyIds(Long psnId, List<Long> agencyIds) {
    String hql =
        "select t.agencyId from FundAgencyAward t where t.psnId = :psnId and t.agencyId in (:agencyIds) and t.status = 1";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("agencyIds", agencyIds).list();
  }
}
