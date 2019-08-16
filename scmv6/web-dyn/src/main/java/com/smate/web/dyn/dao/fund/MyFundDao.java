package com.smate.web.dyn.dao.fund;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.fund.MyFund;

/**
 * 我的（收藏的）基金DAO
 * 
 * @author WSN
 *
 *         2017年8月18日 上午10:56:56
 *
 */
@Repository
public class MyFundDao extends SnsHibernateDao<MyFund, Long> {

  /**
   * 查找是否收藏了某个基金
   * 
   * @param psnId
   * @param fundId
   * @return
   */
  public MyFund findMyFund(Long psnId, Long fundId) {
    String hql = " from MyFund t where t.fundId = :fundId and t.psnId = :psnId";
    return (MyFund) super.createQuery(hql).setParameter("fundId", fundId).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 是否已收藏过基金
   * 
   * @param psnId
   * @param fundId
   * @return
   */
  public boolean hasCollectedFund(Long psnId, Long fundId) {
    String hql = "select count(1) from MyFund t where t.fundId = :fundId and t.psnId = :psnId";
    Long count =
        (Long) super.createQuery(hql).setParameter("fundId", fundId).setParameter("psnId", psnId).uniqueResult();
    if (count > 0) {
      return true;
    }
    return false;
  }

}
