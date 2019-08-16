package com.smate.web.v8pub.dao.sns.psn;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.psn.PsnCopartner;

@Repository
public class PsnCopartnerDao extends SnsHibernateDao<PsnCopartner, Long> {


  public void deletePsnCopartner(Long psnId, int coType) {
    String hql = "delete from PsnCopartner t where t.psnId = :psnId and t.coType = :coType";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("coType", coType).executeUpdate();
  }

  public PsnCopartner getPsnCopartner(Long psnId, Long coPsnId, Long pdwhPubId, int coType) {
    String hql =
        "from PsnCopartner t where t.psnId= :psnId and t.coPsnId = :coPsnId and t.pdwhPubId = :pdwhPubId and t.coType = :coType";
    return (PsnCopartner) super.createQuery(hql).setParameter("psnId", psnId).setParameter("coPsnId", coPsnId)
        .setParameter("pdwhPubId", pdwhPubId).setParameter("coType", coType).uniqueResult();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveWithNewTransaction(PsnCopartner psnCopartner) {
    super.saveOrUpdate(psnCopartner);
  }

}
