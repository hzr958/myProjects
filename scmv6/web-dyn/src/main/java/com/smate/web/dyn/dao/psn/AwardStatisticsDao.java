package com.smate.web.dyn.dao.psn;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.psn.AwardStatistics;

/**
 * 赞操作统计模块
 * 
 * @author zk
 * 
 */
@Repository(value = "awardStatisticsDao")
public class AwardStatisticsDao extends SnsHibernateDao<AwardStatistics, Long> {

  /**
   * 查找AwardStatistics
   * 
   * @param psnId
   * @param awardPsnId
   * @param actionKey
   * @param actionType
   * @param tempAction
   * @return
   */
  @SuppressWarnings("unchecked")
  public AwardStatistics findAwardStatistics(Long psnId, Long awardPsnId, Long actionKey, Integer actionType) {
    String hql = "select t from AwardStatistics t where t.psnId=:psnId and t.awardPsnId=:awardPsnId "
        + "and t.actionKey=:actionKey and t.actionType=:actionType ";
    List<AwardStatistics> list =
        super.createQuery(hql).setParameter("psnId", psnId).setParameter("awardPsnId", awardPsnId)
            .setParameter("actionKey", actionKey).setParameter("actionType", actionType).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public Long countAward(Long psnId, Integer actionType) {
    return super.findUnique(
        "select count(t.id) from AwardStatistics t where t.awardPsnId = ? and t.action = 1 and t.actionType=?", psnId,
        actionType);
  }

  /**
   * 判断该资源 是否被点赞
   * 
   * @param psnId
   * @param actionKey
   * @param actionType
   * @return
   */
  public boolean hasAward(Long psnId, Long actionKey, Integer actionType) {
    String hql = "select t.action from AwardStatistics t where t.psnId=:psnId  "
        + "and t.actionKey=:actionKey and t.actionType=:actionType and t.createDate="
        + "(select max(g.createDate) from AwardStatistics g where g.psnId=t.psnId "
        + "and g.actionKey=t.actionKey and g.actionType=t.actionType )";
    LikeStatusEnum action = (LikeStatusEnum) this.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("actionKey", actionKey).setParameter("actionType", actionType).uniqueResult();
    if (action != null) {
      return action == LikeStatusEnum.LIKE ? true : false;
    }
    return false;

  }

}
