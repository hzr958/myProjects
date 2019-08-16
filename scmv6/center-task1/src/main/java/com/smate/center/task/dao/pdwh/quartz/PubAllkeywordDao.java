package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.pdwh.quartz.PubAllkeyword;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 成果关键词Dao
 * 
 * @author warrior
 * 
 */
@Repository
public class PubAllkeywordDao extends PdwhHibernateDao<PubAllkeyword, Long> {

  /**
   * 查找推荐出来的文献的ID
   * 
   * @param keywordHashList
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubAllkeyword> findPubAllIds(List<Long> keywordHashList, Integer pubYear, Integer batchSize)
      throws DaoException {
    String hql =
        "select new PubAllkeyword(t.pubAllId,t.keyword) from PubAllkeyword t where t.keywordHash in (:keywordHashList) and t.pubYear>:pubYear";
    Query query = super.createQuery(hql);
    query.setParameterList("keywordHashList", keywordHashList);
    query.setParameter("pubYear", pubYear);
    query.setMaxResults(batchSize);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getPubAllKwsById(Long puballId) {
    String hql = "select lower(keyword) from PubAllkeyword where pubAllId=?";
    return super.createQuery(hql, puballId).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getMatchPsnKeyword(List<Long> psnKwHashList, Long puballId) {
    String hql =
        "select t.keyword from PubAllkeyword t where t.pubAllId=:puballId and t.keywordHash in (:psnKwHashList)";
    Query query = super.createQuery(hql);
    query.setParameter("puballId", puballId);
    query.setParameterList("psnKwHashList", psnKwHashList);
    return query.list();
  }

  public void deletePuballKeyrowds(Long puballId) {
    String hql = "delete from PubAllkeyword where pubAllId=?";
    super.createQuery(hql, puballId).executeUpdate();
  }

  public List<PubAllkeyword> findPubAllkeywordByPuballId(Long puballId) {
    String hql = "from PubAllkeyword where pubAllId=?";
    return super.find(hql, puballId);
  }

  public List<Map<String, Object>> findPsnRefRecommendKw(Long psnId, List<Long> kwHashList) {
    String hql =
        "select t1.keyword as keyword,count(*) as count from PubAllkeyword t1,PsnPubAllRecommend t2 where t1.pubAllId=t2.pubAllId and t2.psnId=:psnId and  t1.keywordHash in (:keywordHashList) group by t1.keyword order by count(*) desc";
    Query query = super.createQuery(hql);
    query.setParameter("psnId", psnId);
    query.setParameterList("keywordHashList", kwHashList);
    query.setMaxResults(5);
    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    return query.list();
  }

}
