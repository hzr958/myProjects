package com.smate.center.batch.dao.pdwh.pub.cnki;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubKeywords;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class CnkiPubKeywordsDao extends PdwhHibernateDao<CnkiPubKeywords, Long> {

  /**
   * 获取CNKI关键词.
   * 
   * @param pubId
   * @param type
   * @return
   */
  public CnkiPubKeywords getCnkiPubKeywords(Long pubId, Long type) {

    String hql = "from CnkiPubKeywords t where t.pubId = ? and t.type = ? ";
    return super.findUnique(hql, pubId, type);
  }
}
