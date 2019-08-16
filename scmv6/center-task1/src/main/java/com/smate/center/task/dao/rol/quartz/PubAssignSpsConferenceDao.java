package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignSpsConference;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * scopus成果会议论文DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignSpsConferenceDao extends RolHibernateDao<PubAssignSpsConference, Long> {
  /**
   * 获取成果会议论文查重.
   * 
   * @param pubId
   * @return
   */
  public PubAssignSpsConference getPubAssignConferenceByPubId(Long pubId) {
    String hql = "from PubAssignSpsConference where pubId = ? ";
    List<PubAssignSpsConference> list = super.find(hql, pubId);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
