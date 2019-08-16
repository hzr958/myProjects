package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubStatisticsPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人库成果操作统计Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PubStatisticsDAO extends SnsHibernateDao<PubStatisticsPO, Long> {
  /**
   * 获取成果的阅读总数
   * 
   * @param pubIds
   * @return
   */
  public Long findPubReadCounts(List<Long> pubIds) {
    String hql = "select sum(t.readCount) from PubStatisticsPO t where t.pubId in (:pubIds)";
    return (Long) super.createQuery(hql).setParameterList("pubIds", pubIds).uniqueResult();
  }

  /**
   * 根据pubId的list集合获取PubStatisticsPO的list集合
   * 
   */
  @SuppressWarnings("unchecked")
  public List<PubStatisticsPO> getPubStatisticsPOList(List<Long> pubIds) {
    String hql = "select t from PubStatisticsPO t where t.pubId in (:pubIds)";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

}
