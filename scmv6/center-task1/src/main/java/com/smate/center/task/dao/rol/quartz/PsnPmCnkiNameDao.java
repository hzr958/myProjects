package com.smate.center.task.dao.rol.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PsnPmCnkiName;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PsnPmCnkiNameDao extends RolHibernateDao<PsnPmCnkiName, Long> {

  public boolean isAddtNameExists(String name, Long psnId) {
    String hql = "select count(id) from PsnPmCnkiName where name =:name and psnId =:psnId ";
    Long count = (Long) super.createQuery(hql).setParameter("name", name).setParameter("psnId", psnId).uniqueResult();
    if (count > 0) {
      return true;
    } else {
      return false;
    }
  }

}
