package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignPubMedAuthor;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果作者名称DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignPubMedAuthorDao extends RolHibernateDao<PubAssignPubMedAuthor, Long> {

  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignPubMedAuthor> getPubAuthor(Long pubId) {

    String hql = "from PubAssignPubMedAuthor where pubId = ?  ";
    return super.find(hql, pubId);
  }
}
