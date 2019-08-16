package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PsnPmConference;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 用户确认成果会议记录.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmConferenceDao extends RolHibernateDao<PsnPmConference, Long> {

  /**
   * 获取确认过的成果会议记录.
   * 
   * @param nameHash
   * @param psnId
   * @return
   */
  public PsnPmConference getPsnPmConference(Integer namehash, Long psnId) {
    String hql = "from PsnPmConference where nameHash =:namehash and psnId =:psnId ";
    List<PsnPmConference> list =
        super.createQuery(hql).setParameter("namehash", namehash).setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
