package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmIsiName;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 
 * 用户ISI名称列表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmIsiNameDao extends RolHibernateDao<PsnPmIsiName, Long> {

  /**
   * 判断用户确认的用户名称是否存在.
   * 
   * @param name
   * @param psnId
   * @return
   */
  public boolean isAddtNameExists(String name, Long psnId) {

    String hql = "select count(id) from PsnPmIsiName where name = ? and psnId = ? ";
    Long count = super.findUnique(hql, name, psnId);
    if (count > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 判断用户确认的用户名称是否存在.
   * 
   * @param name
   * @param psnId
   * @return
   */
  public boolean existsPsnPmName(Long psnId) {

    String hql = "select count(id) from PsnPmIsiName where psnId = ? ";
    Long count = super.findUnique(hql, psnId);
    if (count > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 保存用户名称前缀,li qinghua:li q.
   * 
   * @param prefix
   * @param psnId
   */
  public void savePrefixName(Set<String> prefixs, Long psnId) {
    if (prefixs == null || prefixs.size() == 0) {
      return;
    }
    super.createQuery("delete from PsnPmIsiName where psnId = ? and type = 2  ", psnId).executeUpdate();
    for (String prefix : prefixs) {
      PsnPmIsiName name = new PsnPmIsiName(prefix, psnId, 2);
      super.save(name);
    }
  }

  /**
   * 保存用户名称简写，如果li qinghua:li qh,li q h.
   * 
   * @param initName
   * @param psnId
   */
  public void saveInitName(Set<String> initNames, Long psnId) {

    if (initNames == null || initNames.size() == 0) {
      return;
    }
    super.createQuery("delete from PsnPmIsiName where psnId = ? and type = 5", psnId).executeUpdate();
    for (String initName : initNames) {
      PsnPmIsiName name = new PsnPmIsiName(initName, psnId, 5);
      super.save(name);
    }
  }

  /**
   * 保存用户全称.
   * 
   * @param fullNames
   * @param psnId
   */
  public void saveFullName(Set<String> fullNames, Long psnId) {

    if (CollectionUtils.isEmpty(fullNames)) {
      return;
    }
    super.createQuery("delete from PsnPmIsiName where psnId = ? and type = 1", psnId).executeUpdate();
    for (String fullName : fullNames) {
      PsnPmIsiName name = new PsnPmIsiName(fullName, psnId, 1);
      super.save(name);
    }
  }

  /**
   * 获取用户全称列表.
   * 
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Long, List<String>> getUserFullName(Set<Long> psnIds) {

    if (CollectionUtils.isEmpty(psnIds)) {
      return null;
    }
    Map<Long, List<String>> map = new HashMap<Long, List<String>>();
    String hql = "from PsnPmIsiName where  psnId in(:psnIds) and type in(1,4)";
    List<PsnPmIsiName> list = super.createQuery(hql).setParameterList("psnIds", psnIds).list();
    for (PsnPmIsiName name : list) {
      Long psnId = name.getPsnId();
      List<String> nameList = map.get(psnId) == null ? new ArrayList<String>() : map.get(psnId);
      nameList.add(name.getName());
      map.put(psnId, nameList);
    }
    return map;
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmIsiName> getPsnPmIsiNameList(Long psnId) {

    String ql = "from PsnPmIsiName where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 删除用户别名，除前缀外.
   * 
   * @param name
   * @param psnId
   */
  public void removeUserName(String name, Long psnId) {
    super.createQuery("delete from PsnPmIsiName where name = ? and type <> 2 and psnId = ? ", name, psnId)
        .executeUpdate();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmIsiName where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
