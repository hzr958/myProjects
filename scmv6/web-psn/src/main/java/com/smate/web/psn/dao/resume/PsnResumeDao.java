package com.smate.web.psn.dao.resume;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.newresume.PsnResume;

/**
 * 新的简历主表DAO
 * 
 * @author wsn
 *
 */
@Repository
public class PsnResumeDao extends SnsHibernateDao<PsnResume, Long> {

  /**
   * 根据简历ID和人员ID查找简历记录
   * 
   * @param psnId
   * @param resumeId
   * @return
   */
  public PsnResume findPsnResumeByPsnIdAndResumeId(Long psnId, Long resumeId) {
    String hql = "from PsnResume t where t.resumeId = :resumeId and t.ownerPsnId = :psnId";
    return (PsnResume) super.createQuery(hql).setParameter("resumeId", resumeId).setParameter("psnId", psnId)
        .uniqueResult();
  }

  /**
   * 获取个人简历列表
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnResume> queryPsnResumeList(Long psnId) {
    String hql = "from PsnResume t where t.ownerPsnId = :psnId order by t.updateDate desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(10).list();
  }

  /**
   * 更新简历的更新时间
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public void updatePsnResumeUpdateTime(Long cvId) {
    String hql = "update PsnResume t set t.updateDate = :updateDate where t.resumeId = :cvId";
    super.createQuery(hql).setParameter("cvId", cvId).setParameter("updateDate", new Date()).executeUpdate();
  }

  /**
   * 查找简历名称
   * 
   * @param cvId
   * @return
   */
  public String findCVName(Long cvId) {
    String hql = "select t.resumeName from PsnResume t where t.resumeId = :cvId";
    return (String) super.createQuery(hql).setParameter("cvId", cvId).uniqueResult();
  }
}
