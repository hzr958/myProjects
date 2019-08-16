package com.smate.center.batch.dao.pdwh.pub.cnipr;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果基准库地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CniprPubAddrDao extends PdwhHibernateDao<CniprPubAddr, Long> {

  /**
   * 获取成果地址列表.
   * 
   * @param pubId
   * @return
   */
  public List<CniprPubAddr> getCniprPubAddr(Long pubId) {
    String hql = "from CniprPubAddr where pubId = ? ";
    return super.find(hql, pubId);
  }
}
