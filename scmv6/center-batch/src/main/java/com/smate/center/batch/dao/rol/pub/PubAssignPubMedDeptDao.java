package com.smate.center.batch.dao.rol.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubAssignPubMedDept;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果部门DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignPubMedDeptDao extends RolHibernateDao<PubAssignPubMedDept, Long> {

  /**
   * 查询部门是否已经存在.
   * 
   * @param pubId
   * @param dept
   * @return
   */
  public boolean isDeptExists(Long pubId, String dept) {
    String hql = "select count(id) from  PubAssignPubMedDept where pubId = ? and deptName = ? ";
    Long count = super.findUnique(hql, pubId, dept);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
