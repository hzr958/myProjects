package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PatentCategpruNsfc;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PatentCategpruNsfcDao extends PdwhHibernateDao<PatentCategpruNsfc, Long> {

  public List<String> findCategoryByPubId(Long pubId) {
    String hql = "select distinct(t.nsfcCategory) from PatentCategpruNsfc t where t.approveCode=:pubId";
    return this.createQuery(hql).setParameter("pubId", pubId.toString()).list();
  }

}
