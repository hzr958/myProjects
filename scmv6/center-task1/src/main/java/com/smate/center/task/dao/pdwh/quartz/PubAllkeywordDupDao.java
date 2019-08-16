package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.pdwh.quartz.PubAllkeywordDup;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果关键词去重
 * 
 * @author warrior
 * 
 */
@Repository
public class PubAllkeywordDupDao extends PdwhHibernateDao<PubAllkeywordDup, Long> {

  /**
   * 查找关键词频数
   * 
   * @param keywordHash
   * @return
   * @throws DaoException
   */
  public PubAllkeywordDup findKeywordTf(Long keywordHash) throws DaoException {
    String hql = "from PubAllkeywordDup t where t.keywordHash = ?";
    List<PubAllkeywordDup> result = super.createQuery(hql, keywordHash).list();
    if (result != null && result.size() > 0) {
      return result.get(0);
    }
    return null;
  }

  /**
   * 更新关键词频数
   * 
   * @param keywordHash
   * @param tf
   * @throws DaoException
   */
  public void updateTf(Long keywordHash, Long tf) throws DaoException {
    String hql = "update PubAllkeywordDup t set t.count = ? where t.keywordHash = ?";
    super.createQuery(hql, tf, keywordHash).executeUpdate();
  }

  /**
   * 找出有用的关键词
   * 
   * @param keywordHashList
   * @return
   * @throws DaoException
   */
  public List<Long> findUsefulKeyword(List<Long> keywordHashList) throws DaoException {
    String hql = "select t.keywordHash from PubAllkeywordDup t where t.keywordHash in (:keywordHashList)";
    Query query = super.createQuery(hql);
    query.setParameterList("keywordHashList", keywordHashList);
    return query.list();
  }
}
