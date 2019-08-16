package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.smate.center.task.model.pdwh.pub.PdwhPubComment;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 成果评论
 * 
 * @author zx
 *
 */
@Repository
public class PdwhPubCommentDao extends PdwhHibernateDao<PdwhPubComment, Long> {

  public List<PdwhPubComment> queryComments(Long pubId, Page page, Integer pageSize) {
    String hql = "from PdwhPubComment t where t.pubId=:pubId  order by t.createDate desc";
    String countHql = "select count(*) from PdwhPubComment t where t.pubId=:pubId";
    Long totalCount = (Long) super.createQuery(countHql).setParameter("pubId", pubId).uniqueResult();
    page.setTotalCount(totalCount);
    return super.createQuery(hql).setParameter("pubId", pubId).setFirstResult((page.getPageNo() - 1) * pageSize)
        .setMaxResults(pageSize).list();

  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubComment> listByPdwhPubId(Long pdwhPubId) {
    String hql = "from PdwhPubComment t where t.pubId=:pdwhPubId";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  public Long getCommentCount(Long pdwhPubId) {
    String hql = "select count(t.commentsId) from PdwhPubComment t where t.pubId =:pdwhPubId";
    return (Long) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

}
