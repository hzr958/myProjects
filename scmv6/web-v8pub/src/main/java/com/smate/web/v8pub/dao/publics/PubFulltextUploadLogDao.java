package com.smate.web.v8pub.dao.publics;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.PubFulltextUploadLog;

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

  public PubFulltextUploadLog getUploadLog(Long pdwhPubId) {
    String hql = "from PubFulltextUploadLog t where t.pdwhPubId = :pdwhPubId";
    return (PubFulltextUploadLog) createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public PubFulltextUploadLog getSnsUploadLog(Long snsPubId) {
    String hql = "from PubFulltextUploadLog t where t.snsPubId = :snsPubId order by t.fulltextFileId desc";
    List<PubFulltextUploadLog> list = createQuery(hql).setParameter("snsPubId", snsPubId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
