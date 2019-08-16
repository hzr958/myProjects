package com.smate.center.batch.dao.pdwh.pub.cnkipat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * CNKI专利成果基准库地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPatPubAddrDao extends PdwhHibernateDao<CnkiPatPubAddr, Long> {

  /**
   * 获取成果地址列表.
   * 
   * @param pubId
   * @return
   */
  public List<CnkiPatPubAddr> getCnkiPatPubAddr(Long pubId) {
    String hql = "from CnkiPatPubAddr where pubId = ? ";
    return super.find(hql, pubId);
  }
}
