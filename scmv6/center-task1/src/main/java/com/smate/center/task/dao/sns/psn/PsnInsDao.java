package com.smate.center.task.dao.sns.psn;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.psn.PsnInsSns;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.PsnInsPk;

/**
 * 单位人员.
 * 
 * @author hd
 * 
 */
@Repository
public class PsnInsDao extends SnsHibernateDao<PsnInsSns, PsnInsPk> {
  /**
   * 
   * @param psnId
   * @param insId
   * @return
   * @throws DaoException
   */
  public boolean findPsnIsIns(Long psnId, Long insId) throws DaoException {
    PsnInsSns rolpsn = findUnique("from PsnInsSns t where t.pk.psnId = ? and t.pk.insId = ?", psnId, insId);
    return rolpsn != null ? true : false;
  }


}
