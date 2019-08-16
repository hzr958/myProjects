package com.smate.center.task.dao.rol.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PsnPmSpsConame;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PsnPmSpsConameDao extends RolHibernateDao<PsnPmSpsConame, Long> {

  public boolean isExitConame(String name, Long psnId) {
    StringBuilder sb = new StringBuilder("select count(id) from PsnPmSpsConame where name =:name and psnId =:psnId ");
    Long count =
        (Long) super.createQuery(sb.toString()).setParameter("name", name).setParameter("psnId", psnId).uniqueResult();
    return count > 0;
  }

  /**
   * 是否存在合作者.
   * 
   * @param initName
   * @param fullName
   * @param psnId
   * @return
   */
  public boolean isExitCoPreName(String preName, Long psnId) {
    String sb = "select count(id) from PsnPmSpsCoPreName where preName =:preName and psnId =:psnId";
    Long count =
        (Long) super.createQuery(sb).setParameter("preName", preName).setParameter("psnId", psnId).uniqueResult();
    return count > 0;
  }


}
