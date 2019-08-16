package com.smate.center.batch.dao.pdwh.prj;

import com.smate.center.batch.model.pdwh.prj.NsfcPrjPubRelation;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NsfcPrjPubRelationDao extends PdwhHibernateDao<NsfcPrjPubRelation, Long> {
  public List<Long> getPubIdList(Long prjId) {
    String hql = "select t.scmPubId from NsfcPrjPubRelation t where t.prjId =:prjId and t.scmPubId is not null";
    return (List<Long>) super.createQuery(hql).setParameter("prjId", prjId).list();
  }
}
