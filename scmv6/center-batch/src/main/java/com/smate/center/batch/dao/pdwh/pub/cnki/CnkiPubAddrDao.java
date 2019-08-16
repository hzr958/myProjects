package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果基准库地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPubAddrDao extends PdwhHibernateDao<CnkiPubAddr, Long> {

  /**
   * 获取成果地址列表.
   * 
   * @param pubId
   * @return
   */
  public List<CnkiPubAddr> getCnkiPubAddr(Long pubId) {
    String hql = "from CnkiPubAddr where pubId = ? ";
    return super.find(hql, pubId);
  }
}
