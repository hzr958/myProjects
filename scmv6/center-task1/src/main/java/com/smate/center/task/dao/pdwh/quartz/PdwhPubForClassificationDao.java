package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhPubForClassification;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPubForClassificationDao extends PdwhHibernateDao<PdwhPubForClassification, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubForClassification> getIsiPubs(Integer size) {
    String hql = "from PdwhPubForClassification t where t.dbId in (15,16,17) and t.status = 0";
    List<PdwhPubForClassification> rsList = super.createQuery(hql).setMaxResults(size).list();
    return rsList;
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubForClassification> getCnkiPubs(Integer size) {
    String hql = "from PdwhPubForClassification t where t.dbId = 4 and t.status = 0";
    List<PdwhPubForClassification> rsList = super.createQuery(hql).setMaxResults(size).list();
    return rsList;
  }

}
