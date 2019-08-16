package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubKeyWords;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果关键词拆分表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubKeyWordsDao extends SnsHibernateDao<PubKeyWords, Long> {

  /**
   * 保存成果关键词.
   * 
   * @param pubId
   * @param psnId
   * @param keywords
   */
  public void savePubKeywords(Long pubId, Long psnId, List<String> keywords) {
    String hql = "delete from PubKeyWords t where t.pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
    if (CollectionUtils.isEmpty(keywords)) {
      return;
    }
    // 用于查重
    List<String> lkwList = new ArrayList<String>();
    for (String kw : keywords) {
      String lkw = kw.trim().toLowerCase();
      if (StringUtils.isBlank(lkw) || lkwList.contains(lkw)) {
        continue;
      }
      PubKeyWords pubKw = new PubKeyWords(pubId, psnId, kw, lkw);
      super.save(pubKw);
    }
  }

  /**
   * 删除成果关键词.
   * 
   * @param pubId
   */
  public void delPubKeywords(Long pubId) {
    String hql = "delete from PubKeyWords t where t.pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 获取成果关键词列表.
   * 
   * @param pubId
   * @return
   */
  public List<PubKeyWords> getPubKeyWords(Long pubId) {

    String hql = "from PubKeyWords t where t.pubId = ? ";
    return super.createQuery(hql, pubId).list();
  }
}
