package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignCniprAuthor;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * CNIPR成果作者名称DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignCniprAuthorDao extends RolHibernateDao<PubAssignCniprAuthor, Long> {
  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignCniprAuthor> getPubAuthor(Long pubId) {
    String hql = "from PubAssignCniprAuthor where pubId = ?  ";
    return super.find(hql, pubId);
  }

}
