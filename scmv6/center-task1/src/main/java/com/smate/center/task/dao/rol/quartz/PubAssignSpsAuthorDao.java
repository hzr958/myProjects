package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignSpsAuthor;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * scopus成果作者名称DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignSpsAuthorDao extends RolHibernateDao<PubAssignSpsAuthor, Long> {

  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignSpsAuthor> getPubAuthor(Long pubId) {

    String hql = "from PubAssignSpsAuthor where pubId = ?  ";
    return super.find(hql, pubId);
  }

}
