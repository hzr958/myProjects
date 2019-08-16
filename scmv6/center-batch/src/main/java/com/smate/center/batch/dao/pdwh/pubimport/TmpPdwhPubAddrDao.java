package com.smate.center.batch.dao.pdwh.pubimport;

import com.smate.center.batch.model.pdwh.pubimport.TmpPdwhPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class TmpPdwhPubAddrDao extends PdwhHibernateDao<TmpPdwhPubAddr, Long> {

  public List<TmpPdwhPubAddr> getToHandleList(Long minPubId, Long maxPubId) {
    String hql = "from TmpPdwhPubAddr t where t.status = 9 and t.addrId >=:minPubId and t.addrId <:maxPubId";
    return super.createQuery(hql).setParameter("minPubId", minPubId).setParameter("maxPubId", maxPubId)
        .setMaxResults(3000).list();
  }

  public List<BigDecimal> getPrjIdList(Integer size) {
    String sql = "select t.prj_id from nsfc_project_kw_tohandle t where t.status = 0";
    return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setMaxResults(size).list();
  }

  public void updatePrjStatus(Long prjId, Integer status) {
    String sql = "update nsfc_project_kw_tohandle t set t.status =:status where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("prjId", prjId).executeUpdate();
  }

}
