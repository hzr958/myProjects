package com.smate.center.task.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhMemberEmailPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhMemberEmailDAO extends PdwhHibernateDao<PdwhMemberEmailPO, Long> {

  public void deleteAll(Long pdwhPubId) {
    String hql = "delete from PdwhMemberEmailPO t where t.pdwhPubId =:pdwhPubId";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<String> getPubMemberEmailList(Long pdwhPubId) {
    String hql = "select t.email from PdwhMemberEmailPO t where t.pdwhPubId =:pdwhPubId "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
    return createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdByEmail(String psnEmail) {
    String hql = "select t.pdwhPubId from PdwhMemberEmailPO t where t.email =:email "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
    return createQuery(hql).setParameter("email", psnEmail).list();
  }
}
