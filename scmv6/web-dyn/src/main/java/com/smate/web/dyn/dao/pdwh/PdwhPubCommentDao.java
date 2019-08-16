package com.smate.web.dyn.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.pdwhpub.PdwhPubComment;

/**
 * 成果评论
 * 
 * @author zx
 *
 */
@Repository
public class PdwhPubCommentDao extends PdwhHibernateDao<PdwhPubComment, Long> {

  public List<PdwhPubComment> queryComments(Long pubId, DynamicForm form) {
    String hql = "from PdwhPubComment t where t.pubId=:pubId  order by t.createDate desc";
    List<PdwhPubComment> pdwhPubComments = super.createQuery(hql).setParameter("pubId", pubId).list();
    form.setTotalCount(pdwhPubComments.size());
    return super.createQuery(hql).setParameter("pubId", pubId)
        .setFirstResult((form.getPageNumber() - 1) * form.getPageSize()).setMaxResults(form.getPageSize()).list();

  }

}
