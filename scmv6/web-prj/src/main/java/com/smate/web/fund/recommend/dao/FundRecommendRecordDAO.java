package com.smate.web.fund.recommend.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.recommend.model.FundRecommendRecord;

/**
 * 基金推荐操作记录dao
 * 
 * @author yhx
 *
 */
@Repository
public class FundRecommendRecordDAO extends SnsHibernateDao<FundRecommendRecord, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getFundIdsByPsnId(Long psnId, Integer status) {
    String hql = "select t.fundId from FundRecommendRecord t where t.psnId = :psnId and t.status = :status";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

  public FundRecommendRecord findFundByFundIdAndPsnId(Long psnId, Long fundId) {
    String hql = "from FundRecommendRecord t where t.psnId = :psnId and t.fundId = :fundId";
    Object object = this.createQuery(hql).setParameter("fundId", fundId).setParameter("psnId", psnId).uniqueResult();
    if (object != null) {
      return (FundRecommendRecord) object;
    }
    return null;
  }
}
