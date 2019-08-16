package com.smate.center.batch.dao.pdwh.pubimport;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.TmpPublicationAll;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class TmpPublicationAllDao extends PdwhHibernateDao<TmpPublicationAll, Long> {

  @SuppressWarnings("unchecked")
  public List<TmpPublicationAll> getToHandleList(Integer size) {

    String hql = "from TmpPublicationAll t where t.status = 0 order by t.id";
    return super.createQuery(hql).setMaxResults(size).list();
  }
}
