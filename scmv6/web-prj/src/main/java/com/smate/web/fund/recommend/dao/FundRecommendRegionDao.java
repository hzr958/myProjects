package com.smate.web.fund.recommend.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.recommend.model.FundRecommendRegion;

@Repository
public class FundRecommendRegionDao extends SnsHibernateDao<FundRecommendRegion, Long> {

  /**
   * 查询个人地区设置按首字母排序
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<FundRecommendRegion> getFundRegionListByPsnId(Long psnId) {
    String hql = "from FundRecommendRegion where psnId=:psnId order by nlssort(zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
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
