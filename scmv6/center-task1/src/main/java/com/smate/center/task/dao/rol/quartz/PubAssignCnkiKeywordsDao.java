package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignCnkiKeywords;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubAssignCnkiKeywordsDao extends RolHibernateDao<PubAssignCnkiKeywords, Long> {
  /**
   * 获取成果关键词.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignCnkiKeywords> getKwByPubId(Long pubId) {
    String hql = "from PubAssignCnkiKeywords where pubId =:pubId ";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
