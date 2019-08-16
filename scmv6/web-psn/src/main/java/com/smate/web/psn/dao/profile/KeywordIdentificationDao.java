package com.smate.web.psn.dao.profile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.profile.KeywordIdentification;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author zyx
 * 
 */
@Repository
public class KeywordIdentificationDao extends SnsHibernateDao<KeywordIdentification, Long> implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5720335155047151985L;

  /**
   * 关键词认同统计数
   * 
   * @param psnId
   * @param kwIdList
   * @return
   */
  public List<Object[]> countIdentification(Long psnId, List<Long> kwIdList) {

    String hql =
        "SELECT t.keywordId,count(t.friendId) FROM KeywordIdentification t WHERE  t.keywordId in (:kwIdList) and t.psnId=:psnId GROUP BY t.keywordId order by count(t.friendId) desc";
    return super.createQuery(hql).setParameterList("kwIdList", kwIdList).setParameter("psnId", psnId).list();
  }

  /**
   * 关键词认同统计数
   * 
   * @param psnId
   * @param kwIdList
   * @return
   */
  public Long countOneIdentification(Long psnId, Long keywordId) {
    String hql =
        "SELECT count(t.friendId) FROM KeywordIdentification t WHERE  t.keywordId=:keywordId and t.psnId=:psnId";
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("keywordId", keywordId);
    params.put("psnId", psnId);
    return (Long) super.createQuery(hql, params).uniqueResult();
  }

  public List<Long> findFriendId(Long psnId, Long keywordId) {
    String hql =
        "select t.friendId from KeywordIdentification t where t.psnId=? and t.keywordId=? order by  t.opDate desc";
    return super.find(hql, psnId, keywordId);
  }

  // 查找认同记录
  public KeywordIdentification findKeywordIdentification(Long psnId, Long keywordId, Long friendId) {
    String hql = "from KeywordIdentification t where t.psnId=? and t.friendId = ? and t.keywordId=?";
    return super.findUnique(hql, psnId, friendId, keywordId);
  }
}
