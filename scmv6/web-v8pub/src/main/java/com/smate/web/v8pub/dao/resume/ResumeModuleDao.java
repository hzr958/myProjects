package com.smate.web.v8pub.dao.resume;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.vo.sns.newresume.ResumeModule;
import org.springframework.stereotype.Repository;

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

}
