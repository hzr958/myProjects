package com.smate.center.batch.dao.pdwh.pub.isi;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubKeywords;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class IsiPubKeywordsDao extends PdwhHibernateDao<IsiPubKeywords, Long> {

  /**
   * 获取isi关键词.
   * 
   * @param pubId
   * @param type
   * @return
   */
  public IsiPubKeywords getIsiPubKeywords(Long pubId, Long type) {

    String hql = "from IsiPubKeywords t where t.pubId = ? and t.type = ? ";
    return super.findUnique(hql, pubId, type);
  }
}
