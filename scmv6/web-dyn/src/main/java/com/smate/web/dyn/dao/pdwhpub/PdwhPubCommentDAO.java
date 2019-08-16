package com.smate.web.dyn.dao.pdwhpub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.pdwhpub.PdwhPubCommentPO;

/**
 * 成果评论dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubCommentDAO extends PdwhHibernateDao<PdwhPubCommentPO, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubCommentPO> queryComments(Long pdwhPubId, DynamicForm form) {
    String hql = "from PdwhPubCommentPO p where p.pdwhPubId=:pdwhPubId order by p.gmtCreate desc";
    List<PdwhPubCommentPO> pdwhPubComments = super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    form.setTotalCount(pdwhPubComments.size());
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId)
        .setFirstResult((form.getPageNumber() - 1) * form.getPageSize()).setMaxResults(form.getPageSize()).list();

  }

}
