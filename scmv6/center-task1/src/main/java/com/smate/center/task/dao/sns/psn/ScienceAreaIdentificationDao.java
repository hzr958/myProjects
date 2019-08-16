package com.smate.center.task.dao.sns.psn;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.psn.model.ScienceAreaIdentification;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author zjh 人员研究领域记录
 *
 */
@Repository
public class ScienceAreaIdentificationDao extends SnsHibernateDao<ScienceAreaIdentification, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getEndorsedPsnId(Integer startSize, Date beforeDay) {
    String hql = "select distinct psnId from ScienceAreaIdentification where operateDate >=:beforeDay";
    return (List<Long>) super.createQuery(hql).setParameter("beforeDay", beforeDay).setMaxResults(100)
        .setFirstResult(100 * startSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getFriendId(Long psnId, Date beforeDay, int maxSize) {
    String hql =
        "select distinct operatePsnId from ScienceAreaIdentification where  operateDate >=:beforeDay  and psnId =:psnId ";
    return (List<Long>) super.createQuery(hql).setParameter("beforeDay", beforeDay).setParameter("psnId", psnId)
        .setMaxResults(maxSize).list();
  }

  /**
   * 获取在beforeDay前的研究领域赞
   * 
   * @param psnId
   * @param beforeDay
   * @return
   */
  @SuppressWarnings("unchecked")
  public Long getEndorsedCount(Long psnId, Date beforeDay) {
    String hql =
        "select count(operatePsnId) from ScienceAreaIdentification where  operateDate >=:beforeDay  and psnId =:psnId ";
    return (Long) super.createQuery(hql).setParameter("beforeDay", beforeDay).setParameter("psnId", psnId)
        .uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<ScienceAreaIdentification> getCurrentEndorsedInfo(Integer startSize, Date beforeDay) {
    String hql = " from ScienceAreaIdentification where operateDate >=:beforeDay";
    return super.createQuery(hql).setParameter("beforeDay", beforeDay).setMaxResults(100)
        .setFirstResult(100 * startSize).list();
  }


}
