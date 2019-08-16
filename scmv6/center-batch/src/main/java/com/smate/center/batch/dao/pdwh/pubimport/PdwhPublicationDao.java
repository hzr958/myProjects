package com.smate.center.batch.dao.pdwh.pubimport;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPublicationDao extends PdwhHibernateDao<PdwhPublication, Long> {

  public void updatePdwhPub(String authorName, String authorNameSpec, Long pubId) {
    String hql =
        "update PdwhPublication t set t.authorName = :authorName ,t.authorNameSpec = :authorNameSpec where t.pubId = :pubId";
    super.createQuery(hql).setParameter("authorName", authorName).setParameter("authorNameSpec", authorNameSpec)
        .setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 更新成果更新时间为当前时间
   * 
   * @param pubId
   * @author LIJUN
   * @date 2018年6月20日
   */
  public void updatePubUpdateTime(Long pubId) {
    Date newDate = new Date();
    String hql = "update PdwhPublication t set t.updateDate =:newDate where t.pubId = :pubId";
    super.createQuery(hql).setParameter("pubId", pubId).setParameter("newDate", newDate).executeUpdate();
  }

  public PdwhPublication getPdwhPubById(Long pubId) {
    String hql =
        "select new PdwhPublication(t.zhTitle,t.enTitle,t.pubType,t.pubYear) from PdwhPublication t where t.pubId = :pubId";
    return (PdwhPublication) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }


  public List<Object> findListByIdBet(Integer size, Long startPubId, Long endPubId) {
    String sql =
        "select t.res_id from patent_classifi_log t where t.res_status=0 and t.res_id>=:startPubId and t.res_id<:endPubId ";
    return this.getSession().createSQLQuery(sql).setParameter("startPubId", startPubId)
        .setParameter("endPubId", endPubId).setMaxResults(size).list();
  }

  public void updatePatentBet(Long resId, Integer resStatus, String resMsg) {
    String sql = "update patent_classifi_log t set t.res_status=:resStatus,t.res_Msg=:resMsg  where t.res_id=:resId";
    this.getSession().createSQLQuery(sql).setParameter("resStatus", resStatus).setParameter("resMsg", resMsg)
        .setParameter("resId", resId).executeUpdate();
  }

  public List<Object> findPubListByIdBet(Integer size, Long startPubId, Long endPubId) {
    String sql =
        "select t.res_id from publication_classify_log t where t.res_status=0 and t.res_id>=:startPubId and t.res_id<:endPubId ";
    return this.getSession().createSQLQuery(sql).setParameter("startPubId", startPubId)
        .setParameter("endPubId", endPubId).setMaxResults(size).list();
  }

  public void updatePubRs(Long resId, Integer resStatus, String resMsg) {
    String sql =
        "update publication_classify_log t set t.res_status=:resStatus,t.res_Msg=:resMsg  where t.res_id=:resId";
    this.getSession().createSQLQuery(sql).setParameter("resStatus", resStatus).setParameter("resMsg", resMsg)
        .setParameter("resId", resId).executeUpdate();
  }
}
