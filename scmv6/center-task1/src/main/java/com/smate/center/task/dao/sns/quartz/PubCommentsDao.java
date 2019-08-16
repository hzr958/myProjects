package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.smate.center.task.model.sns.quartz.PubComment;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果评论持久层.
 * 
 * @author LY
 * 
 */
@Repository
public class PubCommentsDao extends SnsHibernateDao<PubComment, Long> {

  /**
   * 查看当天psnA对某条资源的评论次数
   */
  public Long getCommentCount(Long pubId, Long psnId) {
    String hql =
        "select count(*) from PubComment p where  p.psnId=:psnId and p.pubId=:pubId and to_char(p.createDate,'YYYY-MM-dd')=to_char(sysdate,'YYYY-MM-dd')";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubComment> queryComments(Long pubId, int pageNo, int pageSize) {
    String hql = "from PubComment t where t.pubId=:pubId and t.isAudit=1 order by t.createDate desc";
    return super.createQuery(hql).setParameter("pubId", pubId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize).list();

  }

  public List<PubComment> getComments(Long pubId) {
    String hql = "from PubComment t where t.pubId=:pubId and t.isAudit=1 order by t.createDate desc";
    return super.createQuery(hql).setParameter("pubId", pubId).list();

  }

  public Long getCommentsCount(Long pubId) {
    String hql = "select count (*) from PubComment t where t.pubId=:pubId and t.isAudit=1";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

}
