package com.smate.web.fund.recommend.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.recommend.model.FundRecommendConditions;

/**
 * 基金推荐条件DAO
 * 
 * @author WSN
 *
 *         2017年8月21日 上午11:14:29
 *
 */
@Repository
public class FundRecommendConditionsDao extends SnsHibernateDao<FundRecommendConditions, Long> {

  /**
   * 查找人员推荐条件
   * 
   * @param psnId
   * @return
   */
  public FundRecommendConditions findPsnFundRecommendConditions(Long psnId) {
    String hql = " from FundRecommendConditions t where t.psnId = :psnId";
    return (FundRecommendConditions) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }
}
