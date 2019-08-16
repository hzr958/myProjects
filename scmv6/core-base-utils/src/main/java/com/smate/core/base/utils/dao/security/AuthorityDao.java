package com.smate.core.base.utils.dao.security;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.menu.Authority;


/**
 * 权限表DAO.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Repository
public class AuthorityDao extends SnsHibernateDao<Authority, Long> {


  public List<Authority> getUserAuthority(Long userId) {
    List<Authority> authoritys = super.find(
        " select r.authorities from  Role r where r.id in (select  id.rolId from UserRole  where   id.userId = ? ) ",
        userId);
    return authoritys;
  }

  public List<Authority> getUserRoleAuthority(Long userId, Integer roleId) {
    List<Authority> authoritys = super.find(
        " select r.authorities from  Role r where r.id  = ? and r.id in (select  id.rolId from UserRole  where   id.userId = ?) ",
        roleId.longValue(), userId);
    return authoritys;
  }
}
