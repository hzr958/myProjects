package com.smate.web.management.dao.institution.sns;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.institution.sns.PsnInsSns;

/**
 * 人与机构的关系dao.
 * 
 * @author oyh
 * 
 */
@Repository
public class PsnInsDao extends SnsHibernateDao<PsnInsSns, Serializable> {
  /**
   * 查找人员与单位关系.
   * 
   * @param psnId
   * @param insId
   * @return
   */
  public PsnInsSns findPsnInsSns(Long psnId, Long insId) {
    String hql = "from PsnInsSns t where t.pk.psnId = ? and t.pk.insId = ?";
    return super.findUnique(hql, psnId, insId);
  }

}
