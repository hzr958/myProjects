package com.smate.web.fund.recommend.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.recommend.model.FundRecommendSeniority;

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
