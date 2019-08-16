package com.smate.center.task.dao.tmp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.tmp.TmpNsfcKeywords;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class TmpNsfcKeywordsDao extends SnsbakHibernateDao<TmpNsfcKeywords, Long> {

  @SuppressWarnings("unchecked")

  public List<TmpNsfcKeywords> getNsfcKwsByHash(Long keywordHash) {
    String hql = "  from TmpNsfcKeywords where keywordsHash=:keywordHash";
    return super.createQuery(hql).setParameter("keywordHash", keywordHash).list();

  }

  @SuppressWarnings("unchecked")
  public List<TmpNsfcKeywords> getNoneKwsHashData() {
    String hql = " from TmpNsfcKeywords where keywordsHash is null and keywords is not null";
    return super.createQuery(hql).setMaxResults(500).list();
  }

  public void generatePubKeywordHash(Long id, Long keywordHash) {
    String hql = " update TmpNsfcKeywords set keywordsHash=:keywordsHash where id=:id";
    super.createQuery(hql).setParameter("keywordsHash", keywordHash).setParameter("id", id).executeUpdate();
  }

}
