package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchKeyword;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * CNKI成果匹配表的关键词表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class CnkiPubMatchKeywordDao extends PdwhHibernateDao<CnkiPubMatchKeyword, Long> {

  /**
   * 根据成果ID获取期刊的关键词和关键词的hash值列表.
   * 
   * @param pubId
   * @return
   */
  public List<CnkiPubMatchKeyword> getMatchedKeywordByPubId(Long pubId) {
    String hql = "select new CnkiPubMatchKeyword(pubId,kwHash) from CnkiPubMatchKeyword t where t.pubId=? ";
    return super.find(hql, pubId);
  }

  /**
   * 获取成果关键词记录.
   * 
   * @param pubId
   * @param kwHash
   * @return
   */
  public CnkiPubMatchKeyword getCnkiPubMatchKeyword(Long pubId, Long kwHash) {
    String hql = "from CnkiPubMatchKeyword t where t.pubId=? and t.kwHash=? ";
    List<CnkiPubMatchKeyword> keywordList = super.find(hql, pubId, kwHash);
    if (CollectionUtils.isNotEmpty(keywordList)) {
      return keywordList.get(0);
    }
    return null;
  }
}
