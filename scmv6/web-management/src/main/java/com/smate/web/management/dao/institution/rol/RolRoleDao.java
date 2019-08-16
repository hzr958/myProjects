package com.smate.web.management.dao.institution.rol;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.security.InsRole;

/**
 * 人员角色表DAO ,支持SaaS.
 * 
 * @author zb
 * 
 */
@Repository
public class RolRoleDao extends RolHibernateDao<InsRole, Serializable> {

  public List<InsRole> getInsRoleByInsIdAndRoleId(final Long insId, final Long roleId) {
    String hql = "select t from InsRole t where id.insId =:insId  and id.roleId=:roleId";
    return super.createQuery(hql).setParameter("insId", insId).setParameter("roleId", roleId).list();
  }

}
