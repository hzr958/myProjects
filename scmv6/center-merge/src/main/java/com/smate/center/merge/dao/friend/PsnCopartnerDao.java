package com.smate.center.merge.dao.friend;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.friend.PsnCopartner;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnCopartnerDao extends SnsHibernateDao<PsnCopartner, Long> {

  @SuppressWarnings("unchecked")
  public List<PsnCopartner> findByCoPsnId(Long coPsnId) throws Exception {
    String hql = "from PsnCopartner t where t.coPsnId = :coPsnId";
    return super.createQuery(hql).setParameter("coPsnId", coPsnId).list();
  }
}
