package com.smate.core.base.utils.dao.security.gxrol;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.ScmRolRoleConstants;
import com.smate.core.base.utils.data.GxRolHibernateDao;
import com.smate.core.base.utils.model.security.InsRole;
import com.smate.core.base.utils.model.security.InsRoleId;


/**
 * 人员角色表DAO ,支持SaaS.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Repository
public class GxRolInsRoleDao extends GxRolHibernateDao<InsRole, InsRoleId> {



  @SuppressWarnings("unchecked")
  public List<InsRole> getInsRoleWithUserId(final Long userId, final Long insId) {

    String hql = "from InsRole   where  id.userId = ? and id.insId = ? ";
    List<InsRole> items = null;

    long insid = (insId == null) ? 0L : insId;
    Query query = createQuery(hql, userId, insid);
    query.setCacheable(true);
    items = query.list();

    return items;
  }

  public InsRole getUserRole(Long userId, Long insId, Long rolId) {
    String hql = "from InsRole   where  id.userId = ? and id.insId = ? and id.roleId = ? ";
    return super.findUnique(hql, userId, insId, rolId);
  }

  @SuppressWarnings("unchecked")
  public List<InsRole> getInsRoleWithUserId(final Long userId, final Long insId, String excludeRoles) {

    String hql = "from InsRole   where  id.userId = ? and id.insId = ? ";
    List<InsRole> items = null;
    if (StringUtils.isNotBlank(excludeRoles)) {

      hql += " and id.roleId not in (" + excludeRoles + ")";
    }

    Query query = createQuery(hql, userId, insId);
    query.setCacheable(true);
    items = query.list();

    return items;
  }

  /**
   * 判断人员在单位是否有多角色.
   * 
   * @param insId
   * @param psnId
   * @return
   */
  public boolean hasMultiRole(Long insId, Long psnId) {

    String hql = "select count(id.userId) from InsRole where  id.userId = ? and id.insId = ? and id.roleId not in (1)";
    Long count = super.findUnique(hql, psnId, insId);
    return count > 1;
  }

  public InsRole getInsRoleByRolId(final Long userId, final Long insId, final Integer roleId) {
    String hql = "from InsRole where id.userId = ? and id.insId = ? and id.roleId= ?";
    return findUnique(hql, userId, insId, roleId);
  }

  public List<InsRole> getInsRoleByRoleIdAndUserId(final Long userId, final Long roleId) {
    String hql = "select t from InsRole t where id.userId = ? and id.roleId= ?";
    return super.createQuery(hql, userId, roleId).list();
  }

  public List<InsRole> getInsRoleByInsIdAndRoleId(final Long insId, final Long roleId) {
    String hql = "select t from InsRole t where id.insId = ? and id.roleId= ?";
    return super.createQuery(hql, insId, roleId).list();
  }

  /**
   * 清空单位科研人员角色.
   * 
   * @param insId
   * @param psnId
   */
  public void removeInsRole(Long insId, Long psnId) {

    String hql = "delete from InsRole where id.userId = ? and id.insId = ? ";
    super.createQuery(hql, psnId, insId).executeUpdate();
  }

  /**
   * 清空用户管理角色.
   * 
   * @param insId
   * @param psnId
   */
  public void removeMangeRole(Long insId, Long psnId) {

    String hql = "delete from InsRole where id.userId = :userId and id.insId = :insId and id.roleId not in(0,1,3)";
    super.createQuery(hql).setParameter("userId", psnId).setParameter("insId", insId).executeUpdate();
  }

  /**
   * 清空单位科研人员角色.
   * 
   * @param insId
   * @param psnId
   */
  public void removeInsRole(Long insId, Long psnId, Long roleId) {

    String hql = "delete from InsRole where id.userId = ? and id.insId = ? and id.roleId = ? ";
    super.createQuery(hql, psnId, insId, roleId).executeUpdate();
  }

  /**
   * 判断是否是单位管理员.
   * 
   * @param psnId
   * @param insId
   * @return
   */
  public boolean isInsRo(Long insId, Long psnId) {
    String hql = "select count(id.userId) from InsRole where id.userId = ? and id.insId = ? and id.roleId = 2";
    Long count = super.findUnique(hql, psnId, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 是否具有管理员角色.
   * 
   * @param insId
   * @param psnId
   * @return
   */
  public boolean ownManageRole(Long insId, Long psnId) {
    String hql = "select count(id.insId) from InsRole where id.userId = ? and id.insId = ? and id.rolId not in (0,1,3)";
    Long count = super.findUnique(hql, psnId, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 有管理员角色的用户.
   * 
   * @param insId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> ownManageRole(Long insId, List<Long> psnIds) {
    String hql =
        "select id.userId from InsRole where id.userId in(:psnIds) and id.insId = :insId and id.roleId not in (0,1,3)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).setParameter("insId", insId).list();
  }

  @SuppressWarnings("unchecked")
  public Long findNsfcRolInsId(Integer roleId, Long userId) {
    String hql = "select id.insId from NsfcInsUserRole where id.userId=? and id.roleId=?";
    Query query = createQuery(hql, userId, roleId);
    List<Long> items = query.list();
    if (items != null && items.size() > 0) {
      return items.get(0);
    } else {
      return null;
    }
  }

  /**
   * 查询用户拥有的管理角色以及角色名称.
   * 
   * @param insId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsRole> getUserManagerRole(Long insId, List<Long> psnIds) {
    String hql =
        "select new InsRole(t.id,t1.name) from InsRole t,Role t1 where t.id.roleId = t1.id and t.id.userId in(:psnIds) and t.id.insId = :insId and t.id.roleId not in (0,1,3)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).setParameter("insId", insId).list();
  }

  /**
   * 查询用户拥有的管理角色以及角色名称.
   * 
   * @param insId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsRole> getInsRORole(Long insId, List<Long> psnIds) {

    String hql =
        "select new InsRole(t.id,t1.name) from InsRole t,Role t1 where t.id.roleId = t1.id and t.id.userId in(:psnIds) and t.id.insId = :insId and t.id.roleId = :roleId ";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).setParameter("insId", insId)
        .setParameter("roleId", Long.valueOf(ScmRolRoleConstants.INS_RO)).list();
  }

  /**
   * 获取用户角色列表.
   * 
   * @param userId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsRole> getRoleListByUserId(Long userId) {
    String hql = "select t from InsRole t where id.userId = ? ";
    return super.createQuery(hql, userId).list();
  }

  /**
   * 用户是否用户某机构的部门角色(包含部门管理员或者部门秘书).
   * 
   * @param psnId
   * @return
   */
  public Boolean hasUnitRole(Long psnId, Long insId) {
    String hql =
        "select count(id.insId) from InsRole where id.userId = ? and id.insId = ? and (id.roleId = 5 or id.roleId=6) ";
    Long count = super.findUnique(hql, psnId, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 查询指定用户所有角色
   * 
   * @param insId
   * @param psnIds
   * @return
   */
  public List<InsRole> getInsAllRole(Long insId, List<Long> psnIds) {
    String hql =
        "select new InsRole(t.id,t1.name) from InsRole t,Role t1 where t.id.roleId = t1.id and t.id.userId in(:psnIds) and t.id.insId = :insId ";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).setParameter("insId", insId).list();
  }

  /**
   * 删除Research Officer (RO)、Librarian (Library)、HR Officer (HRO)、System Admin角色
   * 
   * @param insId
   * @param psnId
   */
  public void delPsnAllSpecialRole(Long insId, Long psnId) {
    String hql =
        "delete from InsRole t where t.id.roleId in(2,8,11,13,7) and t.id.userId =:psnId and t.id.insId = :insId ";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("insId", insId).executeUpdate();
  }
}
