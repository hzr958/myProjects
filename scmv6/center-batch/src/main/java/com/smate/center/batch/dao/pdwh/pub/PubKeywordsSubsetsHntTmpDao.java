package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsetsHntTmp;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubKeywordsSubsetsHntTmpDao extends PdwhHibernateDao<PubKeywordsSubsetsHntTmp, String> {

  @SuppressWarnings("unchecked")
  public List<PubKeywordsSubsetsHntTmp> getCategory(Integer status) {
    String hql = "from PubKeywordsSubsetsHntTmp t where t.status=:status";
    return super.createQuery(hql).setParameter("status", status).list();
  }

  public void updateCategory(PubKeywordsSubsetsHntTmp pt) {
    if (pt == null) {
      return;
    }
    String sql = "update V_KEYWORD_SUBSET_HNT_TMP t set t.status =:status where t.category =:category";
    super.getSession().createSQLQuery(sql).setParameter("status", pt.getStatus()).setParameter("category", pt.getId())
        .executeUpdate();
  }
}
