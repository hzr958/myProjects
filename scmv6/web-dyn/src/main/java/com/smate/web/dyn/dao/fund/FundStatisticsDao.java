package com.smate.web.dyn.dao.fund;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.fund.FundStatistics;

/**
 * 基金操作统计DAO
 * 
 * @author WSN
 *
 *         2017年8月18日 下午4:54:57
 *
 */
@Repository
public class FundStatisticsDao extends SnsHibernateDao<FundStatistics, Long> {

  /**
   * 查找基金的操作统计信息
   * 
   * @param fundId
   * @return
   */
  public FundStatistics findFundStatisticsById(Long fundId) {
    String hql = " from FundStatistics t where t.fundId = :fundId";
    return (FundStatistics) super.createQuery(hql).setParameter("fundId", fundId).uniqueResult();

  }
}
