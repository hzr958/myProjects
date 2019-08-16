package com.smate.center.task.dao.snsbak;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.PubPdwhAddrInfoInit;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class PubPdwhAddrInfoInitDao extends SnsbakHibernateDao<PubPdwhAddrInfoInit, Long> {

  @SuppressWarnings("unchecked")
  public List<PubPdwhAddrInfoInit> getProvInteritorToHandleList(Integer size) {
    String hql = "from PubPdwhAddrInfoInit t where t.status = 0";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhAddrInfoInit> getProvInternationalToHandleList(Integer size) {
    String hql = "from PubPdwhAddrInfoInit t where t.statusInternational = 0";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhAddrInfoInit> getCityToHandleList(Integer size) {
    String hql = "from PubPdwhAddrInfoInit t where t.statusCity = 0";
    return super.createQuery(hql).setMaxResults(size).list();
  }
}
