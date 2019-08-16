package com.smate.center.task.dao.rol.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubRolSubmissionStat;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubRolSubmissionStatDao extends RolHibernateDao<PubRolSubmissionStat, Long> {
  /**
   * 更新成果提交数.
   * 
   * @param insId
   */
  public void refreshPubSubmission(Long insId) {
    String hql = "call pkg_pub_submission_person.refresh_pub_submission_byIns(?)";
    super.getSession().createSQLQuery(hql).setParameter(0, insId).executeUpdate();
  }

  public void refreshPubSubmission(Long insId, Long psnId) {
    String hql = "call pkg_pub_submission_person.refresh_pub_submission_byPsn(?,?)";
    super.getSession().createSQLQuery(hql).setParameter(0, insId).setParameter(1, psnId).executeUpdate();
  }

}
