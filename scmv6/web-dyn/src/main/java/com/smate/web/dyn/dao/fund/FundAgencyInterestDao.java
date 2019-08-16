package com.smate.web.dyn.dao.fund;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.fund.FundAgencyInterest;

/**
 * 关注资助机构DAO
 * 
 * @author wsn
 * @date Nov 13, 2018
 */
@Repository
public class FundAgencyInterestDao extends SnsHibernateDao<FundAgencyInterest, Long> {

  /**
   * 查询人员关注某个资助机构记录
   * 
   * @param psnId
   * @param agencyId
   * @return
   */
  public FundAgencyInterest findInterestAgencyByPsnIdAndAgencyId(Long psnId, Long agencyId) {
    String hql = " from FundAgencyInterest t where t.psnId = :psnId and t.agencyId = :agencyId and t.status = 1";
    return (FundAgencyInterest) super.createQuery(hql).setParameter("psnId", psnId).setParameter("agencyId", agencyId)
        .uniqueResult();
  }

  /**
   * 是否已收藏过资助机构
   * 
   * @param psnId
   * @param agencyId
   * @return
   */
  public boolean hasCollectedAgency(Long psnId, Long agencyId) {
    String hql =
        "select count(1) from FundAgencyInterest t where t.psnId = :psnId and t.agencyId = :agencyId and t.status = 1";
    Long count =
        (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("agencyId", agencyId).uniqueResult();
    if (count > 0) {
      return true;
    }
    return false;
  }

}
