package com.smate.center.batch.dao.pdwh.pubimport;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.TmpPdwhPubAddrClean;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class TmpPdwhPubAddrCleanDao extends PdwhHibernateDao<TmpPdwhPubAddrClean, Long> {
  public Long getTmpPdwhPubAddrCleanCount(Long addrId) {
    String hql = "select count(1) from TmpPdwhPubAddrClean t where t.addrId =:addrId";
    return (Long) super.createQuery(hql).setParameter("addrId", addrId).uniqueResult();
  }

  public void deleteByAddrId(Long addrId) {
    String hql = "delete TmpPdwhPubAddrClean t where t.addrId =:addrId";
    super.createQuery(hql).setParameter("addrId", addrId).executeUpdate();
  }
}
