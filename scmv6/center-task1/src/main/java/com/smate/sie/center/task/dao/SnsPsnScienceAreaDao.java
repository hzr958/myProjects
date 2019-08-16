package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.sie.center.task.otherlibrary.model.SnsPsnScienceArea;

/**
 * 个人研究领域
 * 
 * @author zjh
 *
 */
@Repository
public class SnsPsnScienceAreaDao extends SnsHibernateDao<SnsPsnScienceArea, Long> {

  /**
   * 查找人员有效的科技领域ID
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findPsnScienceAreas(Long psnId) {
    String hql =
        "select t.scienceArea from SnsPsnScienceArea t where t.psnId = :psnId and t.status = 1 order by updateDate desc,t.id desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
