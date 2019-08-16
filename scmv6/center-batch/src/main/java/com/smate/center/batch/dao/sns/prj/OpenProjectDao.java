package com.smate.center.batch.dao.sns.prj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.prj.OpenProject;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 第三方项目DAO
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @return
 */
@Repository
public class OpenProjectDao extends SnsHibernateDao<OpenProject, Long> {

  /**
   * 根据数量查询待合并的第三方项目
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<OpenProject> queryBySize(Integer size) {
    String hql = "from OpenProject where taskStatus=0 order by id asc ";
    List<OpenProject> list = super.createQuery(hql).setMaxResults(size).list();
    return list;
  }

  public OpenProject queryById(Long objId) {
    String hql = "from OpenProject where taskStatus=0 and id=? ";
    OpenProject obj = (OpenProject) super.createQuery(hql, objId).uniqueResult();
    return obj;
  }

  public List<OpenProject> queryByObjId(String objId) {
    String hql = "from OpenProject where taskStatus=2 and objId=? ";
    List<OpenProject> list = super.createQuery(hql, objId).list();
    return list;
  }

}
