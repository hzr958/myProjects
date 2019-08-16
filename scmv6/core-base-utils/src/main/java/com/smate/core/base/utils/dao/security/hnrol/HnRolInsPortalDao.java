package com.smate.core.base.utils.dao.security.hnrol;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.HnRolHibernateDao;
import com.smate.core.base.utils.exception.SysDataException;
import com.smate.core.base.utils.model.InsPortalExtend;
import com.smate.core.base.utils.model.hnrol.HnRolInsPortal;


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
public class HnRolInsPortalDao extends HnRolHibernateDao<HnRolInsPortal, Long> {

  /**
   * 批量获取单位中文名称
   * 
   * @param pageNo
   * @param pageSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findZhTitle(Integer pageNo, Integer pageSize) {
    String hql = "select p.zhTitle from HnRolInsPortal p order by p.insId asc ";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  /**
   * 
   * @param domain
   * @return
   */
  public HnRolInsPortal getEntity(String domain) {
    String hql = "from  HnRolInsPortal where domain = ?";
    HnRolInsPortal insPortal = super.findUnique(hql, domain);
    if (insPortal != null) {
      return insPortal;
    }
    hql = "from InsPortalExtend where domain = ? ";
    InsPortalExtend insPortalE = super.findUnique(hql, domain);
    if (insPortalE != null) {
      hql = "from  HnRolInsPortal where insId = ?";
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
  public void saveEntity(HnRolInsPortal insPort) throws SysDataException {

    super.save(insPort);
  }

  /**
   * 获取人员的所有Rol地址.
   * 
   * @param insPort
   * @throws SysDataException
   */
  @SuppressWarnings("unchecked")
  public List<HnRolInsPortal> findUserRolUrl(Long psnId) throws SysDataException {
    try {
      String hql = "from HnRolInsPortal where insId in (select pk.insId from PsnIns where status = 1 and pk.psnId=? )";
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
  public List<HnRolInsPortal> findUserRolUrl(Long psnId, Long insId) throws SysDataException {
    try {
      String hql = "from HnRolInsPortal where insId in (select pk.insId from PsnIns where pk.psnId=?  and pk.insId<>?)";
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
  public HnRolInsPortal findInsPortalByName(String insName) throws SysDataException {
    try {
      String hql = "from HnRolInsPortal where zh_title=?";
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
    String hql = "select rolNodeId from InsPortal where insId = ? ";
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
  public List<HnRolInsPortal> findInsPortalByKey(String key) throws SysDataException {
    String hql = "from HnRolInsPortal t where t.zhTitle like ? or t.enTitle like ? ";
    return super.createQuery(hql, "%" + key + "%", "%" + key + "%").setMaxResults(300).list();
  }

  /**
   * 单位域名列表.
   * 
   * @param insIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<HnRolInsPortal> getInsPortalByInsIds(List<Long> insIds) {

    String hql = "from HnRolInsPortal t where t.insId in(:insIds)";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }
}
