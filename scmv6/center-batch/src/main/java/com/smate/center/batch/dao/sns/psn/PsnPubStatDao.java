package com.smate.center.batch.dao.sns.psn;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PsnPubStat;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人成果库统计.
 * 
 * @author yamingd
 * 
 */
@Repository
public class PsnPubStatDao extends SnsHibernateDao<PsnPubStat, Long> {

  /**
   * 增加成果总数.
   * 
   * @param psnId
   * @param incr @
   */
  public void incrTotalOutputs(Long psnId, Integer incr) {
    String hql = "update PsnPubStat set total = total + ?,lastUpdate=? where psnId = ?";
    super.createQuery(hql, incr, new Date(), psnId).executeUpdate();
  }

  /**
   * 增加成果总数.
   * 
   * @param psnId
   * @param incr @
   */
  public void decrTotalOutputs(Long psnId, Integer decr) {
    String hql = "update PsnPubStat set total = total - ?,lastUpdate=? where psnId = ?";
    super.createQuery(hql, decr, new Date(), psnId).executeUpdate();
  }

  /**
   * 增加待认领成果总数.
   * 
   * @param psnId
   * @param incr @
   */
  public void incrTotalConfirm(Long psnId, Integer incr) {
    String hql = "update PsnPubStat set totalConfirm = totalConfirm + ?,lastUpdate=? where psnId = ?";
    super.createQuery(hql, incr, new Date(), psnId).executeUpdate();
  }

  /**
   * 减少待认领成果总数.
   * 
   * @param psnId
   * @param incr @
   */
  public void decrTotalConfirm(Long psnId, Integer decr) {
    String hql = "update PsnPubStat set totalConfirm = totalConfirm - ?,lastUpdate=? where psnId = ?";
    super.createQuery(hql, decr, new Date(), psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PsnPubStat> getStatsByPsns(List<Long> psnIds) {
    String hql = "from PsnPubStat where psnId in (:psnId)";
    Query query = super.getSession().createQuery(hql).setParameterList("psnId", psnIds);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<PsnPubStat> getStatsByPsnsByIns(List<Long> psnIds, Long insId) {
    String hql =
        "select new PsnPubStat(psnId,count(id.pubId)) from PubIns where psnId in (:psnId) and id.insId = :insId and pubStatus>=0 group by psnId";
    Query query = super.getSession().createQuery(hql).setParameterList("psnId", psnIds).setLong("insId", insId);
    return query.list();
  }

  public PsnPubStat getStatsByPsnsByIns(Long psnId, Long insId) {
    String hql =
        "select new PsnPubStat(psnId,count(id.pubId)) from PubIns where psnId in (:psnId) and id.insId = :insId and pubStatus>=0 group by psnId";
    Query query = super.getSession().createQuery(hql).setParameter("psnId", psnId).setLong("insId", insId);
    return (PsnPubStat) query.uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PsnPubStat> getPsnPubStat(Long psnId) {
    return super.createQuery("from PsnPubStat where psnId=?", psnId).list();
  }
}
