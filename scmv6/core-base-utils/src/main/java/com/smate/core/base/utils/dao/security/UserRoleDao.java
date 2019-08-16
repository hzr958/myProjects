package com.smate.core.base.utils.dao.security;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;



/**
 * 注册人員登陸信息DAO.
 * 
 * 
 * 
 */
@Repository
public class UserRoleDao extends SnsHibernateDao<UserRole, Long> {

  @SuppressWarnings("unchecked")
  public List<UserRole> getSysAdministrator() {

    String ql = "from UserRole where id.rolId = 4";
    List<UserRole> users = super.createQuery(ql).list();
    return users;

  }

  @SuppressWarnings("unchecked")
  public List<UserRole> getUserRole(Long  psnId) {

    String ql = "from UserRole where id.userId =:psnId";
    List<UserRole> users = super.createQuery(ql).setParameter("psnId",psnId).list();
    return users;

  }
}
