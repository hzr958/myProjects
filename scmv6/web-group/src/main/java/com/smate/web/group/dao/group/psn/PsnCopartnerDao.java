package com.smate.web.group.dao.group.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.psn.PsnCopartner;

@Repository
public class PsnCopartnerDao extends SnsHibernateDao<PsnCopartner, Long> {


  public void deletePsnCopartner(Long psnId, int coType) {
    String hql = "delete from PsnCopartner t where t.psnId = :psnId and t.coType = :coType";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("coType", coType).executeUpdate();
  }

  public PsnCopartner getPsnCopartner(Long psnId, Long coPsnId, Long grpId, int coType) {
    String hql =
        "from PsnCopartner t where t.psnId= :psnId and t.coPsnId = :coPsnId and t.grpId = :grpId and t.coType = :coType";
    return (PsnCopartner) super.createQuery(hql).setParameter("psnId", psnId).setParameter("coPsnId", coPsnId)
        .setParameter("grpId", grpId).setParameter("coType", coType).uniqueResult();
  }

}
