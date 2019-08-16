package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.TmpPublicationForSnsGroup;
import com.smate.core.base.utils.data.PdwhHibernateDao;


@Repository
public class TmpPublicationForSnsGroupDao extends PdwhHibernateDao<TmpPublicationForSnsGroup, Long> {

  @SuppressWarnings("unchecked")
  public List<TmpPublicationForSnsGroup> getToHandleList(Integer size, Long startPubId, Long endPubId) {
    String hql =
        "from TmpPublicationForSnsGroup t where t.pubAllId >:startPubId and t.pubAllId <=:endPubId and t.status = 0 order by t.pubAllId asc";
    return super.createQuery(hql).setParameter("startPubId", startPubId).setParameter("endPubId", endPubId)
        .setMaxResults(size).list();
  }

}
