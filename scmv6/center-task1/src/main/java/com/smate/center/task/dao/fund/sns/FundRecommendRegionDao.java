package com.smate.center.task.dao.fund.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.sns.FundRecommendRegion;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class FundRecommendRegionDao extends SnsHibernateDao<FundRecommendRegion, Long> {

  @SuppressWarnings("unchecked")
  public List<FundRecommendRegion> getFundRegionListByPsnId(Long psnId) {
    String hql = "from FundRecommendRegion where psnId=:psnId order by updateDate desc, regionId desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public FundRecommendRegion getFundRegion(Long psnId, Long regionId) {
    String hql = "from FundRecommendRegion where psnId=:psnId and regionId=:regionId";
    return (FundRecommendRegion) super.createQuery(hql).setParameter("psnId", psnId).setParameter("regionId", regionId)
        .uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnSetRegionId(Long psnId) {
    String hql = "select regionId from FundRecommendRegion where psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
