package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignPubMedConference;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * pubMed成果会议论文DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignPubMedConferenceDao extends RolHibernateDao<PubAssignPubMedConference, Long> {

  /**
   * 获取成果会议论文查重.
   * 
   * @param pubId
   * @return
   */
  public PubAssignPubMedConference getPubAssignPubMedConferenceByPubId(Long pubId) {
    String hql = "from PubAssignPubMedConference where pubId = ? ";
    List<PubAssignPubMedConference> list = super.find(hql, pubId);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }


}
