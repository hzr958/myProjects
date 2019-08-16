package com.smate.web.psn.dao.project;



import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class ProjectStatisticsDao extends SnsHibernateDao<ProjectStatistics, Long> {
  /**
   * 统计人员项目分享数
   * 
   * @param psnId
   * @return
   */
  public Long countPsnPrjShareSum(Long psnId) {
    String hql =
        "select nvl(sum(t.shareCount),0) from ProjectStatistics t where  exists(select 1 from Project t1 where t1.id=t.projectId and t1.psnId=:psnId and t1.status=0 )";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }
}
