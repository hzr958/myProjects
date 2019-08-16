package com.smate.center.task.dao.sns.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;
import com.smate.center.task.model.sns.quartz.ReadStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 阅读统计
 * 
 * @author Scy
 * 
 */
@Repository(value = "readStatisticsDao")
public class ReadStatisticsDao extends SnsHibernateDao<ReadStatistics, Long> {

  /**
   * 获取阅读统计记录列表.
   * 
   * @param psnId
   * @return
   */
  public List<ReadStatistics> getPubReadStaticList(Long pubId) {
    String hql = "from ReadStatistics where actionKey =:pubId and actionType = 1";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
