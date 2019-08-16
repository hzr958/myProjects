package com.smate.sie.center.task.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.PubStat;

/**
 * 成果统计dao
 * 
 * @author ztg
 *
 */
@Repository
public class SiePubStatDao extends SieHibernateDao<PubStat, Long> {

  /**
   * 获取成果统计数
   * 
   * @param pubId
   * @return
   */
  public PubStat getByUnitId(Long pubId) {
    String hql = "from PubStat u where u.pubId = :pubId";
    return (PubStat) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubStat> getsByUnitId(List<Long> pubIds) {
    if (CollectionUtils.isEmpty(pubIds))
      return null;

    List<PubStat> publications =
        super.createQuery("from PubStat t where t.pubId in(:pubId)").setParameterList("pubId", pubIds).list();

    return publications;
  }

  /**
   * 获取指定统计类型的统计数.
   * 
   * @param patId
   * @param fieldName
   * @return
   */
  public Long getPubStatistic(Long pubId, String fieldName) {
    StringBuilder hql = new StringBuilder();
    hql.append("select t.");
    hql.append(fieldName);
    hql.append(" from PubStat t where t.pubId=?");
    return findUnique(hql.toString(), pubId);
  }
}
