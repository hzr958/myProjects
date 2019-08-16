package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchKeyword;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * ISI成果匹配表的关键词表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class IsiPubMatchKeywordDao extends PdwhHibernateDao<IsiPubMatchKeyword, Long> {

  /**
   * 根据成果ID获取期刊的关键词和关键词的hash值列表.
   * 
   * @param pubId
   * @return
   */
  public List<IsiPubMatchKeyword> getIsiPubMatchKeywordByPubId(Long pubId) {
    String hql = "select new IsiPubMatchKeyword(pubId,kwHash) from IsiPubMatchKeyword t where t.pubId=? ";
    return super.find(hql, pubId);
  }

  /**
   * 获取成果关键词记录.
   * 
   * @param pubId
   * @param kwHash
   * @return
   */
  public IsiPubMatchKeyword getIsiPubMatchKeyword(Long pubId, Long kwHash) {
    String hql = "from IsiPubMatchKeyword t where t.pubId=? and t.kwHash=? ";
    List<IsiPubMatchKeyword> keywordList = super.find(hql, pubId, kwHash);
    if (CollectionUtils.isNotEmpty(keywordList)) {
      return keywordList.get(0);
    }
    return null;
  }

  /**
   * 保存成果关键词记录.
   * 
   * @param keyword
   */
  public void savePubMatchKey(IsiPubMatchKeyword keyword) {
    IsiPubMatchKeyword kw = this.getIsiPubMatchKeyword(keyword.getPubId(), keyword.getKwHash());
    if (kw == null) {
      super.save(keyword);
    }
  }
}
