package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignCnkiJournal;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * CNKI成果期刊DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignCnkiJournalDao extends RolHibernateDao<PubAssignCnkiJournal, Long> {
  /**
   * 获取成果期刊数据.
   * 
   * @param pubId
   * @return
   */
  public PubAssignCnkiJournal getPubAssignJournalByPubId(Long pubId) {
    String hql = "from PubAssignCnkiJournal where pubId =:pubId ";
    List<PubAssignCnkiJournal> list = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
