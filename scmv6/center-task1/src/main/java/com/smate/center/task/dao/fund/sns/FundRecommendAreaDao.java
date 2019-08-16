package com.smate.center.task.dao.fund.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.sns.FundRecommendArea;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class FundRecommendAreaDao extends SnsHibernateDao<FundRecommendArea, Long> {
  /**
   * 获取个人的科技领域设置
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<FundRecommendArea> getFundRecommendAreaListByPsnId(Long psnId) {
    String hql = "from FundRecommendArea where psnId=:psnId order by updateDate desc,scienceAreaId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 获取个人科技领域设置的数量
   * 
   * @param psnId
   * @return
   */
  public Long getPsnFundRecommendAreaSize(Long psnId) {
    String hql = "select count(1) from FundRecommendArea where psnId=:psnId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 根据psnId和科技领域id获取一条记录
   * 
   * @param psnId
   * @param scienceAreaId
   * @return
   */
  public FundRecommendArea getFundRecommendArea(Long psnId, Long scienceAreaId) {
    String hql = "from FundRecommendArea where psnId=:psnId and scienceAreaId=:scienceAreaId";
    return (FundRecommendArea) super.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("scienceAreaId", scienceAreaId).uniqueResult();
  }

  /**
   * 根据psnId和科技领域id查看是否有该条记录
   * 
   * @param psnId
   * @param scienceAreaId
   * @return
   */
  public boolean haveFundRecommendArea(Long psnId, Long scienceAreaId) {
    String hql = "select count(1) from FundRecommendArea where psnId=:psnId and scienceAreaId=:scienceAreaId";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("scienceAreaId", scienceAreaId)
        .uniqueResult();
    if (count > 0) {
      return true;
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnSetAreaId(Long psnId) {
    String hql = "select scienceAreaId from FundRecommendArea where psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
