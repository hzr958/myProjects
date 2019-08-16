package com.smate.center.batch.dao.rol;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.InsConfirm;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 单位成果确认
 * 
 * @author Scy
 * 
 */
@Repository
public class InsConfirmDao extends RolHibernateDao<InsConfirm, Long> {

  /**
   * 获取成果确认状态
   * 
   * @param insId
   * @return
   */
  public Integer findInsConfirmStatus(Long insId) {
    String hql = "select t.status from InsConfirm t where t.insId = ?";
    Integer status = (Integer) this.createQuery(hql, insId).uniqueResult();
    if (status == null) {
      return 0;
    }
    return status;
  }
}
