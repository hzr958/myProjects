package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmIsiConame;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 用户确认成果合作者.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmIsiConameDao extends RolHibernateDao<PsnPmIsiConame, Long> {

  /**
   * 获取确认的合作者.
   * 
   * @param initName
   * @param fullName
   * @param psnId
   * @return
   */
  public PsnPmIsiConame getPsnPmIsiConame(String initName, String fullName, Long psnId) {
    StringBuilder sb = new StringBuilder("from PsnPmIsiConame where initName = ? and psnId = ? ");
    List<Object> p = new ArrayList<Object>();
    p.add(initName);
    p.add(psnId);
    if (StringUtils.isNotBlank(fullName)) {
      sb.append(" and fullName = ? ");
      p.add(fullName);
    } else {
      sb.append(" and fullName is null ");
    }
    List<PsnPmIsiConame> list = super.find(sb.toString(), p.toArray());
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 是否存在合作者.
   * 
   * @param initName
   * @param fullName
   * @param psnId
   * @return
   */
  public boolean isExitConame(String initName, String fullName, Long psnId) {

    // 判断跟本人用户名是否一样
    String hql = "select count(id) from PsnPmIsiName where name in(:names) and psnId = :psnId ";
    List<Object> names = new ArrayList<Object>();
    names.add(initName);
    if (StringUtils.isNotBlank(fullName)) {
      names.add(fullName);
    }
    Long count =
        (Long) super.createQuery(hql).setParameterList("names", names).setParameter("psnId", psnId).uniqueResult();
    if (count > 0) {
      return true;
    }
    // 判断是否存在相同合作者
    StringBuilder sb = new StringBuilder("select count(id) from PsnPmIsiConame where psnId = ? and ( ");
    List<Object> p = new ArrayList<Object>();
    p.add(psnId);
    if (StringUtils.isNotBlank(fullName)) {
      sb.append(" fullName = ? ");
      p.add(fullName);
    } else {
      sb.append(" initName = ? ");
      p.add(initName);
    }
    sb.append(" )");
    count = super.findUnique(sb.toString(), p.toArray());
    return count > 0;
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmIsiConame> getPsnPmIsiConameList(Long psnId) {

    String ql = "from PsnPmIsiConame where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmIsiConame where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
