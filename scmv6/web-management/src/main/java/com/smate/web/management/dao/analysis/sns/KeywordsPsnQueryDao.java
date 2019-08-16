package com.smate.web.management.dao.analysis.sns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.sns.KeywordsPsnQuery;

@Repository
public class KeywordsPsnQueryDao extends SnsHibernateDao<KeywordsPsnQuery, Long> {

  /**
   * 通过关键词查询id查询相关人员.
   * 
   * @return
   */
  public Long getQueryId() {
    String hql = "select SEQ_KEYWORDS_PSN_QUERY_ID.NEXTVAL from dual";
    return super.queryForLong(hql);
  }

  /**
   * 通过关键词查询id查询相关人员.
   * 
   * @param qid
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryKeywordsPsn(Long qid, int size) {

    List<Long> psnIds = null;
    List<Long> gidList = queryKeywordPsnHotGid(qid);
    if (CollectionUtils.isEmpty(gidList))
      return psnIds;

    String hql =
        "select t.psnId from PsnKwEpt t,KeywordsPsnQuery q where t.keywordGid in (:gid) and q.qid = :qid group by t.psnId order by sum(t.score * q.weight) desc";

    psnIds =
        super.createQuery(hql).setParameterList("gid", gidList).setParameter("qid", qid).setMaxResults(size).list();

    return psnIds;
  }

  @SuppressWarnings("unchecked")
  public List<Long> queryKeywordPsnHotGid(Long qid) {

    String hql = "select t.keywordGid from PsnKwEpt t ,KeywordsPsnQuery q where  t.kwTxt = q.keywordTxt and q.qid = ?";

    return super.createQuery(hql, qid).list();
  }

  /**
   * 通过关键词查询id查询相关人员及关键词相同总数.
   * 
   * @param qid
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Long, Long> queryKeyPsnAndKeyCount(Long qid, int size) {

    Map<Long, Long> returnMap = null;
    List<Long> gidList = queryKeywordPsnHotGid(qid);
    if (CollectionUtils.isEmpty(gidList))
      return returnMap;

    String hql =
        "select t.psnId as psn_id ,count(t.psnId) as key_count from PsnKwEpt t where t.keywordGid in (:gid) group by t.psnId order by count(t.psnId) desc";

    List<Map<String, Long>> psnIdList = super.createQuery(hql).setParameterList("gid", gidList)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setMaxResults(size).list();

    returnMap = new HashMap<Long, Long>();
    if (CollectionUtils.isNotEmpty(psnIdList)) {
      for (Map<String, Long> map : psnIdList) {

        Long psnId = map.get("psn_id").longValue();
        Long keyCount = map.get("key_count").longValue();
        returnMap.put(psnId, keyCount);
      }
    }

    return returnMap;
  }

}
