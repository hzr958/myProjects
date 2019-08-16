package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PsnPmJournal;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PsnPmJournalDao extends RolHibernateDao<PsnPmJournal, Long> {

  public PsnPmJournal getPsnPmJournal(Long jid, Long psnId) {
    String hql = "from PsnPmJournal where jid=:jid and psnId =:psnId ";
    List<PsnPmJournal> list = super.createQuery(hql).setParameter("jid", jid).setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
