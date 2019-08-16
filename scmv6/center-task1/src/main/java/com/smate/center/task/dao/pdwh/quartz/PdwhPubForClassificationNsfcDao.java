package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhPubForClassificationNsfc;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPubForClassificationNsfcDao extends PdwhHibernateDao<PdwhPubForClassificationNsfc, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubForClassificationNsfc> getIsiPubs(Integer size) {
    String hql = "from PdwhPubForClassificationNsfc t where t.dbId in (15,16,17) and t.status = 0";
    List<PdwhPubForClassificationNsfc> rsList = super.createQuery(hql).setMaxResults(size).list();
    return rsList;
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubForClassificationNsfc> getCnkiPubs(Integer size) {
    String hql = "from PdwhPubForClassificationNsfc t where t.dbId = 4 and t.status = 0";
    List<PdwhPubForClassificationNsfc> rsList = super.createQuery(hql).setMaxResults(size).list();
    return rsList;
  }

}
