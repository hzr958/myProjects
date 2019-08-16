package com.smate.center.batch.dao.pdwh.pub.ei;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.ei.EiPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果基准库地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class EiPubAddrDao extends PdwhHibernateDao<EiPubAddr, Long> {

  /**
   * 获取成果地址列表.
   * 
   * @param pubId
   * @return
   */
  public List<EiPubAddr> getEiPubAddr(Long pubId) {
    String hql = "from EiPubAddr where pubId = ? ";
    return super.find(hql, pubId);
  }
}
