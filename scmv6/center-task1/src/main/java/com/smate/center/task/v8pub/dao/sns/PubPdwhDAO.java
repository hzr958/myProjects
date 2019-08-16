package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.apache.tools.ant.types.resources.selectors.Date;
import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * @author houchuanjie
 * @date 2018/06/01 17:44
 */
@Repository
public class PubPdwhDAO extends PdwhHibernateDao<PubPdwhPO, Long> {

  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> getconfirmPubDetail(List<Long> pdwhPubIds) {
    String hql = "from PubPdwhPO t where t.pubId in (:pubIds) and t.status = 0";
    return super.createQuery(hql).setParameterList("pubIds", pdwhPubIds).list();
  }

  public String getPdwhPubTitle(Long pdwhPubId) {
    String hql = "select t.title from PubPdwhPO t where t.pubId = :pdwhPubId and t.status = 0";
    return (String) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  public void updatePubUpdateTime(Long pdwhPubId) {
    String hql = "update PubPdwhPO t set t.gmtModified = :date where t.pubId = :pdwhPubId and t.status = 0";
    super.createQuery(hql).setParameter("date", new Date()).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> getPubDetails(List<Long> pdwhPubIds) {
    String pubIds = "";
    for (int i = 0; i < pdwhPubIds.size(); i++) {
      pubIds = pubIds + pdwhPubIds.get(i) + ",";
    }
    pubIds = pubIds.substring(0, pubIds.length() - 1);
    String hql =
        "from PubPdwhPO t where t.pubId in (:pubIds) and t.status = 0 order by instr('" + pubIds + "',t.pubId)";
    return super.createQuery(hql).setParameterList("pubIds", pdwhPubIds).setMaxResults(3).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> findPubByBatchSize(Long lastId, Long maxPubId) {
    String hql = "from PubPdwhPO t where t.pubType in (1, 2, 3, 4, 7, 8, 10) and t.pubId>:lastId "
        + "and t.pubId <= :maxPubId and t.status = 0 order by t.pubId";
    return super.createQuery(hql).setParameter("lastId", lastId).setParameter("maxPubId", maxPubId).setMaxResults(2000)
        .list();
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> findPatByBatchSize(Long lastId, Long maxPubId) {
    String hql = "from PubPdwhPO t where t.pubType = 5 and t.pubId>:lastId "
        + "and t.pubId<=:maxPubId and t.status = 0 order by t.pubId";
    return super.createQuery(hql).setParameter("lastId", lastId).setParameter("maxPubId", maxPubId).setMaxResults(2000)
        .list();
  }

  /**
   * 查询最近一个月更新的成果
   * 
   * @param startIndex
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> getUpdatePdwhMonth(Integer startIndex, Integer size) {
    String hql = "select new PubPdwhPO(pubId,title) FROM PubPdwhPO where status=0 ";
    return super.createQuery(hql).setFirstResult(startIndex).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> listByPubIds(List<Long> repeatPubList) {
    String hql = "from PubPdwhPO t where t.pubId in (:pubIds) and t.status = 0";
    return super.createQuery(hql).setParameterList("pubIds", repeatPubList).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> filterNotExistsPub(List<Long> repeatPubList) {
    String hql = "select t.pubId from PubPdwhPO t where t.pubId in (:pubIds) and t.status = 0";
    return super.createQuery(hql).setParameterList("pubIds", repeatPubList).list();
  }

  public PubPdwhPO getPdwhPub(Long pdwhPubId) {
    String hql =
        "select new PubPdwhPO(t.pubId,t.publishYear,t.citations) from PubPdwhPO t where t.pubId = :pdwhPubId and t.status = 0";
    return (PubPdwhPO) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }
}
