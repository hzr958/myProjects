package com.smate.center.task.dao.snsbak;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.PubPdwhAddrInfoInit2;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class PubPdwhAddrInfoInit2Dao extends SnsbakHibernateDao<PubPdwhAddrInfoInit2, Long> {

  @SuppressWarnings("unchecked")
  public List<PubPdwhAddrInfoInit2> getProvInteritorToHandleList(Integer size) {
    String hql = "from PubPdwhAddrInfoInit2 t where t.status = 0";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhAddrInfoInit2> getProvInternationalToHandleList(Integer size) {
    String hql = "from PubPdwhAddrInfoInit2 t where t.statusInternational = 0";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhAddrInfoInit2> getCityToHandleList(Integer size) {
    String hql = "from PubPdwhAddrInfoInit2 t where t.statusCity = 0";
    return super.createQuery(hql).setMaxResults(size).list();
  }
}
