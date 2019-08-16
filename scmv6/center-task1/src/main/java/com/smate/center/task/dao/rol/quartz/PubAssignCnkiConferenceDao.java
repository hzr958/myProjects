package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignCnkiConference;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * CNKI成果会议论文DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignCnkiConferenceDao extends RolHibernateDao<PubAssignCnkiConference, Long> {
  /**
   * 获取成果会议论文查重.
   * 
   * @param pubId
   * @return
   */
  public PubAssignCnkiConference getPubAssignConferenceByPubId(Long pubId) {
    String hql = "from PubAssignCnkiConference where pubId =:pubId ";
    List<PubAssignCnkiConference> list = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
