package com.smate.web.dyn.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.PrjComment;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.pub.PubComment;

@Repository
public class PrjCommentsDao extends SnsHibernateDao<PrjComment, Long> {

  public void updatePsnInf(Long personId, String avatars, String name) {
    String hql = "update PrjComment t set t.psnName = ? ,t.psnAvatars = ? where t.psnId = ? ";
    super.createQuery(hql, name, avatars, personId).executeUpdate();
  }

  public List<PrjComment> getPrjReply(Long prjId, DynamicForm form) {
    String hql = " from PrjComment p where p.prjId=:prjId order by p.createDate desc";
    List<PubComment> pubComments = super.createQuery(hql).setParameter("prjId", prjId).list();
    form.setTotalCount(pubComments.size());
    return super.createQuery(hql).setParameter("prjId", prjId)
        .setFirstResult((form.getPageNumber() - 1) * form.getPageNumber()).setMaxResults(form.getPageSize()).list();

  }

}
