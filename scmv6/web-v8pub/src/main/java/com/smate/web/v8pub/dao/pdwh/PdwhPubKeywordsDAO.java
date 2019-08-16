package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubKeywordsPO;

/**
 * 成果 关键词dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubKeywordsDAO extends PdwhHibernateDao<PdwhPubKeywordsPO, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubKeywordsPO> getByPubId(Long pdwhPubId) {
    String hql = "from PdwhPubKeywordsPO p where p.pdwhPubId=:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) ";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  /**
   * 保存成果关键词
   * 
   * @param pubId
   * @param kwList
   */

  public void savePubKeywords(Long pdwhPubId, List<String> kwList) {
    String hql = "delete from PdwhPubKeywordsPO t where t.pdwhPubId = ? ";
    super.createQuery(hql, pdwhPubId).executeUpdate();
    if (CollectionUtils.isEmpty(kwList)) {
      return;
    }
    for (String kw : kwList) {
      PdwhPubKeywordsPO pubKw = new PdwhPubKeywordsPO();
      pubKw.setPdwhPubId(pdwhPubId);
      pubKw.setPubKeyword(kw);
      super.save(pubKw);
    }
  }

}
