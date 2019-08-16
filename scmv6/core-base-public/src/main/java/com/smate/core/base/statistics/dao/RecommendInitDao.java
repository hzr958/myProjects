package com.smate.core.base.statistics.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.smate.core.base.statistics.model.RecommendInit;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class RecommendInitDao extends SnsHibernateDao<RecommendInit, Long> {

  public RecommendInit getRecommendInit(Long psnId) {
    String hql = "from RecommendInit where psnId=:psnId";
    return (RecommendInit) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  public void saveRecommendInit(RecommendInit init) {
    String hql = "from RecommendInit where psnId=:psnId";
    RecommendInit initOld =
        (RecommendInit) super.createQuery(hql).setParameter("psnId", init.getPsnId()).uniqueResult();
    if (initOld != null) {
      Integer pubInit = Optional.ofNullable(init.getPubRecommendInit()).orElseGet(() -> initOld.getPubRecommendInit());
      Integer fundInit =
          Optional.ofNullable(init.getFundRecommendInit()).orElseGet(() -> initOld.getFundRecommendInit());
      hql =
          "update RecommendInit set pubRecommendInit=:pubRecommendInit,fundRecommendInit=:fundRecommendInit where psnId=:psnId";
      super.createQuery(hql).setParameter("pubRecommendInit", pubInit).setParameter("fundRecommendInit", fundInit)
          .setParameter("psnId", init.getPsnId()).executeUpdate();
    } else {
      super.save(init);
    }
  }
}
