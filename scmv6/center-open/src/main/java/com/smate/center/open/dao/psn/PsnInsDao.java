package com.smate.center.open.dao.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.PsnIns;
import com.smate.core.base.utils.model.security.PsnInsPk;

/**
 * 单位人员.
 * 
 * @author aijiangbin
 *
 */
@Repository
public class PsnInsDao extends SnsHibernateDao<PsnIns, PsnInsPk> {

  /**
   * 
   * @param psnId
   * @param insId
   * @return
   */
  public boolean findPsnIsIns(Long psnId, Long insId) {
    String hql = "from PsnIns t where t.pk.psnId =:psnId and t.pk.insId =:insId and t.status = 1 ";
    Object object = this.createQuery(hql).setParameter("psnId", psnId).setParameter("insId", insId).uniqueResult();
    return object != null ? true : false;
  }
}
