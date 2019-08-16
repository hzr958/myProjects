package com.smate.core.base.utils.dao.security;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.model.cas.security.SysRolUser;

/**
 * 登录用户表DAO.
 * 
 * @author cwli
 * 
 */
@Repository
public class SysRolUserDao extends HibernateDao<SysRolUser, Long> {


  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.CAS;
  }


  public SysRolUser getSysRolUserByGuid(String guid) {
    return super.findUniqueBy("guid", guid);
  }

  public Long getRolUserByGuid(SysRolUser rolUser) {
    String hql = "select psnId from SysRolUser where guid=? and insId=?";
    return super.findUnique(hql, rolUser.getGuid(), rolUser.getInsId());
  }

  @SuppressWarnings("unchecked")
  public List<Long> queryConnectedPsnByGuid(String guid) {
    return super.createQuery("select t.psnId from SysRolUser t where t.guid=?", guid).list();
  }

  public SysRolUser querySysRolUser(String guid, Long psnId) {

    return (SysRolUser) super.createQuery("from SysRolUser t where t.psnId=? and t.guid=?", psnId, guid)
        .setMaxResults(1).uniqueResult();
  }

  public SysRolUser findSysRolUser(String guid, Long insId) {

    return super.findUnique("from SysRolUser t where t.guid=? and t.insId=?", guid, insId);
  }

  @SuppressWarnings("unchecked")
  public List<SysRolUser> getSysRolUserListByInsId(Long insId) {
    String hql = "from SysRolUser t where t.insId =:insId";
    return super.createQuery(hql).setParameter("insId", insId).list();
  }

}
