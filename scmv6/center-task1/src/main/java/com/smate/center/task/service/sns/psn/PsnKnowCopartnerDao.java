package com.smate.center.task.service.sns.psn;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.PsnKnowCopartner;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnKnowCopartnerDao extends SnsHibernateDao<PsnKnowCopartner, Serializable> {
  public void delPsnKnowCopartnerByPsnId(Long psnId) {
    String hql = "delete from PsnKnowCopartner where psnId = :psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  public PsnKnowCopartner getPsnKnowCopartner(Long psnId, Long cptPsnId) {
    String hql = "from PsnKnowCopartner where psnId = :psnId and cptPsnId = :cptPsnId";
    return (PsnKnowCopartner) super.createQuery(hql).setParameter("psnId", psnId).setParameter("cptPsnId", cptPsnId)
        .uniqueResult();
  }
}
