package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubKeywordsPO;

/**
 * 成果 关键词dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubKeywordsDAO extends SnsHibernateDao<PubKeywordsPO, Long> {

  @SuppressWarnings("unchecked")
  public List<PubKeywordsPO> getByPubId(Long pubId) {
    String hql = "from PubKeywordsPO p where p.pubId=:pubId ";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

  /**
   * 删除成果关键词.
   * 
   * @param pubId
   */
  public void delPubKeywords(Long pubId) {
    String hql = "delete from PubKeywordsPO t where t.pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 保存成果关键词.
   * 
   * @param pubId
   * @param keywords
   */
  public void savePubKeywords(Long pubId, List<String> keywords) {
    String hql = "delete from PubKeywordsPO t where t.pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
    if (CollectionUtils.isEmpty(keywords)) {
      return;
    }
    for (String kw : keywords) {
      PubKeywordsPO pubKw = new PubKeywordsPO(pubId, kw);
      super.save(pubKw);
    }
  }

}
