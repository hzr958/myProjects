package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsets;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubKeywordsSubsetsDao extends PdwhHibernateDao<PubKeywordsSubsets, Long> {
  @SuppressWarnings("unchecked")
  public List<String> getNsfcKwsByLanguage(Integer language) {
    String hql = "select distinct(t.content) from PubKeywordsSubsets t where t.language =:language and t.size = 1";
    return super.createQuery(hql).setParameter("language", language).list();
  }
}
