package com.smate.center.task.dao.fund.sns;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.sns.FundRecommendSeniority;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class FundRecommendSeniorityDao extends SnsHibernateDao<FundRecommendSeniority, Long> {
  public FundRecommendSeniority getSeniorityByPsnId(Long psnId) {
    String hql = "from FundRecommendSeniority where psnId=:psnId";
    return (FundRecommendSeniority) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  public void updateSeniorityCode(Long psnId, Integer code) {
    String hql = "update FundRecommendSeniority set code=:code where psnId=:psnId";
    super.createQuery(hql).setParameter("code", code).setParameter("psnId", psnId).executeUpdate();
  }
}
