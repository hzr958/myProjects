package com.smate.center.data.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.data.model.pub.ProjectDataFiveYear;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class ProjectDataFiveYearDao extends SnsHibernateDao<ProjectDataFiveYear, Long> {
  /**
   * new ProjectDataFiveYear(id,applicationCode,zhKeywords,enKeywords)
   * 
   * @param size
   * @param id
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ProjectDataFiveYear> getProjectDataList(Integer size, Long startId, Long endId) {
    String hql =
        "select new ProjectDataFiveYear(id,applicationCode,zhKeywords,enKeywords) from ProjectDataFiveYear t where t.id > :startId and t.id <= :endId order by t.id ";
    return createQuery(hql).setParameter("startId", startId).setParameter("endId", endId).setMaxResults(size).list();
  }

}
