package com.smate.center.task.dao.rol.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PsnPmIsiConame;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PsnPmIsiConameDao extends RolHibernateDao<PsnPmIsiConame, Long> {
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
    String hql = "select count(id) from PsnPmIsiName where name in(:names) and psnId =:psnId ";
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

}
