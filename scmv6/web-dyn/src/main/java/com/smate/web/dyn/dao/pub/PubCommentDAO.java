package com.smate.web.dyn.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.pub.PubCommentPO;

/**
 * 成果评论dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubCommentDAO extends SnsHibernateDao<PubCommentPO, Long> {

  /**
   * 获取成果评论列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubCommentPO> getPubReply(Long pubId, DynamicForm form) {
    String hql = " from PubCommentPO p where p.pubId=:pubId order by p.gmtCreate desc";
    List<PubCommentPO> pubComments = super.createQuery(hql).setParameter("pubId", pubId).list();
    form.setTotalCount(pubComments.size());
    return super.createQuery(hql).setParameter("pubId", pubId)
        .setFirstResult((form.getPageNumber() - 1) * form.getPageNumber()).setMaxResults(form.getPageSize()).list();

  }
}
