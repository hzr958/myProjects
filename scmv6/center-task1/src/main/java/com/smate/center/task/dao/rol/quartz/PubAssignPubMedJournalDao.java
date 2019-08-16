package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignPubMedJournal;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果期刊DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignPubMedJournalDao extends RolHibernateDao<PubAssignPubMedJournal, Long> {
  /**
   * 获取成果期刊数据.
   * 
   * @param pubId
   * @return
   */
  public PubAssignPubMedJournal getPubAssignPubMedJournalByPubId(Long pubId) {
    String hql = "from PubAssignPubMedJournal where pubId =:pubId ";
    List<PubAssignPubMedJournal> list = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
