package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.ShareStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 共享统计模块Dao
 * 
 * @author zk
 * 
 */
@Repository(value = "shareStatisticsDao")
public class ShareStatisticsDao extends SnsHibernateDao<ShareStatistics, Long> {


  /**
   * 获取分享数据 zk add.
   */
  public List findSharePsn(Integer size) {
    // String hql =
    // "select count(p.id) as count,p.awardPsnId as psnId from AwardStatistics p where
    // p.createDate>=trunc(last_day(add_months(sysdate,-2))+1)and
    // p.createDate<trunc(last_day(add_months(sysdate,-1))+1) and action=1 group by p.awardPsnId having
    // count(p.awardPsnId)>=1";
    String hql =
        "select  count(p.id) as count,p.sharePsnId  as psnId from ShareStatistics p where trunc(p.createDate)>=trunc(sysdate-7) group by p.sharePsnId having count(p.sharePsnId)>=1";
    return super.createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(100)
        .setFirstResult(size * 100).list();
  }

  /**
   * 获取psnId的分享总数
   * 
   * @param psnId
   * @param type
   * @return
   */
  public Long getSharetotalPsn(Long psnId) {
    // return super.queryForLong("select count(id) from ShareStatistics t where t.sharePsnId = ? and
    // actionType = ?",new Object[] { psnId, type });
    String hql = "select count(id) from ShareStatistics t where t.sharePsnId = :spi";
    return (Long) super.createQuery(hql).setParameter("spi", psnId).uniqueResult();
  }

}
