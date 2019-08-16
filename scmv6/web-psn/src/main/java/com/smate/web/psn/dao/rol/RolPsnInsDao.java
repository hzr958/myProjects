package com.smate.web.psn.dao.rol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.web.psn.model.rol.RolPsnIns;
import com.smate.web.psn.model.rol.RolPsnInsPk;

/**
 * Rol 人员与单位关系表DAO.
 * 
 * @author lichangwen
 */
@Repository
public class RolPsnInsDao extends RolHibernateDao<RolPsnIns, RolPsnInsPk> {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 根据人员ID获取单位关系列表(所有状态).
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RolPsnIns> findAllStatusRolPsnInsList(Long psnId) {

    return super.createQuery("from RolPsnIns  t where t.pk.psnId = ?", psnId).list();
  }
}
