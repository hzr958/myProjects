package com.smate.web.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhMemberEmailPO;

import java.util.List;

@Repository
public class PdwhMemberEmailDAO extends PdwhHibernateDao<PdwhMemberEmailPO, Long> {

  public void deleteAll(Long pdwhPubId) {
    String hql = "delete from PdwhMemberEmailPO t where t.pdwhPubId =:pdwhPubId";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }


  public List<PdwhMemberEmailPO> findByPubId(Long pdwhPubId) {
    String hql = "from PdwhMemberEmailPO t where t.pdwhPubId =:pdwhPubId";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }
}
