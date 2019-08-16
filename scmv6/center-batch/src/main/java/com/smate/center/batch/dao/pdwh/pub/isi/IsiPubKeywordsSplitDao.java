package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubKeywordsSplit;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果关键词拆分.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class IsiPubKeywordsSplitDao extends PdwhHibernateDao<IsiPubKeywordsSplit, Long> {

  /**
   * 根据成果ID获取拆分的ISI成果关键词列表.
   * 
   * @param pubId
   * @return
   */
  public List<IsiPubKeywordsSplit> getIsiPubKwList(Long pubId) {
    String hql = "from IsiPubKeywordsSplit t where t.pubId=? ";
    return super.find(hql, pubId);
  }
}
