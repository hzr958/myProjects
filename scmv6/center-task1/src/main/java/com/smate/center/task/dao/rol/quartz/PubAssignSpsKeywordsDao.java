package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignSpsKeywords;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * scopus成果关键词DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignSpsKeywordsDao extends RolHibernateDao<PubAssignSpsKeywords, Long> {

  /**
   * 获取成果关键词.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignSpsKeywords> getKwByPubId(Long pubId) {
    String hql = "from PubAssignSpsKeywords where pubId = ? ";
    return super.find(hql, pubId);
  }

}
