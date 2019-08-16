package com.smate.core.base.project.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.ProjectExpenAccessory;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目经费附件记录表
 * 
 * @author YJ
 *
 *         2019年8月6日
 */
@Repository
public class ProjectExpenAccessoryDao extends SnsHibernateDao<ProjectExpenAccessory, Long> {

  /**
   * 获取项目经费附件
   * 
   * @param expenId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ProjectExpenAccessory> listByExpenRecordId(Long expenRecordId) {
    String hql = "from ProjectExpenAccessory t where t.expenRecordId =:expenRecordId";
    return this.createQuery(hql).setParameter("expenRecordId", expenRecordId).list();
  }

  public void deleteAccessory(Long expenRecordId) {
    String hql = "delete ProjectExpenAccessory t where t.expenRecordId =:expenRecordId";
    this.createQuery(hql).setParameter("expenRecordId", expenRecordId).executeUpdate();
  }

}
