package com.smate.web.fund.recommend.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.recommend.model.FundAward;

/**
 * 赞操作DAO
 * 
 * @author WSN
 *
 *         2017年8月18日 下午6:09:34
 *
 */
@Repository
public class FundAwardDao extends SnsHibernateDao<FundAward, Long> {

  /**
   * 查找人员对某个基金的赞操作记录
   * 
   * @param psnId
   * @param fundId
   * @return
   */
  public FundAward findFundAwardRecord(Long psnId, Long fundId) {
    String hql = " from FundAward t where t.awardPsnId = :psnId and t.fundId = :fundId";
    return (FundAward) super.createQuery(hql).setParameter("psnId", psnId).setParameter("fundId", fundId)
        .uniqueResult();
  }

  /**
   * 查找对某个基金的赞状态
   * 
   * @param psnId
   * @param fundId
   * @return
   */
  public FundAward awardOperationStatus(Long psnId, Long fundId) {
    String hql =
        "select new FundAward(t.fundId, t.awardPsnId, t.status) from FundAward t where t.awardPsnId = :psnId and t.fundId = :fundId";
    return (FundAward) super.createQuery(hql).setParameter("psnId", psnId).setParameter("fundId", fundId)
        .uniqueResult();
  }

  /**
   * 批量查询基金赞状态
   * 
   * @param psnId
   * @param fundIds
   * @return
   */
  public List<FundAward> findAwardStatusList(Long psnId, List<Long> fundIds) {
    String hql =
        "select new FundAward(t.fundId, t.awardPsnId, t.status) from FundAward t where t.awardPsnId = :psnId and t.fundId in (:fundIds)";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("fundIds", fundIds).list();
  }

}
