package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.quartz.AwardStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 赞操作统计模块
 * 
 * @author Scy
 * 
 */
@Repository(value = "awardStatisticsDao")
public class AwardStatisticsDao extends SnsHibernateDao<AwardStatistics, Long> {

  /**
   * 获取赞数据 zk add.
   */
  public List findAwardPsn(Integer size) throws DaoException {
    // String hql =
    // "select count(p.id) as count,p.awardPsnId as psnId from AwardStatistics p where
    // p.createDate>=trunc(last_day(add_months(sysdate,-2))+1)and
    // p.createDate<trunc(last_day(add_months(sysdate,-1))+1) and action=1 group by p.awardPsnId having
    // count(p.awardPsnId)>=1";
    String hql =
        "select  count(p.id) as count,p.awardPsnId  as psnId from AwardStatistics p where trunc(p.createDate)>=trunc(sysdate-7)  and action=1  group by p.awardPsnId having count(p.awardPsnId)>=1";
    return super.createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(100)
        .setFirstResult(size * 100).list();
  }

  public Long countAward(Long psnId, Integer actionType) {
    return super.findUnique(
        "select count(t.id) from AwardStatistics t where t.awardPsnId = ? and t.action = 1 and t.actionType=?", psnId,
        actionType);
  }

  /**
   * 获取某人成果被赞的总数
   */
  public Long getAwardtotalPsn(Long psnId) {
    return super.queryForLong("select count(id) from award_statistics t where t.award_psn_id = ? and t.action = 1",
        new Object[] {psnId});
  }
}
