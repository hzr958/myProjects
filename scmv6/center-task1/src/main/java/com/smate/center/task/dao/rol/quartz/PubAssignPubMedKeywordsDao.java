package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignPubMedKeywords;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果关键词DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignPubMedKeywordsDao extends RolHibernateDao<PubAssignPubMedKeywords, Long> {
  /**
   * 获取成果关键词.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignPubMedKeywords> getKwByPubId(Long pubId) {
    String hql = "from PubAssignPubMedKeywords where pubId = ? ";
    return super.find(hql, pubId);
  }
}
