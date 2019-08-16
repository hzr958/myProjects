package com.smate.core.base.utils.dao.security.role;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.exception.SysDataException;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;

/**
 * 单位域名设置DAO.
 * 
 * 
 * @author xys
 * 
 * @since 6.0.9
 * @version 6.0.9
 */
@Repository
public class Sie6InsPortalDao extends SieHibernateDao<Sie6InsPortal, Long> {

  /**
   * 批量获取单位中文名称
   * 
   * @param pageNo
   * @param pageSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findZhTitle(Integer pageNo, Integer pageSize) {
    String hql = "select p.zhTitle from Sie6InsPortal p order by p.insId asc ";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  /**
   * 
   * @param domain
   * @return
   */
  public Sie6InsPortal getEntity(String domain) {
    String hql = "from  Sie6InsPortal where domain = ?";
    Sie6InsPortal insPortal = super.findUnique(hql, domain);
    if (insPortal != null) {
      return insPortal;
    }
    return null;
  }

  /**
   * 保存域名信息修改lqh add.
   * 
   * @param insPort
   * @throws SysDataException
   */
  public void saveEntity(Sie6InsPortal insPort) throws SysDataException {

    super.save(insPort);
  }

  /**
   * 获取人员的所有Rol地址.
   * 
   * @param insPort
   * @throws SysDataException
   */
  @SuppressWarnings("unchecked")
  public List<Sie6InsPortal> findUserRolUrl(Long psnId) throws SysDataException {
    try {
      String hql = "from Sie6InsPortal where insId in (select pk.insId from PsnIns where status = 1 and pk.psnId=? )";
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
  public List<Sie6InsPortal> findUserRolUrl(Long psnId, Long insId) throws SysDataException {
    try {
      String hql = "from Sie6InsPortal where insId in (select pk.insId from PsnIns where pk.psnId=?  and pk.insId<>?)";
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
  public Sie6InsPortal findInsPortalByName(String insName) throws SysDataException {
    try {
      String hql = "from Sie6InsPortal where zh_title=?";
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
    String hql = "select rolNodeId from Sie6InsPortal where insId = ? ";
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
  public List<Sie6InsPortal> findInsPortalByKey(String key) throws SysDataException {
    String hql = "from Sie6InsPortal t where t.zhTitle like ? or t.enTitle like ? ";
    return super.createQuery(hql, "%" + key + "%", "%" + key + "%").setMaxResults(300).list();
  }

  /**
   * 单位域名列表.
   * 
   * @param insIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Sie6InsPortal> getInsPortalByInsIds(List<Long> insIds) {

    String hql = "from Sie6InsPortal t where t.insId in(:insIds)";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 单位域名是否已存在
   * 
   * @param domain
   * @param insId
   * @return
   * @throws DaoException
   */
  public Sie6InsPortal findInsPortalByCheck(String domain, Long insId) {

    if (insId == null) {
      String hql = "from Sie6InsPortal t where lower(t.domain)=?";
      return this.findUnique(hql, domain.toLowerCase());
    } else {
      String hql = "from Sie6InsPortal t where lower(t.domain)=? and insId<>?";
      return this.findUnique(hql, domain.toLowerCase(), insId);
    }

  }

  public void deleteByInsId(Long mergeid) {
    String hql = "delete from SieInsPortal t where t.insId = ? ";
    super.createQuery(hql, mergeid).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Sie6InsPortal> getListByInsId(Long insId) {
    String hql = " from Sie6InsPortal t where t.insId= ?";
    return super.createQuery(hql, insId).list();
  }

  /**
   * 查询单位域名LOGO为空的单位域名
   * 
   * @param page
   * @return page
   */
  @SuppressWarnings("unchecked")
  public Page<Sie6InsPortal> findInsProtalByWithoutLogo(Page<Sie6InsPortal> page) {
    String hql = "from Sie6InsPortal t where t.logo is null or t.logo = '' order by t.insId desc";
    String countHql = "select count(t.insId) from Sie6InsPortal t where t.logo is null or t.logo = ''";
    Query q = createQuery(hql);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(countHql);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<Sie6InsPortal> result = q.list();
    page.setResult(result);
    return page;
  }

}
