package com.smate.web.psn.dao.prj;

import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.PrjComment;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PrjCommentsDao extends SnsHibernateDao<PrjComment, Long> {

  public void updatePsnInf(Long personId, String avatars, String name) {
    String hql = "update PrjComment t set t.psnName = ? ,t.psnAvatars = ? where t.psnId = ? ";
    super.createQuery(hql, name, avatars, personId).executeUpdate();
  }

}
