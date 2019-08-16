package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubKeywordsSplit;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 成果关键词拆分.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPubKeywordsSplitDao extends PdwhHibernateDao<CnkiPubKeywordsSplit, Long> {

  /**
   * 根据成果ID获取拆分的CNKI成果关键词列表.
   * 
   * @param pubId
   * @return
   */
  public List<CnkiPubKeywordsSplit> getCnkiPubKwList(Long pubId) {
    String hql = "from CnkiPubKeywordsSplit t where t.pubId=? ";
    return super.find(hql, pubId);
  }
}
