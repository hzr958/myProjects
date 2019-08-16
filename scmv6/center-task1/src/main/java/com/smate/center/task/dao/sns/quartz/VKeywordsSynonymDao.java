package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.VKeywordsSynonym;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class VKeywordsSynonymDao extends SnsHibernateDao<VKeywordsSynonym, Long> {

  /**
   * 批量获取keywords
   * 
   * @param page
   * @param batchSize
   * @return
   */
  public List<VKeywordsSynonym> GetVKeywordsSynonymList(int page, int batchSize) {

    String hql = "from VKeywordsSynonym ";

    return super.createQuery(hql).setMaxResults(batchSize).setFirstResult(batchSize * (page - 1)).list();

  }
}
