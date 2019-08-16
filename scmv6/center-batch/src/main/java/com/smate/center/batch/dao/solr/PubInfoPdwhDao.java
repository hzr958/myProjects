package com.smate.center.batch.dao.solr;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.solr.PubInfoPdwh;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubInfoPdwhDao extends PdwhHibernateDao<PubInfoPdwh, Long> {

  @SuppressWarnings("unchecked")
  public List<PubInfoPdwh> getPubByHash(Long zhHash, Long enHash) {

    String hql =
        "select new PubInfoPdwh(pubAllId, pubId, dbId, zhTitleHash, enTitleHash) from PubInfoPdwh t where t.zhTitleHash = :zhHash and t.enTitleHash=:enHash";

    return super.createQuery(hql).setParameter("zhHash", zhHash).setParameter("enHash", enHash).list();
  }

  public Long getMaxPubAllId() {
    String hql = "select max(t.pubAllId) from PubInfoPdwh t";
    return super.findUnique(hql);
  }

  public Long getMinPubAllId() {
    String hql = "select min(t.pubAllId) from PubInfoPdwh t";
    // String hql = " from PubInfoPdwh t";
    return super.findUnique(hql);
  }

  @SuppressWarnings("unchecked")
  public List<PubInfoPdwh> findPubByBatchSize(Long lastId, Integer batchSize) {
    String hql =
        "from PubInfoPdwh t where t.pubType in (1, 2, 3, 4, 7, 8, 10) and t.pubAllId>:lastId order by t.pubAllId asc";
    // return super.queryForList(sql, new Object[] { lastId, lastId +
    // batchSize, 0 });
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubInfoPdwh> findPatByBatchSize(Long lastId, Integer batchSize) {
    String hql = "from PubInfoPdwh t where t.pubType = 5 and t.pubAllId>:lastId order by t.pubAllId asc";
    // return super.queryForList(sql, new Object[] { lastId, lastId +
    // batchSize, 0 });
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubInfoPdwh> findAllPubByBatchSize(Long lastId, Integer batchSize) {
    String hql = "from PubInfoPdwh t where t.pubAllId>:lastId order by t.pubAllId asc";
    // return super.queryForList(sql, new Object[] { lastId, lastId +
    // batchSize, 0 });
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findPubAllIdBySize(Long lastId, Integer batchSize) {
    String hql = "select t.pubAllId from PubInfoPdwh t where t.pubAllId>:lastId order by t.pubAllId asc";

    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
  }

}
