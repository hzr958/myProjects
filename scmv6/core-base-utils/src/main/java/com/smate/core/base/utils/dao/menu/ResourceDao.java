package com.smate.core.base.utils.dao.menu;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.menu.Resource;


/**
 * 
 * 获得资源数据，权限与菜单功能使用.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */

@Repository
public class ResourceDao extends SnsHibernateDao<Resource, Long> {// HibernateDao<Resource, Long>

  /**
   * 
   * @param psnId person id.
   * @return items.
   * 
   */
  @SuppressWarnings("unchecked")
  public List<Resource> getResourcesWithPsn(final Long psnId, final Long insId, final Integer roleId) {

    List<Resource> items = null;
    Query query = null;

    String hql = "Select distinct menu FROM Resource as menu  ";
    if (insId == null) {
      hql +=
          " where    menu.resourceType = 'menu' and menu.status not in(0) order by menu.lavels ASC,menu.parentId ASC,menu.orderNum ASC ";
      query = createQuery(hql);
    } else if (roleId == null) {
      hql += "  join menu.authorities as author "
          + "  join author.roles as roles  where  roles in (select id.roleId from InsRole where id.userId = ? and id.insId = ?) and menu.status not in(0)  and  menu.resourceType = 'menu'  order by menu.lavels ASC,menu.parentId ASC, menu.orderNum ASC";
      query = createQuery(hql, psnId, insId);
    } else {
      hql += "  join menu.authorities as author "
          + "  join author.roles as roles  where  roles in (select id.roleId from InsRole where id.userId = ? and id.insId = ?  and id.roleId = ? ) and  menu.resourceType = 'menu' and menu.status not in(0)  order by menu.lavels ASC,menu.parentId ASC, menu.orderNum ASC";
      query = createQuery(hql, psnId, insId, roleId.longValue());
    }
    String runEnv = System.getenv("RUN_ENV");
    if (!("development").equalsIgnoreCase(runEnv)) {
      query.setCacheable(true);
    }
    items = query.list();

    return items;
  }

  /**
   * 
   * @param menuId tgii.
   * @return items.
   */
  public List<Resource> getResourcesWithId(final Long menuId) {
    String hql = " FROM Resource as menu where menu.parentId = ? ";
    List<Resource> items = find(hql, new Object[] {menuId});
    return items;
  }

  /**
   * 获取所有菜单的链接地址.
   * 
   * @author kexitang
   * 
   * @return menuIds
   */
  @SuppressWarnings("rawtypes")
  public List getAllMenus() {
    Query query = null;
    String hql = "select id,value from Resource where resourceType='menu' order by lavels desc,orderNum";
    query = createQuery(hql);
    query.setCacheable(true);
    List menuIds = query.list();
    return menuIds;

  }

  /**
   * 根据URL地址找匹配的菜单ID列表.
   * 
   * @param url
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getMenuIdListByUrl(String url) {
    String hql = "select id from Resource t where t.resourceType='menu' and t.status=1 and t.value like ? ";
    return super.createQuery(hql, url + "%").list();
  }

  /**
   * 根据URL和角色查找匹配的菜单ID列表.
   * 
   * @param url
   * @param psnId
   * @param roleId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getMenuIdListByRoleAndUrl(String url, Long psnId, Long roleId, Long insId) {
    String hql =
        "Select distinct menu.id FROM Resource as menu join menu.authorities as author join author.roles as roles "
            + "where  roles in (select id.roleId from InsRole where id.userId = ? and id.insId = ?  and id.roleId = ? ) "
            + "and  menu.resourceType = 'menu' and menu.status=1 and menu.value like ?";
    List<Long> menuIdList = super.createQuery(hql, psnId, insId, roleId, "%" + url + "%").list();
    return menuIdList;
  }



}
