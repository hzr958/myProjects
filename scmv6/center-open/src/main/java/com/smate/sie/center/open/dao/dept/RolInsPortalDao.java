package com.smate.sie.center.open.dao.dept;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.sie.publication.SieInsPortal;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.exception.SysDataException;
import com.smate.core.base.utils.model.rol.SieInsPortalExtend;

/**
 * 单位门户信息表DAO.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Repository
public class RolInsPortalDao extends RolHibernateDao<SieInsPortal, Long> {

  /**
   * 批量获取单位中文名称
   * 
   * @param pageNo
   * @param pageSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findZhTitle(Integer pageNo, Integer pageSize) {
    String hql = "select p.zhTitle from SieInsPortal p order by p.insId asc ";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  /**
   * 
   * @param domain
   * @return
   */
  public SieInsPortal getEntity(String domain) {
    String hql = "from  SieInsPortal where domain = ?";
    SieInsPortal insPortal = super.findUnique(hql, domain);
    if (insPortal != null) {
      return insPortal;
    }
    hql = "from SieInsPortalExtend where domain = ? ";
    SieInsPortalExtend insPortalE = super.findUnique(hql, domain);
    if (insPortalE != null) {
      hql = "from  SieInsPortal where insId = ?";
      return super.findUnique(hql, insPortalE.getInsId());
    }
    return null;
  }

  /**
   * 保存域名信息修改lqh add.
   * 
   * @param insPort
   * @throws SysDataException
   */
  public void saveEntity(SieInsPortal insPort) throws SysDataException {

    super.save(insPort);
  }

  /**
   * 获取人员的所有Rol地址.
   * 
   * @param insPort
   * @throws SysDataException
   */
  @SuppressWarnings("unchecked")
  public List<SieInsPortal> findUserRolUrl(Long psnId) throws SysDataException {
    try {
      String hql = "from SieInsPortal where insId in (select pk.insId from PsnIns where status = 1 and pk.psnId=? )";
      Query query = createQuery(hql, psnId);
      // query.setCacheable(true);
      return query.list();
    } catch (Exception e) {
      return null;
    }

  }

  /**
   * 获取人员的所有Rol地址,不包含本单位.
   * 
   * @param insPort
   * @throws SysDataException
   */
  @SuppressWarnings("unchecked")
  public List<SieInsPortal> findUserRolUrl(Long psnId, Long insId) throws SysDataException {
    try {
      String hql = "from SieInsPortal where insId in (select pk.insId from PsnIns where pk.psnId=?  and pk.insId<>?)";
      Query query = createQuery(hql, psnId, insId);
      // query.setCacheable(true);
      return query.list();
    } catch (Exception e) {
      return null;
    }

  }

  /**
   * 查看.
   * 
   * @param insName
   * @return
   * @throws SysDataException
   */
  public SieInsPortal findInsPortalByName(String insName) throws SysDataException {
    try {
      String hql = "from SieInsPortal where zh_title=?";
      return super.findUnique(hql, insName);
    } catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获取单位节点.
   * 
   * @param insId
   * @return
   */
  public Integer getInsNode(Long insId) {
    String hql = "select rolNodeId from SieInsPortal where insId = ? ";
    return super.findUnique(hql, insId);
  }

  /**
   * 关键字查找.
   * 
   * @param key
   * @return
   * @throws SysDataException
   */
  @SuppressWarnings("unchecked")
  public List<SieInsPortal> findInsPortalByKey(String key) throws SysDataException {
    String hql = "from SieInsPortal t where t.zhTitle like ? or t.enTitle like ? ";
    return super.createQuery(hql, "%" + key + "%", "%" + key + "%").setMaxResults(300).list();
  }

  /**
   * 单位域名列表.
   * 
   * @param insIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieInsPortal> getInsPortalByInsIds(List<Long> insIds) {

    String hql = "from SieInsPortal t where t.insId in(:insIds)";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }
}
