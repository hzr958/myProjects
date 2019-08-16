package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PdwhPubKeywords;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果关键词
 * 
 * @author aijiangbin
 * @date 2018年4月24日
 */
@Repository
public class PdwhPubKeywordsDao extends PdwhHibernateDao<PdwhPubKeywords, Long> {

  /**
   * 得到未处理的成果关键词
   * 
   * @param size
   * @return
   */
  public List<PdwhPubKeywords> getNoDealPdwhPubKeywordsList(int size) {
    String hql = "from PdwhPubKeywords  t where  t.status=0  ";
    List list = this.createQuery(hql).setMaxResults(size).list();
    return list;
  }

  public Long getpubKeywords(Long pubId, int type) {
    String hql = "select distinct(t.pubId) from PdwhPubKeywords t where t.pubId = :pubId and t.language = :type";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).setParameter("type", type).uniqueResult();
  }

  public void deletePubKeywords(Long pubId, int type) {
    String hql = "delete PdwhPubKeywords t where t.pubId = :pubId and t.language = :type";
    super.createQuery(hql).setParameter("pubId", pubId).setParameter("type", type).executeUpdate();

  }

  public List<PdwhPubKeywords> listKeywordsByPdwhPubId(Long pdwhPubId, int language) {
    String hql = "from PdwhPubKeywords t where t.pubId=:pdwhPubId and t.language =:language";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("language", language).list();
  }

}
