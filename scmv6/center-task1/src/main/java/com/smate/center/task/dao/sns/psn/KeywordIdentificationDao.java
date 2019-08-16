package com.smate.center.task.dao.sns.psn;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.profile.KeywordIdentification;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author zyx
 * 
 */
@Repository
public class KeywordIdentificationDao extends SnsHibernateDao<KeywordIdentification, Long> implements Serializable {

  @SuppressWarnings("unchecked")
  public List<Long> getEndorsedPsnId(Integer startSize, Date beforeDay) {
    String hql = "select distinct psnId from KeywordIdentification where opDate >=:beforeDay";
    return (List<Long>) super.createQuery(hql).setParameter("beforeDay", beforeDay).setMaxResults(100)
        .setFirstResult(100 * startSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getFriendId(Long psnId, Date beforeDay, int maxSize) {
    String hql = "select distinct friendId from KeywordIdentification where  opDate >=:beforeDay  and psnId =:psnId ";
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
    String hql = "select count(friendId) from KeywordIdentification where  opDate >=:beforeDay  and psnId =:psnId ";
    return (Long) super.createQuery(hql).setParameter("beforeDay", beforeDay).setParameter("psnId", psnId)
        .uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<KeywordIdentification> getCurrentEndorsedInfo(Integer startSize, Date beforeDay) {
    String hql = " from KeywordIdentification where opDate >=:beforeDay";
    return super.createQuery(hql).setParameter("beforeDay", beforeDay).setMaxResults(100)
        .setFirstResult(100 * startSize).list();
  }

}
