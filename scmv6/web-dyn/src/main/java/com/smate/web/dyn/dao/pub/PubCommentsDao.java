package com.smate.web.dyn.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.pub.PubComment;

/**
 * 成果评论持久层.
 * 
 * @author LY
 * 
 */
@Repository
public class PubCommentsDao extends SnsHibernateDao<PubComment, Long> {
  /**
   * 获取成果评论列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubComment> getPubReply(Long pubId, DynamicForm form) {
    String hql = " from PubComment p where p.pubId=:pubId order by p.createDate desc";
    List<PubComment> pubComments = super.createQuery(hql).setParameter("pubId", pubId).list();
    form.setTotalCount(pubComments.size());
    return super.createQuery(hql).setParameter("pubId", pubId)
        .setFirstResult((form.getPageNumber() - 1) * form.getPageNumber()).setMaxResults(form.getPageSize()).list();

  }

}
