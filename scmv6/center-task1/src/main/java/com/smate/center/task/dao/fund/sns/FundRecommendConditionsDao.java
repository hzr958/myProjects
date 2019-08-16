package com.smate.center.task.dao.fund.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.sns.FundRecommendConditions;
import com.smate.core.base.utils.data.SnsHibernateDao;

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

  @SuppressWarnings("unchecked")
  public List<FundRecommendConditions> getFundRecommendConditions(Integer maxSize) {
    String hql = "from FundRecommendConditions t where t.status=0";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }

  public void updateFundRecommendConditionStatus(Long psnId) {
    String hql = "update FundRecommendConditions set status=1 where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

}
