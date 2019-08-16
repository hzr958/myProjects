package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignConference;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果会议论文DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignConferenceDao extends RolHibernateDao<PubAssignConference, Long> {
  /**
   * 获取成果会议论文查重.
   * 
   * @param pubId
   * @return
   */
  public PubAssignConference getPubAssignConferenceByPubId(Long pubId) {
    String hql = "from PubAssignConference where pubId = ? ";
    List<PubAssignConference> list = super.find(hql, pubId);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
