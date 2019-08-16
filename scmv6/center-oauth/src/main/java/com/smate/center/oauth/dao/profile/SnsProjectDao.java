package com.smate.center.oauth.dao.profile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目DAO.
 * 
 * @author wsn
 * 
 */
@Repository
public class SnsProjectDao extends SnsHibernateDao<Project, Long> {

  /**
   * 获取项目总数.
   * 
   * @param psnId
   * @return
   */
  public Integer getSumProject(Long psnId) {
    String hql = "select count(t.id) from Project t where t.psnId = ? and t.status = 0 ";
    Long count = super.findUnique(hql, psnId);
    return count.intValue();
  }

  @SuppressWarnings("unchecked")
  public List<Project> findPrjIdsByPsnId(Long psnId) {
    String hql = "from Project t where psnId=? and t.status = 0 ";
    return super.createQuery(hql, psnId).list();
  }
}
