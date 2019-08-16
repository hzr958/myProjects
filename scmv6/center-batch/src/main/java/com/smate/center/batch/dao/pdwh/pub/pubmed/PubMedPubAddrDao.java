package com.smate.center.batch.dao.pdwh.pub.pubmed;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.pubmed.PubMedPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果基准库地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubMedPubAddrDao extends PdwhHibernateDao<PubMedPubAddr, Long> {

  /**
   * 获取成果地址列表.
   * 
   * @param pubId
   * @return
   */
  public List<PubMedPubAddr> getPubMedPubAddr(Long pubId) {
    String hql = "from PubMedPubAddr where pubId = ? ";
    return super.find(hql, pubId);
  }
}
