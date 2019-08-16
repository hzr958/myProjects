package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.OriginalPdwhPubRelation;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class OriginalPdwhPubRelationDao extends PdwhHibernateDao<OriginalPdwhPubRelation, Long> {

  @SuppressWarnings("unchecked")
  public List<OriginalPdwhPubRelation> getHandleData() {
    String hql = "from OriginalPdwhPubRelation t where t.status=0  order by  t.id";
    return super.createQuery(hql).setMaxResults(1000).list();
  }
}

