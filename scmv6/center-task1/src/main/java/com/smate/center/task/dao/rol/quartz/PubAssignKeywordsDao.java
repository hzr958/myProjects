package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignKeywords;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubAssignKeywordsDao extends RolHibernateDao<PubAssignKeywords, Long> {
  /**
   * 获取成果关键词.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignKeywords> getKwByPubId(Long pubId) {
    String hql = "from PubAssignKeywords where pubId = ? ";
    return super.find(hql, pubId);
  }
}
