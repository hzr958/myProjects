package com.smate.center.task.dao.publicpub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.publicpub.PubFulltextUploadLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubFulltextUploadLogDao extends SnsHibernateDao<PubFulltextUploadLog, Long> {

  @SuppressWarnings("unchecked")
  public List<PubFulltextUploadLog> getNeedRcmdData() {
    String hql = "from PubFulltextUploadLog t where t.status=0 order by t.id";
    return createQuery(hql).setMaxResults(1000).list();
  }

  public void updateStatus(Long id, int status) {
    String hql = "update PubFulltextUploadLog t set t.status= :status where t.id =:id";
    createQuery(hql).setParameter("status", status).setParameter("id", id).executeUpdate();
  }

  public PubFulltextUploadLog getFulltextByPdwhPubId(Long pdwhPubId) {
    String hql = "from PubFulltextUploadLog t where t.pdwhPubId = :pdwhPubId ";
    return (PubFulltextUploadLog) createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubFulltextUploadLog> getFulltextBySnsPubId(List<Long> snsPubIds) {
    String hql = "from PubFulltextUploadLog t where t.snsPubId in (:snsPubIds) ";
    return createQuery(hql).setParameterList("snsPubIds", snsPubIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPdwhToImageData(Integer size) {
    String hql =
        "select distinct(t.pdwhPubId) from PubFulltextUploadLog t where t.pdwhPubId is not null and t.pdwhPubToImage=0 order by t.pdwhPubId";
    return createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getSnsToImageData(Integer size) {
    String hql =
        "select distinct(t.snsPubId) from PubFulltextUploadLog t where t.snsPubId is not null and t.snsPubToImage=0 order by t.snsPubId";
    return createQuery(hql).setMaxResults(size).list();
  }

  public void updatePdwhToImageStatus(Long pdwhPubId, int status, String msg) {
    String hql =
        "update PubFulltextUploadLog t set t.pdwhPubToImage= :status ,t.errorMsg = :msg where t.pdwhPubId =:pdwhPubId";
    createQuery(hql).setParameter("status", status).setParameter("msg", msg).setParameter("pdwhPubId", pdwhPubId)
        .executeUpdate();
  }

  public void updateSnsToImageStatus(Long pubId, int status, String msg) {
    String hql =
        "update PubFulltextUploadLog t set t.snsPubToImage= :status ,t.errorMsg = :msg where t.snsPubId =:pubId";
    createQuery(hql).setParameter("status", status).setParameter("msg", msg).setParameter("pubId", pubId)
        .executeUpdate();
  }

}
