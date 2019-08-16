package com.smate.web.psn.dao.resume;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.newresume.ResumeModule;

/**
 * 简历模块信息DAO
 * 
 * @author wsn
 *
 */
@Repository
public class ResumeModuleDao extends SnsHibernateDao<ResumeModule, Long> {

  /**
   * 删除对应的简历ID下面所有模块记录
   * 
   * @param resumeId
   */
  public void deleteResumeModuleByResumeId(Long resumeId) {
    String hql = "delete from ResumeModule t where t.resumeId = :resumeId";
    super.createQuery(hql).setParameter("resumeId", resumeId).executeUpdate();
  }

  public ResumeModule findResumeModuleByResumeIdAndModuleId(Long resumeId, Integer moduleId) {
    String hql = "from ResumeModule t where t.resumeId = :resumeId and t.moduleId = :moduleId";
    return (ResumeModule) super.createQuery(hql).setParameter("resumeId", resumeId).setParameter("moduleId", moduleId)
        .uniqueResult();
  }

  /**
   * 查找简历模块信息主键
   * 
   * @param resumeId
   * @param moduleId
   * @return
   */
  public Long findCVModuleInfoId(Long resumeId, Integer moduleId) {
    String hql = "select t.moduleInfoId from ResumeModule t where t.resumeId = :resumeId and t.moduleId = :moduleId";
    return (Long) super.createQuery(hql).setParameter("resumeId", resumeId).setParameter("moduleId", moduleId)
        .uniqueResult();
  }

  /**
   * 查找简历所有模块信息ID
   * 
   * @param cvId
   * @param psnId
   * @return
   */
  public List<Long> findModuleInfoIds(Long cvId) {
    String hql = "select t.moduleInfoId from ResumeModule t where t.resumeId = :cvId ";
    return super.createQuery(hql).setParameter("cvId", cvId).list();
  }

  /**
   * 查找简历所有模块的状态
   * 
   * @param cvId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ResumeModule> findCVModuleStatus(Long cvId) {
    String hql = "select new ResumeModule(resumeId, moduleId, status) from ResumeModule t where t.resumeId = :cvId ";
    return super.createQuery(hql).setParameter("cvId", cvId).list();
  }
}
