package com.smate.center.task.dao.rol.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.rol.quartz.PubRolPsnStat;
import com.smate.center.task.model.rol.quartz.PubRolSubmissionStatKey;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubRolPsnStatDao extends RolHibernateDao<PubRolPsnStat, PubRolSubmissionStatKey> {

  /**
   * 按单位刷新个人待认领成果数据.
   * 
   * @param insId
   * @throws DaoException
   */
  public void refreshPsnStatByIns(Long insId) throws DaoException {
    String hql = "call pkg_pub_assign_person.refresh_pub_psn_stat_byIns(?)";
    super.getSession().createSQLQuery(hql).setParameter(0, insId).executeUpdate();
  }

  public void refreshPsnStatByPsn(Long insId, Long psnId) {
    PubRolPsnStat stat = this.getPubRolPsnStat(psnId, insId);
    if (stat == null) {
      stat = new PubRolPsnStat(psnId, insId);
      super.save(stat);
    }
    super.save(stat);

  }

  private PubRolPsnStat getPubRolPsnStat(Long psnId, Long insId) {
    return (PubRolPsnStat) super.createQuery("from PubRolPsnStat where key.psnId =:psnId and key.insId =:insId ")
        .setParameter("psnId", psnId).setParameter("insId", insId).uniqueResult();
  }

}
