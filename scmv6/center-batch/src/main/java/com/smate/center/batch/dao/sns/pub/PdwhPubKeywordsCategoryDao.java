package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PdwhPubKeywordsCategory;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PdwhPubKeywordsCategoryDao extends SnsHibernateDao<PdwhPubKeywordsCategory, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubKeywordsCategory> getPdwhPubKeywords(Integer size, Long startPubId, Long endPubId) {
    String hql =
        "from PdwhPubKeywordsCategory t where t.status=0 and t.id > :startPubId and t.id <= :endPubId order by t.id ";
    return createQuery(hql).setParameter("startPubId", startPubId).setParameter("endPubId", endPubId)
        .setMaxResults(size).list();
  }

  public void saveOpResult(Long pubId, int status) {
    String hql = "update PdwhPubKeywordsCategory  t set t.status = :status where t.pubId = :pubId";
    super.createQuery(hql).setParameter("status", status).setParameter("pubId", pubId).executeUpdate();
  }

  public void saveOpListResult(List<Long> pubIdList, int status) {
    String hql = "update PdwhPubKeywordsCategory  t set t.status = :status where t.pubId in (:pubIds)";
    super.createQuery(hql).setParameter("status", status).setParameterList("pubIds", pubIdList).executeUpdate();
  }

}
