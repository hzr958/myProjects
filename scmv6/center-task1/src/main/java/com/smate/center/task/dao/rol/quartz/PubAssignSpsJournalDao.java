package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignSpsJournal;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubAssignSpsJournalDao extends RolHibernateDao<PubAssignSpsJournal, Long> {
  /**
   * 获取成果期刊数据.
   * 
   * @param pubId
   * @return
   */
  public PubAssignSpsJournal getPubAssignJournalByPubId(Long pubId) {
    String hql = "from PubAssignSpsJournal where pubId = ? ";
    List<PubAssignSpsJournal> list = super.find(hql, pubId);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
