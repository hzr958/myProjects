package com.smate.web.prj.dao.project;

import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.PrjComment;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PrjCommentsDao extends SnsHibernateDao<PrjComment, Long> {

  public void updatePsnInf(Long personId, String avatars, String name) {
    String hql = "update PrjComment t set t.psnName = ? ,t.psnAvatars = ? where t.psnId = ? ";
    super.createQuery(hql, name, avatars, personId).executeUpdate();
  }

  /**
   * 获取项目的评论数
   * 
   * @param prjId
   * @return
   */
  public Long getCommentCount(Long prjId) {
    String hql = "select count(1) from PrjComment t where t.prjId =:prjId";
    return (Long) super.createQuery(hql).setParameter("prjId", prjId).uniqueResult();
  }
}
