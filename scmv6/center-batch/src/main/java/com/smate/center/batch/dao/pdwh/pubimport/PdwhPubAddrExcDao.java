package com.smate.center.batch.dao.pdwh.pubimport;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAddrExc;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPubAddrExcDao extends PdwhHibernateDao<PdwhPubAddrExc, Long> {

  /**
   * 获取某机构排除成果HASH列表.
   * 
   * @param insId
   * @return
   */
  public List<PdwhPubAddrExc> getExcAddrHash(Long insId) {

    String hql = "select new PdwhPubAddrExc(t.id, t.insId, t.addrHash) from PdwhPubAddrExc t where t.insId = ? ";
    return super.find(hql, insId);
  }

}
