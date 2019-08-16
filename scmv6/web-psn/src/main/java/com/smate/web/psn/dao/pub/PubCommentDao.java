package com.smate.web.psn.dao.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.pub.PubComment;

@Repository
public class PubCommentDao extends SnsHibernateDao<PubComment, Long> {
  /**
   * 更新评论人信息.
   * 
   * @param psnId
   * @param avators
   * @param name
   */
  public void updatePsnInf(Long personId, String avatars, String name) {
    String hql = "update PubComment t set t.psnName = ? ,t.psnAvatars = ? where t.psnId = ? ";
    super.createQuery(hql, name, avatars, personId).executeUpdate();
  }

}
