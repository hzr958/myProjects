package com.smate.center.task.dao.sns.quartz;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.ScienceAreaAgreeStatus;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class ScienceAreaAgreeStatusDao extends SnsHibernateDao<ScienceAreaAgreeStatus, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getEndorsedPsnId(Integer startSize, Date beforeDay) {
    String hql = "select distinct psnId from ScienceAreaAgreeStatus where operateDate >=:beforeDay and status =0";
    return (List<Long>) super.createQuery(hql).setParameter("beforeDay", beforeDay).setMaxResults(100)
        .setFirstResult(100 * startSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Integer> getPsnScienceArea(Long psnId, Long operatePsnId, Date beforeDay) {
    String hql =
        "select t.scienceAreaId  from ScienceAreaAgreeStatus t where t.psnId=:psnId and t.operatePsnId=:operatePsnId and t.operateDate >=:beforeDay and t.status =0";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("operatePsnId", operatePsnId)
        .setParameter("beforeDay", beforeDay).list();
  }



  @SuppressWarnings("unchecked")
  public List<Long> getFriendId(Long psnId, Date beforeDay, int maxSize) {
    String hql =
        "select distinct operatePsnId from ScienceAreaAgreeStatus where  operateDate >=:beforeDay  and psnId =:psnId and status=0 ";
    return (List<Long>) super.createQuery(hql).setParameter("beforeDay", beforeDay).setParameter("psnId", psnId)
        .setMaxResults(maxSize).list();
  }

  public void updateEmailBuildStatus(Long psnId, int status) {
    String sql = "update SCIENCEAREA_AGREE_STATUS  set status=? where PSN_ID=? and status =0";
    super.update(sql, new Object[] {status, psnId});

  }

}
