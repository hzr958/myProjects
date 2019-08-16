package com.smate.web.psn.dao.statistics;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.statistics.AwardStatistics;

/**
 * 赞统计DAO
 *
 * @author wsn
 * @createTime 2017年6月21日 下午4:52:46
 *
 */
@Repository
public class AwardStatisticsDao extends SnsHibernateDao<AwardStatistics, Long> {

  /**
   * 统计某一种类资源赞的数量
   * 
   * @param psnId
   * @param actionType
   * @return
   */
  public Long countAward(Long psnId, Integer actionType) {
    return super.findUnique(
        "select count(t.id) from AwardStatistics t where t.awardPsnId = ? and t.action = 1 and t.actionType=?", psnId,
        actionType);
  }

  /**
   * 统计人员被赞数----由于成果改造，成果赞记录放到v_pub_like表了
   * 
   * @param awardPsnId 要统计的人员的ID
   * @return 人员被赞总数
   */
  public Long countPsnAwardSum(Long awardPsnId) {
    String hql =
        "select count(1) from AwardStatistics t where t.action = 1 and t.awardPsnId = :awardPsnId and t.actionType <> 1 and t.actionType <> 2";
    return (Long) super.createQuery(hql).setParameter("awardPsnId", awardPsnId).uniqueResult();
  }
}
