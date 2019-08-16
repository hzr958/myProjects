package com.smate.core.base.utils.dao.menu;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.model.menu.ResourceMapping;


/**
 * 
 * 获得资源映射数据，菜单功能使用_MJG_CITYU-109.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */

@Repository
public class ResourceMappingDao extends HibernateDao<ResourceMapping, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 根据URL获取菜单ID<角色必须为空>.
   * 
   * @param url
   * @return
   */
  @SuppressWarnings("rawtypes")
  public Long getMenuIdByUrl(String url) {
    String hql = "select resourceId from ResourceMapping t where t.roleId is null and t.url=? ";
    List menuList = super.createQuery(hql, url).list();
    if (CollectionUtils.isNotEmpty(menuList)) {
      return Long.valueOf(String.valueOf(menuList.get(0)));
    }
    return null;
  }

  /**
   * 根据URL和角色ID获取菜单ID.
   * 
   * @param roleId
   * @param url
   * @return
   */
  public Long getMenuIdByUrlAndRole(Long roleId, String url) {
    String hql = "select resourceId from ResourceMapping t where t.roleId=? and t.url=? ";
    Object obj = super.createQuery(hql, roleId, url).uniqueResult();
    if (obj != null) {
      return Long.valueOf(String.valueOf(obj));
    }
    return null;
  }

  /**
   * 获取菜单ID对应的链接地址列表.
   * 
   * @param resourceId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getUrlListByMenuId(Long resourceId) {
    String hql = "select url from ResourceMapping t where t.resourceId=? ";
    return super.createQuery(hql, resourceId).list();
  }

  /**
   * 根据ID获取菜单ID.
   * 
   * @param id
   * @return
   */
  public Long getMenuIdById(Long id) {
    String hql = "select resourceId from ResourceMapping t where t.id=? ";
    Object obj = super.createQuery(hql, id).uniqueResult();
    if (obj != null) {
      return Long.valueOf(String.valueOf(obj));
    }
    return null;
  }

  /**
   * 根据ID获取映射记录信息.
   * 
   * @param id
   * @return
   */
  public ResourceMapping getMenuMappingByID(Long id) {
    String hql = "from ResourceMapping t where t.id=? ";
    Object obj = super.createQuery(hql, id).uniqueResult();
    if (obj != null) {
      return (ResourceMapping) obj;
    }
    return null;
  }
}
