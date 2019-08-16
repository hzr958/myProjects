package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PsnPmCnkiConame;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 用户确认Cnki成果合作者.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmCnkiConameDao extends RolHibernateDao<PsnPmCnkiConame, Long> {
  /**
   * 判断用户确认的合作者是否存在.
   * 
   * @param name
   * @param psnId
   * @return
   */
  public PsnPmCnkiConame getCnkiConame(String name, Long psnId) {

    String hql = "from PsnPmCnkiConame where name =:name and psnId =:psnId ";
    @SuppressWarnings("unchecked")
    List<PsnPmCnkiConame> list =
        super.createQuery("hql").setParameter("name", name).setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
