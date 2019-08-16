package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignCnkiPatAuthor;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * CnkiPat成果作者名称DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignCnkiPatAuthorDao extends RolHibernateDao<PubAssignCnkiPatAuthor, Long> {
  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignCnkiPatAuthor> getPubAuthor(Long pubId) {
    String hql = "from PubAssignCnkiPatAuthor where pubId = ?  ";
    return super.find(hql, pubId);
  }

}
